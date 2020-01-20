/*
package com.dudencovgmaill.smartbitmaplib.util

import android.os.Parcel
import android.os.Parcelable
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [*/
/*16, 17, 18, 19, 21, 22, 23,*//*
 24*/
/*, 25, 26, 27, 28*//*
])
class ExtTests {

    @Test
    fun marshall_unmarshall_test1() {

        val exp = ""
        val act: String? = Model(exp).marshall()?.unmarshall(Model::class.java)?.data

        assertEquals(exp, act)
    }

    @Test
    fun marshall_unmarshall_test2() {

        val exp = "test"
        val act: String? = Model("").marshall()?.unmarshall(Model::class.java)?.data

        assertNotEquals(exp, act)
    }

    @Test
    fun marshall_unmarshall_test3() {

        val exp = String(CharArray(1000000) { 'w' })
        val act: String? = Model(exp).marshall()?.unmarshall(Model::class.java)?.data

        assertEquals(exp, act)
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
}*/
