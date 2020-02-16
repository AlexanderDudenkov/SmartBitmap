package com.dudencovgmaill.smartbitmaplib

import android.os.Parcelable
import java.io.File
import java.io.FileInputStream

interface IFileController {

    /**
     * Method for inserting any toByteArray into image file in it end.
     *
     * @param [file] the file into which [bytes] are inserted
     * @param [bytes] any toByteArray.
     *
     * @return the file is contained [bytes] in it end.
     * */
    @Throws
    fun insert(file: File, bytes: ByteArray): File

    /**
     * Method for inserting object into file in it end.
     *
     * @param [file] the file into which [any] are inserted
     * @param [any] object extended from [Parcelable]
     *
     * @return the file is contained [any] in it end.
     * */
    @Throws
    fun <T : Parcelable> insert(file: File, any: T): File

    /**
     * Return toByteArray from file which were before inserted by {@link #insert(File,ByteArray): File?}.
     *
     *@param [file] the file is contained "extra" toByteArray.
     *
     * */
    @Throws
    fun extractBytes(file: File): ByteArray

    /**
     * Return object from file which were before inserted by {@link #insert(File,ByteArray): File?}.
     *
     *@param [file] the file is contained "extra" toByteArray.
     *@param [objectClass] class extended from [Parcelable]
     * */
    @Throws
    fun <T : Parcelable> extractObject(file: File, objectClass: Class<T>): T

    /**
     * Return file stream with "extra" toByteArray plus 4 toByteArray which are metadata
     *
     *@param [file] the file is contained "extra" toByteArray.
     *
     * */
    fun extractStreamBytes(file: File): FileInputStream
}