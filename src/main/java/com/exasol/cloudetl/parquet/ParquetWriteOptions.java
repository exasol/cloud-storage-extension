package com.exasol.cloudetl.parquet;

import static java.util.Locale.ENGLISH;

import java.util.Objects;

import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import com.exasol.cloudetl.storage.StorageProperties;

/** Parquet writer options. */
public final class ParquetWriteOptions {
    /** Block size. */
    public final int blockSize;
    /** Page size. */
    public final int pageSize;
    /** Compression codec. */
    public final CompressionCodecName compressionCodec;
    /** Dictionary encoding flag. */
    public final boolean enableDictionaryEncoding;
    /** Validation flag. */
    public final boolean enableValidation;

    /** Create options. */
    public ParquetWriteOptions(final int blockSize, final int pageSize, final CompressionCodecName compressionCodec,
            final boolean enableDictionaryEncoding, final boolean enableValidation) {
        this.blockSize = blockSize;
        this.pageSize = pageSize;
        this.compressionCodec = compressionCodec;
        this.enableDictionaryEncoding = enableDictionaryEncoding;
        this.enableValidation = enableValidation;
    }

    /** Create options from storage properties. */
    public static ParquetWriteOptions from(final StorageProperties params) {
        final String codec = params.get("PARQUET_COMPRESSION_CODEC").isDefined()
                ? params.get("PARQUET_COMPRESSION_CODEC").get().toUpperCase(ENGLISH)
                : "";
        final CompressionCodecName compressionCodec;
        switch (codec) {
        case "SNAPPY":
            compressionCodec = CompressionCodecName.SNAPPY;
            break;
        case "GZIP":
            compressionCodec = CompressionCodecName.GZIP;
            break;
        case "LZO":
            compressionCodec = CompressionCodecName.LZO;
            break;
        default:
            compressionCodec = CompressionCodecName.UNCOMPRESSED;
            break;
        }
        final int blockSize = Integer.parseInt(params.get("PARQUET_BLOCK_SIZE").isDefined()
                ? params.get("PARQUET_BLOCK_SIZE").get()
                : Integer.toString(ParquetWriter.DEFAULT_BLOCK_SIZE));
        final int pageSize = Integer.parseInt(params.get("PARQUET_PAGE_SIZE").isDefined()
                ? params.get("PARQUET_PAGE_SIZE").get()
                : Integer.toString(ParquetWriter.DEFAULT_PAGE_SIZE));
        final boolean dictionary = Boolean.parseBoolean(params.get("PARQUET_DICTIONARY_ENCODING").isDefined()
                ? params.get("PARQUET_DICTIONARY_ENCODING").get()
                : "true");
        final boolean validation = Boolean
                .parseBoolean(params.get("PARQUET_VALIDATION").isDefined() ? params.get("PARQUET_VALIDATION").get() : "true");
        return new ParquetWriteOptions(blockSize, pageSize, compressionCodec, dictionary, validation);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ParquetWriteOptions)) {
            return false;
        }
        final ParquetWriteOptions other = (ParquetWriteOptions) obj;
        return this.blockSize == other.blockSize && this.pageSize == other.pageSize
                && this.enableDictionaryEncoding == other.enableDictionaryEncoding
                && this.enableValidation == other.enableValidation && this.compressionCodec == other.compressionCodec;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.blockSize, this.pageSize, this.compressionCodec, this.enableDictionaryEncoding,
                this.enableValidation);
    }
}
