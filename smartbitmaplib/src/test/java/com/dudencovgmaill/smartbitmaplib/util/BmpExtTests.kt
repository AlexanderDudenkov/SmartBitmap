/*
package com.dudencovgmaill.smartbitmaplib.util

import android.graphics.Bitmap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [16,17,18,19,21,22,23,24,25,26,27,28])
class BmpExtTests {

    @Test
    fun int_to_bytes_and_back_test() {

        val exp: Int = 10000000
        val act: Int? = exp.bytes().toInt()

        assertEquals(exp, act)
    }

    @Test
    fun bmp_to_bytes_and_back_1() {
        bmp_to_bytes_and_back(1000,1000,Bitmap.Config.ARGB_8888)
    }

    @Test
    fun bmp_to_bytes_and_back_2() {
        bmp_to_bytes_and_back(1,1,Bitmap.Config.ARGB_8888)
    }

    private fun bmp_to_bytes_and_back(width: Int, height: Int, config: Bitmap.Config){

        val exp: Bitmap = Bitmap.createBitmap(width, height, config)
        val act: Bitmap? = exp.toByteArray()?.toBitmap(exp.width, exp.height, exp.config)

        assertTrue(exp.sameAs(act))
    }

}*/
