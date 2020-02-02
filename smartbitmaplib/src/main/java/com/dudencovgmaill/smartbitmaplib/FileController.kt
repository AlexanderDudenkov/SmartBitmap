package com.dudencovgmaill.smartbitmaplib

import android.os.Parcelable
import com.dudencovgmaill.smartbitmaplib.util.bytes
import com.dudencovgmaill.smartbitmaplib.util.marshall
import com.dudencovgmaill.smartbitmaplib.util.unmarshall
import java.io.File
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 *Class for working with files.
 * */
class FileController {

    /**
     * Method for inserting any bytes into image file in it end.
     *
     * @param [file] the file into which [bytes] are inserted
     * @param [bytes] any bytes.
     *
     * @return the file is contained [bytes] in it end.
     * */
    @Throws
    fun insert(file: File, bytes: ByteArray): File? {

        checkFile(file)

        file.copyTo(
            File(
                file.parentFile!!,
                "${file.nameWithoutExtension}${PREFIX_TO_NAME}_${System.currentTimeMillis()}.${file.extension}"
            )
        ).let { copy ->

            copy.appendBytes(bytes)
            copy.appendBytes(bytes.size.bytes())

            return copy
        }
    }

    /**
     * Return bytes from file which were before inserted by {@link #insert(File,ByteArray): File?}.
     *
     *@param [file] the file is contained "extra" bytes.
     *
     * */
    @Throws
    fun extractBytes(file: File): ByteArray {

        checkFile(file)

        val dataSize: Int
        val channel = file.inputStream().channel

        with(ByteBuffer.allocate(METADATA_BYTES_SIZE)) {
            order(ByteOrder.LITTLE_ENDIAN)
            channel.read(this, file.length() - METADATA_BYTES_SIZE)
            dataSize = getInt(0)
            clear()
        }

        return with(ByteBuffer.allocate(dataSize + METADATA_BYTES_SIZE)) {
            order(ByteOrder.LITTLE_ENDIAN)
            channel.read(this, file.length() - dataSize - METADATA_BYTES_SIZE)
            channel.close()
            array().dropLast(METADATA_BYTES_SIZE).toByteArray()
        }
    }

    /**
     * Method for inserting object into file in it end.
     *
     * @param [file] the file into which [any] are inserted
     * @param [any] object extended from [Parcelable]
     *
     * @return the file is contained [any] in it end.
     * */
    @Throws
    fun <T : Parcelable> insert(file: File, any: T): File? {
        return any.marshall()?.let { insert(file, it) }
    }

    /**
     * Return object from file which were before inserted by {@link #insert(File,ByteArray): File?}.
     *
     *@param [file] the file is contained "extra" bytes.
     *@param [objectClass] class extended from [Parcelable]
     * */
    @Throws
    fun <T : Parcelable> extractObject(file: File, objectClass: Class<T>): T? {
        return extractBytes(file).unmarshall(objectClass)
    }

    @Throws
    private fun checkFile(file: File) {

        val error: String? = when {
            !file.isFile -> "it is not a file!"
            !file.isAbsolute -> "file path isn't absolute!"
            !file.exists() -> "file is not exist!"
            !file.canWrite() -> "file is not rewritable!"
            else -> null
        }

        if (error != null) throw FileNotFoundException(error)
    }

    /*fun isBitmapsEquals(source: Bitmap, checked: Bitmap): Boolean {

        val checkedBytesWithExtra = checked.toByteArray() ?: return false
        val sourceBytesWithExtra =
            insert(source, extractBytes(checked) ?: return false)?.toByteArray() ?: return false

        return sourceBytesWithExtra.contentEquals(checkedBytesWithExtra)
    }*/

    companion object {
        private const val PREFIX_TO_NAME = "_modified"
        private const val METADATA_BYTES_SIZE: Int = Int.SIZE_BYTES
    }
}