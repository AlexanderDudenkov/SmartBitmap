package com.dudencovgmaill.smartbitmaplib

import android.os.Parcelable
import com.dudencovgmaill.smartbitmaplib.util.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 *Class for working with files.
 * */
open class FileController : IFileController {

    /**
     * Random value for recognizing the file
     * */
    protected val code = "code"

    protected open val dataBytesSize: Int = Int.SIZE_BYTES
    protected open val codeSize: Int = code.length

    @Throws
    override fun insert(file: File, bytes: ByteArray): File {

        checkFile(file)

        file.copyTo(File(file.parentFile!!, getNewFileName(file))).let { copy ->

            copy.appendBytes(bytes)
            copy.appendBytes(bytes.size.toByteArray())
            copy.appendBytes(code.toByteArray())

            return copy
        }
    }

    @Throws
    override fun <T : Parcelable> insert(file: File, any: T): File {
        return insert(file, any.marshall())
    }

    @Throws
    override fun extractBytes(file: File): ByteArray {

        checkFile(file)
        isCodeTheSame(extractCode(file), code.toByteArray())
        with(ByteBuffer.allocate(extractDataSize(file).toInt())) {
            extractStreamBytes(file).buffered().read(this)
            return this.array()
        }
    }

    @Throws
    override fun <T : Parcelable> extractObject(file: File, objectClass: Class<T>): T {
        return extractBytes(file).unmarshall(objectClass)
    }

    @Throws
    override fun extractStreamBytes(file: File): FileInputStream {
        checkFile(file)
        isCodeTheSame(extractCode(file), code.toByteArray())

        return extractStreamBytes(file, extractDataSize(file).toInt())
    }

    open fun extractStreamBytes(file: File, dataSize: Int): FileInputStream {
        with(file.inputStream()) {
            skip(file.length() - dataSize - dataBytesSize - codeSize)
            return this
        }
    }

    open fun extractDataSize(file: File): ByteArray {
        with(ByteBuffer.allocate(dataBytesSize)) {
            order(ByteOrder.LITTLE_ENDIAN)
            file.read(this, file.length() - dataBytesSize - codeSize)
            return this.array()
        }
    }

    open fun extractCode(file: File): ByteArray {
        with(ByteBuffer.allocate(codeSize)) {
            order(ByteOrder.LITTLE_ENDIAN)
            file.read(this, file.length() - codeSize)
            return this.array()
        }
    }

    open fun getNewFileName(file: File) =
        "${file.nameWithoutExtension}_modified_${System.currentTimeMillis()}.${file.extension}"

    @Throws
    open fun checkFile(file: File) {

        val error: String? = when {
            !file.isFile -> "it is not a file!"
            !file.isAbsolute -> "file path isn't absolute!"
            !file.exists() -> "file is not exist!"
            !file.canWrite() -> "file is not rewritable!"
            else -> null
        }

        if (error != null) throw FileNotFoundException(error)
    }

    @Throws
    open fun isCodeTheSame(codeAct: ByteArray, codeExp: ByteArray) {
        if (!codeAct.contentEquals(codeExp)) throw Exception("this file wasn't created by this library!")
    }
}