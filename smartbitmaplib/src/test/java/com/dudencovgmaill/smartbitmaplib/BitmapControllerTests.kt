package com.dudencovgmaill.smartbitmaplib

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [/*16, 17, 18, 19, 21, 22, 23,*/ 24/*, 25, 26, 27, 28*/])
class BitmapControllerTests {

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

        val exp: ByteArray = ByteArray(1) { 1 }
        val bmpWithExtraByte =
            subject?.insert(Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888), exp)
        val act: ByteArray? = subject?.extractBytes(bmpWithExtraByte!!)

        Assert.assertArrayEquals(exp, act)
    }

    @Test
    fun `insert object into bmp`() {

        val exp: String = String(CharArray(2) { 'w' })
        val bmpWithExtraByte =
            subject?.insert(Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888), Model(exp))
        val act: String? = subject?.extractObject(bmpWithExtraByte!!, Model::class.java)?.data

        Assert.assertEquals(exp, act)
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