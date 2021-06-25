package com.exasol.cloudetl.emitter

import scala.concurrent.Await
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.Duration
import scala.util.Failure
import scala.util.Success

import com.exasol.ExaIterator
import com.exasol.cloudetl.source.Source
import com.exasol.cloudetl.storage.StorageProperties
import com.exasol.common.data.Row

import akka.Done
import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Source => AkkaSource}
import com.typesafe.scalalogging.LazyLogging

/**
 * A class that uses actor system to read a file and emit into Exasol,
 * with an asynchronous boundary between the two stages.
 */
class DataEmitterPipeline(source: Source, properties: StorageProperties)
    extends Emitter
    with LazyLogging {
  private[this] implicit val system: ActorSystem = ActorSystem()
  private[this] implicit val dispatcher: ExecutionContextExecutor = system.dispatcher

  /**
   * @inheritdoc
   */
  override final def emit(iterator: ExaIterator): Unit = {
    val pipeline = AkkaSource
      .fromIterator(() => source.stream().grouped(getChunkSize()))
      .async
      .buffer(getBufferSize(), OverflowStrategy.backpressure)
      .runForeach(buffer => emitBuffer(buffer, iterator))
    try {
      Await
        .ready(pipeline, Duration.Inf)
        .onComplete {
          case Success(Done)      => logger.info("Finished emitter pipeline.")
          case Failure(exception) => throw new IllegalStateException(exception)
        }
    } catch {
      case exception: Throwable =>
        throw new IllegalStateException("Failed to read and emit data from file.", exception)
    } finally {
      val _ = system.terminate()
    }
  }

  private[this] def emitBuffer(buffer: Seq[Row], iterator: ExaIterator): Unit =
    buffer.foreach { row =>
      val columns: Seq[Object] = row.getValues().map(_.asInstanceOf[AnyRef])
      iterator.emit(columns: _*)
    }

  private[this] def getChunkSize(): Int = properties.getChunkSize()

  private[this] def getBufferSize(): Int = properties.getBufferSize()

}
