package com.dudencovgmaill.smartbitmaplib

import android.graphics.Bitmap
import android.os.Parcelable
import com.dudencovgmaill.smartbitmaplib.util.*

/**
 *Class for working with bitmaps.
 * */
class BitmapController {

    /**
     * Method for inserting any bytes into bitmap code in it end.
     *
     * @param [bitmap] the bitmap into which [bytes] are inserted
     * @param [bytes] any bytes.
     *
     * @return the bitmap is contained [bytes] in it end.
     * */
    fun insert(bitmap: Bitmap, bytes: ByteArray): Bitmap? {

        var bmpBytes = bitmap.toByteArray()

        if (bytes.isEmpty() && bmpBytes == null) return bitmap

        var layer = 1

        bmpBytes = bmpBytes!!.plus(bytes)

        if ((bytes.size + Int.SIZE_BYTES * 2) % getWidthBytes(bitmap) != 0) {

            layer = if (getWidthBytes(bitmap) > (bytes.size + Int.SIZE_BYTES * 2)) {
                (getWidthBytes(bitmap) / (bytes.size + Int.SIZE_BYTES * 2)) + 1
            } else {
                ((bytes.size + Int.SIZE_BYTES * 2) / getWidthBytes(bitmap)) + 1
            }

            val emptyBytesCount: Int =
                getWidthBytes(bitmap) * layer - (bytes.size + Int.SIZE_BYTES * 2)

            bmpBytes = bmpBytes.plus(ByteArray(emptyBytesCount))
        }

        bmpBytes = bmpBytes.plus(bitmap.byteCount.bytes())//start index of bytes in bmp
        bmpBytes =
            bmpBytes.plus((bitmap.byteCount + bytes.lastIndex).bytes())//end index of bytes in bmp

        return bmpBytes.toBitmap(bitmap.width, bitmap.height + layer, bitmap.config)
    }

    /**
     * Method for inserting object into bitmap code in it end.
     *
     * @param [bitmap] the bitmap into which [any] are inserted
     * @param [any] object extended from [Parcelable]
     *
     * @return the bitmap is contained [any] in it end.
     * */
    fun <T : Parcelable> insert(bitmap: Bitmap, any: T): Bitmap? {
        return any.marshall()?.let { insert(bitmap, it) }
    }

    /**
     * Return bytes from bitmap which were before inserted by {@link #insert(Bitmap,ByteArray): Bitmap?}.
     *
     *@param [bitmap] the bitmap is contained "extra" bytes.
     *
     *
     * */
    fun extractBytes(bitmap: Bitmap): ByteArray? {
        val bmpBytes = bitmap.toByteArray()

        if (bmpBytes != null && bmpBytes.isNotEmpty()) {

            val startIndex = startIndex(bmpBytes)
            val endIndex = endIndex(bmpBytes)

            return ByteArray(endIndex - startIndex + 1).also {
                bmpBytes.copyInto(it, 0, startIndex, endIndex)
            }
        }
        return null
    }

    fun <T : Parcelable> extractObject(bitmap: Bitmap, objectClass: Class<T>): T? {
        return extractBytes(bitmap)?.unmarshall(objectClass)
    }

    fun overlay(source: Bitmap, newLay: Bitmap): Bitmap {

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

    fun isBitmapsEquals(source: Bitmap, checked: Bitmap): Boolean {

        val checkedBytesWithExtra = checked.toByteArray() ?: return false
        val sourceBytesWithExtra =
            insert(source, extractBytes(checked) ?: return false)?.toByteArray() ?: return false

        return sourceBytesWithExtra.contentEquals(checkedBytesWithExtra)
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

    private fun startIndex(array: ByteArray): Int =
        array.copyOfRange(array.size - 2 * Int.SIZE_BYTES, array.size - Int.SIZE_BYTES).toInt()

    private fun endIndex(array: ByteArray): Int =
        array.copyOfRange(array.size - Int.SIZE_BYTES, array.size).toInt()
}