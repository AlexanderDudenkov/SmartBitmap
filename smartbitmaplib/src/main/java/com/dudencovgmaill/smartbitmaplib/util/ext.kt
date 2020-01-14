package com.dudencovgmaill.smartbitmaplib.util

import android.os.Parcel
import android.util.Log

fun Any?.d(msg: String) {
    this?.let { Log.d(this::class.java.simpleName, msg) }
}

fun <T> T.marshall(): ByteArray? {

    var bytes: ByteArray?

    Parcel.obtain().let {
        it.writeValue(this)
        bytes = it.marshall()
        it.recycle()
    }

    return bytes
}

fun <T> ByteArray.unmarshall(anyClass: Class<T>): T? {

    var any: T?

    Parcel.obtain().let {
        it.unmarshall(this, 0, size)
        it.setDataPosition(0)
        any = it.readValue(anyClass.classLoader)as? T
        it.recycle()
    }

    return any
}