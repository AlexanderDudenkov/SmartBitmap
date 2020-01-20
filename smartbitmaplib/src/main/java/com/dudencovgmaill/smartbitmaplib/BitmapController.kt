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
    /*fun insert(bitmap: Bitmap, bytes: ByteArray): Bitmap? {

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
    }*/

    fun insert(bitmap: Bitmap, bytes: ByteArray): Bitmap? {

        return insert(BitmapData(bitmap), bytes)?.let {
            it.bytes.toBitmap(it.width, it.height, it.config)
        }
    }

    private fun insert(bitmapData: BitmapData, bytes: ByteArray): BitmapData? {

        var extraHeight = 1

        var bmpBytes = bitmapData.bytes

        if (bmpBytes.isEmpty() && bytes.isEmpty()) return null

        bmpBytes = bmpBytes.plus(bytes)

        if ((bytes.size + Int.SIZE_BYTES * 2) % bitmapData.widthByteCount != 0) {

            extraHeight = calculateExtraHeight(bitmapData.widthByteCount, bytes.size)

            val emptyBytesCount: Int =
                bitmapData.widthByteCount * extraHeight - (bytes.size + Int.SIZE_BYTES * 2)

            bmpBytes = bmpBytes.plus(ByteArray(emptyBytesCount))
        }

        bmpBytes = bmpBytes.plus(bitmapData.bytes.size.bytes())//start index of bytes in bmp
        bmpBytes =
            bmpBytes.plus((bitmapData.bytes.size + bytes.lastIndex).bytes())//end index of bytes in bmp

        return BitmapData(
            bitmapData.width,
            bitmapData.height + extraHeight,
            bitmapData.config,
            bmpBytes,
            bitmapData.widthByteCount
        )
    }

    private fun calculateExtraHeight(widthByteCount: Int, bytesCount: Int): Int {

        return if (widthByteCount > (bytesCount + Int.SIZE_BYTES * 2)) {
            (widthByteCount / (bytesCount + Int.SIZE_BYTES * 2)) + 1
        } else {
            ((bytesCount + Int.SIZE_BYTES * 2) / widthByteCount) + 1
        }
    }

    private class BitmapData {

        var bmp: Bitmap? = null
            private set
        var width: Int
            private set
        var height: Int
            private set
        var config: Bitmap.Config
            private set
        var bytes: ByteArray
            private set
        var widthByteCount: Int
            private set

        constructor(bmp: Bitmap) {
            this.bmp = bmp
            this.width = bmp.width
            this.height = bmp.height
            this.config = bmp.config
            this.bytes = bmp.toByteArray()
            this.widthByteCount = getWidthBytes(bmp)
        }

        constructor(
            width: Int,
            height: Int,
            config: Bitmap.Config,
            bytes: ByteArray,
            widthByteCount: Int
        ) {
            this.width = width
            this.height = height
            this.config = config
            this.bytes = bytes
            this.widthByteCount = widthByteCount
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
    }

    private inline fun insertTest(test: String, test2: (v: String) -> String): String = test2(test)
    //private inline fun insertTest(test: Int, test2: (v: Int) -> Int): Int = test2(test)
    private class BitmapDataTest(val data: Int)

    private fun test(o: BitmapDataTest): Int = o.data

    private fun test2(o: BitmapDataTest): BitmapDataTest = BitmapDataTest(1 + o.data)

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
    /*fun extractBytes(bitmap: Bitmap): ByteArray? {
        val bmpBytes = bitmap.toByteArray()

        if (bmpBytes != null && bmpBytes.isNotEmpty()) {

            val startIndex = startIndex(bmpBytes)
            val endIndex = endIndex(bmpBytes)

            return ByteArray(endIndex - startIndex + 1).also {
                bmpBytes.copyInto(it, 0, startIndex, endIndex)
            }
        }
        return null
    }*/

    fun extractBytes(bitmap: Bitmap): ByteArray? = extractBytes(bitmap.toByteArray())


    private fun extractBytes(bitmapBytes: ByteArray?): ByteArray? {
        val bmpBytes = bitmapBytes

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

    /*private fun getWidthBytes(bmp: Bitmap): Int {
        return getPixelSize(bmp.config) * bmp.width
    }

    private fun getPixelSize(config: Bitmap.Config): Int {
        return when (config) {
            Bitmap.Config.ALPHA_8 -> 1
            Bitmap.Config.RGB_565, Bitmap.Config.ARGB_4444 -> 2
            Bitmap.Config.ARGB_8888 -> 4
            else -> 8
        }
    }*/

    private fun startIndex(array: ByteArray): Int =
        array.copyOfRange(array.size - 2 * Int.SIZE_BYTES, array.size - Int.SIZE_BYTES).toInt()

    private fun endIndex(array: ByteArray): Int =
        array.copyOfRange(array.size - Int.SIZE_BYTES, array.size).toInt()
}