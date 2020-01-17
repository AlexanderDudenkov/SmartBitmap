package com.dudencovgmaill.smartbitmaplib

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.dudencovgmaill.smartbitmaplib.util.toByteArray
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.nio.ByteBuffer

@RunWith(JUnit4::class)
class BitmapControllerTests2 {

    private var subject: BitmapController? = null

    @Before
    fun setUp() {
        subject = BitmapController()
    }

    @After
    fun tearDown() {
        subject = null
    }

    @Test
    fun `insert bytes into bmp`() {

        val exp: Bitmap = mock(Bitmap::class.java).apply {
            `when`(width).thenReturn(1)
            `when`(height).thenReturn(2)
            `when`(config).thenReturn(Bitmap.Config.RGB_565)
            //`when`(toByteArray()).thenReturn(ByteArray(3))
            `when`(byteCount).thenReturn(3)
        }

        val ms = mock(Bitmap::class.java)

        val bmpInput: Bitmap = mock(Bitmap::class.java).apply {
            `when`(width).thenReturn(1)
            `when`(height).thenReturn(1)
            `when`(config).thenReturn(Bitmap.Config.RGB_565)
            doNothing().`when`(this).copyPixelsToBuffer(ByteBuffer.allocate(2))
            `when`(Bitmap.createBitmap(1,2,Bitmap.Config.RGB_565)).thenReturn(exp)
            //`when`(toByteArray()).thenReturn(ByteArray(2))
            `when`(byteCount).thenReturn(2)
        }

        val bytesInput: ByteArray = ByteArray(1) { 1 }

        val act: Bitmap? = subject?.insert(bmpInput, bytesInput)

        assertEquals(exp.width, act?.width)
        assertEquals(exp.height, act?.height)
        assertEquals(exp.config, act?.config)
        //assertEquals(exp.toByteArray(), act?.toByteArray())
        assertEquals(exp.byteCount, act?.byteCount)
    }

    private data class Model(val data: String?) : Parcelable {

        constructor(parcel: Parcel) : this(parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(data)
        }

        override fun describeContents(): Int {
            return hashCode()
        }

        companion object CREATOR : Parcelable.Creator<Model> {
            override fun createFromParcel(parcel: Parcel): Model {
                return Model(parcel)
            }

            override fun newArray(size: Int): Array<Model?> {
                return arrayOfNulls(size)
            }
        }
    }
}