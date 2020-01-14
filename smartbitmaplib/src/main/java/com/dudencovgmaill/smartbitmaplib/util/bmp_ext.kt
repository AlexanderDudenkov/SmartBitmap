package com.dudencovgmaill.smartbitmaplib.util

import android.graphics.Bitmap
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

@Throws(BufferUnderflowException::class)
fun Bitmap.toByteArray(): ByteArray? {

    val size: Int = byteCount
    val buffer = ByteBuffer.allocate(size)
    copyPixelsToBuffer(buffer)

    return buffer.array()
}

fun ByteArray.toBitmap(
    width: Int,
    height: Int,
    config: Bitmap.Config
): Bitmap? = ByteBuffer.wrap(this).toBitmap(width, height, config)


fun ByteBuffer.toBitmap(
    width: Int,
    height: Int,
    config: Bitmap.Config

): Bitmap? {
    return Bitmap.createBitmap(width, height, config).also {
        it.copyPixelsFromBuffer(this)
    }
}

fun Bitmap.getPixels(): IntArray {

    val array = IntArray(byteCount)
    this.getPixels(array, 0, width, 0, 0, width, height)
    return array
}

fun Bitmap.extractBytes(from: Int, to: Int = byteCount): ByteArray? {

    return ByteArray(to - from).also { toByteArray()?.copyInto(it, 0, from, to) }
}

fun Int.bytes(): ByteArray = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this).array()

fun ByteArray.toInt(): Int = ByteBuffer.wrap(this).int