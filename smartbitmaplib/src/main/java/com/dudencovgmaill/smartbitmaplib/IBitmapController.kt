package com.dudencovgmaill.smartbitmaplib

import android.graphics.Bitmap

interface IBitmapController {

    fun addBytesToEnd(source: Bitmap, bytes: ByteArray): Bitmap?
    fun overlay(source: Bitmap, newLay: Bitmap): Bitmap
    fun extractBytes(source: Bitmap): ByteArray?
}