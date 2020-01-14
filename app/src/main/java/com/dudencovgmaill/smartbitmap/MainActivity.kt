package com.dudencovgmaill.smartbitmap

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dudencovgmaill.smartbitmaplib.BitmapController
import com.dudencovgmaill.smartbitmaplib.IBitmapController
import com.dudencovgmaill.smartbitmaplib.Model
import com.dudencovgmaill.smartbitmaplib.util.marshall
import com.dudencovgmaill.smartbitmaplib.util.unmarshall
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val bmpController: IBitmapController =
        BitmapController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        run()
    }

    private fun run() {

        val title = String(CharArray(10000){'w'})
        val name = String(CharArray(10000){'h'})
        val age = 1

        //val bmp1 = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val bmp1 = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565)
        val bmp2 = Model(title, name, age).marshall()
            ?.let { bmpController.addBytesToEnd(bmp1, it) }

        iv1?.setImageBitmap(bmp2)
        iv2?.setImageBitmap(bmp1)

        val act = bmpController.extractBytes(bmp2!!)?.unmarshall(Model::class.java)
        tv?.text = "exp:$title, $name, $age; act:${act?.title}, ${act?.name}, ${act?.age}"
    }
}
