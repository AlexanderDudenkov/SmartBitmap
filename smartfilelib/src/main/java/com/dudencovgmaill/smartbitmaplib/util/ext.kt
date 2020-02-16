package com.dudencovgmaill.smartbitmaplib.util

import android.os.Parcel
import android.os.Parcelable
import java.io.File
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Transform the object to the byte array.
 *
 * @return the byte array after object marshalling .
 * */
fun <T : Parcelable> T.marshall(): ByteArray {

    var bytes: ByteArray

    Parcel.obtain().let {
        it.writeValue(this)
        bytes = it.marshall()
        it.recycle()
    }

    return bytes
}

/**
 * Transform the byte array to the object.
 *
 * @param [anyClass] the class of a desired type [T].
 * */
@Throws(RuntimeException::class)
@Suppress("UNCHECKED_CAST")
fun <T : Parcelable> ByteArray.unmarshall(anyClass: Class<T>): T {

    var any: T

    Parcel.obtain().let {
        it.unmarshall(this, 0, size)
        it.setDataPosition(0)
        any = it.readValue(anyClass.classLoader) as T
        it.recycle()
    }

    return any
}

/**
 * Return toByteArray from the integer.
 * */
fun Int.toByteArray(): ByteArray = ByteBuffer.allocate(Int.SIZE_BYTES).let {
    it.order(ByteOrder.LITTLE_ENDIAN)
    it.putInt(this).array()
}

/**
 * Return the integer from it toByteArray.
 * */
fun ByteArray.toInt(): Int = ByteBuffer.wrap(this).let {
    it.order(ByteOrder.LITTLE_ENDIAN)
    it.int
}

fun File.read(buffer: ByteBuffer, from: Long = 0) {
    inputStream().read(buffer, from)
}

fun <T: InputStream> T.read(buffer: ByteBuffer, from: Long = 0) {
    with(buffered()) {
        skip(from)
        repeat(buffer.capacity()) { buffer.put(read().toByte()) }
        close()
    }
}