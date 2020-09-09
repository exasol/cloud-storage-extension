package org.mockito

import org.mockito.stubbing.Stubber

/** Extra helper functions for mockito mocking */
object ExtraMockito {

  /**
   * Delegates the call to <code>Mockito.doReturn(toBeReturned, toBeReturnedNext)</code> but fixes
   * the following compiler issue that happens because the overloaded vararg on the Java side
   *
   * {{{
   *    Error:(33, 25) ambiguous reference to overloaded definition, both method doReturn in class
   *    Mockito of type (x$1: Any, x$2: Object*)org.mockito.stubbing.Stubber and  method doReturn
   *    in class Mockito of type (x$1: Any)org.mockito.stubbing.Stubber match argument types
   *    (`Type`)
   * }}}
   *
   * This is adapted from mockito-scala project,
   *  - mockito-scala/blob/master/core/src/main/scala/org/mockito/MockitoAPI.scala#L59
   */
  def doReturn[T](toBeReturned: T, toBeReturnedNext: T*): Stubber =
    Mockito.doReturn(
      toBeReturned,
      toBeReturnedNext.map(_.asInstanceOf[Object]): _*
    )

}
