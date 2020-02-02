package com.dudencovgmaill.smartbitmap

import android.os.Bundle
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.dudencovgmaill.smartbitmaplib.FileController
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private val bmpController: FileController = FileController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        test()
    }

    fun test() {

        val file =
            File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/20200123_165116.jpg")

        val data = ByteArray(100) { 100 }
        var act: ByteArray? = null

        bmpController.run {
            val res = insert(file, data)
            if (res != null) act = extractBytes(res)
        }

        tv?.text = if (data.contentEquals(act ?: ByteArray(0))) "the same" else "not the same"
    }

    fun test2() {

        val file =
            File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/20200123_165116.jpg")

        val data = "data"
        var act: Model? = null

        bmpController.run {
            val res = insert(file, Model(data))
            if (res != null) act = extractObject(res,Model::class.java)
        }

        tv?.text = "exp:$data; act:${act?.data}"
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
