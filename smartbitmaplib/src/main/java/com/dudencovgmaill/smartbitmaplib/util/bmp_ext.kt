@file:JvmName("bmp_ext")
package com.dudencovgmaill.smartbitmaplib.util

import android.graphics.Bitmap
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

/**
 * Return bitmap bytes from bitmap.
 * */
@Throws(BufferUnderflowException::class)
fun Bitmap.toByteArray(): ByteArray {

    val size: Int = byteCount
    val buffer = ByteBuffer.allocate(size)
    copyPixelsToBuffer(buffer)

    return buffer.array()
}

/**
 * Return bitmap from bitmap bytes.
 *
 * @param width    The width of the bitmap
 * @param height   The height of the bitmap
 * @param config   The bitmap config to create.
 * */
fun ByteArray.toBitmap(
    width: Int,
    height: Int,
    config: Bitmap.Config
): Bitmap? = ByteBuffer.wrap(this).toBitmap(width, height, config)

/**
 * Return bitmap from bitmap bytes in byte buffer.
 *
 * @param width    The width of the bitmap
 * @param height   The height of the bitmap
 * @param config   The bitmap config to create.
 * */
fun ByteBuffer.toBitmap(
    width: Int,
    height: Int,
    config: Bitmap.Config

): Bitmap? {
    return Bitmap.createBitmap(width, height, config).also {
        it.copyPixelsFromBuffer(this)
    }
}

/**
 * Return bitmap pixels.
 * */
fun Bitmap.getPixels(): IntArray {

    val array = IntArray(byteCount)
    this.getPixels(array, 0, width, 0, 0, width, height)
    return array
}

/**
 * Return bytes from the integer.
 * */
fun Int.bytes(): ByteArray = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this).array()

/**
 * Return the integer from it bytes.
 * */
fun ByteArray.toInt(): Int = ByteBuffer.wrap(this).int