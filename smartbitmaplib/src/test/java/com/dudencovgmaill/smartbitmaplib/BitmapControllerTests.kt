package com.dudencovgmaill.smartbitmaplib

import android.graphics.Bitmap
import com.dudencovgmaill.smartbitmaplib.util.toByteArray
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [16, 17, 18, 19, 21, 22, 23, 24, 25, 26, 27, 28])
class BitmapControllerTests {

    private var subject: IBitmapController? = null

    @Before
    fun setUp() {
        subject = BitmapController()
    }

    @After
    fun tearDown() {
        subject = null
    }

    @Test
    fun addBytesToEndTest() {

        val exp: Byte = 1
        val input = ByteArray(1) { exp }
        val act =
            subject?.addBytesToEnd(Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888), input)?.toByteArray()
                ?.lastOrNull()
                ?.toInt()

        Assert.assertEquals(exp, act)
    }
}