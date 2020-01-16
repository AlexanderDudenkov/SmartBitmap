package com.dudencovgmaill.smartbitmaplib.util

import android.os.Parcel
import android.os.Parcelable

/**
 * Transform the object to the byte array.
 *
 * @return the byte array after object marshalling .
 * */
fun <T : Parcelable> T.marshall(): ByteArray? {

    var bytes: ByteArray?

    Parcel.obtain().let {
        it.writeValue(this)
        bytes = it.marshall()
        it.recycle()
    }

    return bytes
}

/**
 * Transform the byte array to the object.
 *
 * @param [anyClass] the class of a desired type [T].
 * */
@Suppress("UNCHECKED_CAST")
fun <T : Parcelable> ByteArray.unmarshall(anyClass: Class<T>): T? {

    var any: T?

    Parcel.obtain().let {
        it.unmarshall(this, 0, size)
        it.setDataPosition(0)
        any = it.readValue(anyClass.classLoader) as? T
        it.recycle()
    }

    return any
}