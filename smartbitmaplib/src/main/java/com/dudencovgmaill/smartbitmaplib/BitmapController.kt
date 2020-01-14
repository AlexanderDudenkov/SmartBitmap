package com.dudencovgmaill.smartbitmaplib

import android.graphics.Bitmap
import com.dudencovgmaill.smartbitmaplib.util.*

class BitmapController : IBitmapController {

    override fun addBytesToEnd(source: Bitmap, bytes: ByteArray): Bitmap? {

        var bmpBytes = source.toByteArray()

        if (bytes.isEmpty() && bmpBytes == null) return source

        var layer = 1

        bmpBytes = bmpBytes!!.plus(bytes)

        if ((bytes.size + Int.SIZE_BYTES * 2) % getWidthBytes(source) != 0) {

            layer = if (getWidthBytes(source) > (bytes.size + Int.SIZE_BYTES * 2)) {
                (getWidthBytes(source) / (bytes.size + Int.SIZE_BYTES * 2)) + 1
            } else {
                ((bytes.size + Int.SIZE_BYTES * 2) / getWidthBytes(source)) + 1
            }

            val emptyBytesCount: Int =
                getWidthBytes(source) * layer - (bytes.size + Int.SIZE_BYTES * 2)

            bmpBytes = bmpBytes.plus(ByteArray(emptyBytesCount))
        }

        bmpBytes = bmpBytes.plus(source.byteCount.bytes())//start index of bytes in bmp
        bmpBytes = bmpBytes.plus((source.byteCount + bytes.lastIndex).bytes())//end index of bytes in bmp

        return bmpBytes.toBitmap(source.width, source.height + layer, source.config)
    }

    private fun getWidthBytes(bmp: Bitmap): Int {
        return getPixelSize(bmp.config) * bmp.width
    }

    private fun getPixelSize(config: Bitmap.Config): Int {
        return when (config) {
            Bitmap.Config.ALPHA_8 -> 1
            Bitmap.Config.RGB_565, Bitmap.Config.ARGB_4444 -> 2
            Bitmap.Config.ARGB_8888 -> 4
            else -> 8
        }
    }

    override fun overlay(source: Bitmap, newLay: Bitmap): Bitmap {

        return source.apply {
            setPixels(
                getPixels(),
                0,
                newLay.width,
                0,
                0,
                newLay.width,
                newLay.height
            )
        }
    }

    override fun extractBytes(source: Bitmap): ByteArray? {

        val bmpBytes = source.toByteArray()

        if (bmpBytes != null && bmpBytes.isNotEmpty()) {

            val startIndex = startIndex(bmpBytes)
            val endIndex = endIndex(bmpBytes)

            return ByteArray(endIndex - startIndex + 1).also {
                bmpBytes.copyInto(it, 0, startIndex, endIndex)
            }
        }
        return null
    }

    private fun startIndex(array: ByteArray): Int =
        array.copyOfRange(array.size - 2 * Int.SIZE_BYTES, array.size - Int.SIZE_BYTES).toInt()

    private fun endIndex(array: ByteArray): Int =
        array.copyOfRange(array.size - Int.SIZE_BYTES, array.size).toInt()
}