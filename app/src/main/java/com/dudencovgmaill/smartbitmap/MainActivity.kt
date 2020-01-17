package com.dudencovgmaill.smartbitmap

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.dudencovgmaill.smartbitmaplib.BitmapController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val bmpController: BitmapController = BitmapController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        run()
    }

    private fun run() {

        val data = String(CharArray(2) { 'w' })

        //val bmp1 = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val bmp1 = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565)
        val bmp2: Bitmap? = bmpController.insert(bmp1, Model(data))

        iv?.setImageBitmap(bmp2)


        val act: Model? = bmpController.extractObject(bmp2!!, Model::class.java)
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
