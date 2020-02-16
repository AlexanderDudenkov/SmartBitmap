package com.dudencovgmaill.smartbitmap

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dudencovgmaill.smartbitmaplib.FileController
import com.dudencovgmaill.smartbitmaplib.IFileController
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private val controller: IFileController = FileController()
    private val thread = Thread {
        test()
        Thread.sleep(1000)
        test2()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission(arrayOf(WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION) { thread.start() }
    }

    fun test() {

        val file =
            File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/20200205_144442.jpg")

        val data = ByteArray(100) { 100 }
        var act: ByteArray? = null

        try {
            controller.run {

                val res = insert(file, data)
                act = extractBytes(res)
            }
        } catch (e: Exception) {
            tv?.post {Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()}
        }

        tv?.post { tv.text = if (data.contentEquals(act ?: ByteArray(0))) "the same" else "not the same" }
    }

    fun test2() {

        val file =
            File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/20200205_144442.jpg")

        val data = "data"
        var act: Model? = null

        try {
            controller.run {
                val res = insert(file, Model(data))
                act = extractObject(res, Model::class.java)
            }
        } catch (e: Exception) {
            tv?.post {Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()}
        }

        tv?.post { tv.text = "exp:$data; act:${act?.data}" }
    }

    private fun checkPermission(
        permissions: Array<String>,
        @IntRange(from = 0) requestCode: Int,
        granted: () -> Unit
    ) {
        permissions.forEach {

            if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED) {
                granted()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(it), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    thread.start()
                } else {
                    finish()
                }
                return
            }
        }
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

    companion object {
        private const val REQUEST_CODE_PERMISSION: Int = 1
    }
}
