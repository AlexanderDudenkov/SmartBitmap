package com.dudencovgmaill.smartbitmaplib

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import androidx.core.graphics.createBitmap
import com.dudencovgmaill.smartbitmaplib.util.*
import io.mockk.*
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.nio.ByteBuffer
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.safeCast
import kotlin.reflect.jvm.isAccessible

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BitmapControllerTests2 {

    private var subject: BitmapController? = null

    @BeforeAll
    fun setUp() {
        //mockkStatic("com.dudencovgmaill.smartbitmaplib.util.bmp_ext")
        subject = BitmapController()
    }

    @AfterAll
    fun tearDown() {
        subject = null
    }

    /*@Test
    fun `insert bytes into bmp`() {
        *//*val act:subject!!::class.memberFunctions.find { it.name == "insert" && !it.isAccessible }?.run {
            isAccessible = true
            this.call()
        }*//*


        val exp: Bitmap = mockk<Bitmap>().apply {
            every { width } returns (1)
            every { height } returns (2)
            every { config } returns (Bitmap.Config.RGB_565)
            every { byteCount } returns (3)
            every { copyPixelsFromBuffer(ByteBuffer.allocate(2)) } just Runs
        }

        val bmpInput: Bitmap = mockk<Bitmap>().apply {
            every { width } returns (1)
            every { height } returns (1)
            every { config } returns (Bitmap.Config.RGB_565)
            every { byteCount } returns (2)
            every { copyPixelsToBuffer(ByteBuffer.allocate(2)) } just Runs
        }

        every { Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565) } returns exp
        //every { bmpInput.toByteArray() } returns (ByteArray(2))
        //every { ByteBuffer.allocate(2).toBitmap(1, 1, Bitmap.Config.RGB_565) } returns (exp)

        val bytesInput: ByteArray = ByteArray(1) { 1 }

        val act: Bitmap? = subject?.insert(bmpInput, bytesInput)

        assertEquals(exp.width, act?.width)
        assertEquals(exp.height, act?.height)
        assertEquals(exp.config, act?.config)
        //assertEquals(exp.toByteArray(), act?.toByteArray())
        assertEquals(exp.byteCount, act?.byteCount)
    }*/

    @Test
    fun testingBmpStatic() {

        val exp: Int = 1

        var bmp: Bitmap

        val input: Bitmap = mockk<Bitmap>().apply {
            every { width } returns (1)
            every { height } returns (2)
            every { config } returns (Bitmap.Config.RGB_565)
            every { byteCount } returns (3)
        }

        every { Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565) } returns input

        bmp = Bitmap.createBitmap(exp, 1, Bitmap.Config.RGB_565)

        val act: Int = bmp.width

        assertEquals(exp, act)
    }

    /*@Test
    fun testingMockExt() {

        val exp: ByteArray = ByteArray(1) { 1 }

        every { 1.bytes() } returns exp

        val act: ByteArray = 1.bytes()

        assertEquals(exp, act)
    }*/

    @Test
    fun testingMockStaticPlusExt() {

        val exp: Bitmap = mockk<Bitmap>().apply {
            every { width } returns (1)
            every { height } returns (1)
            every { config } returns (Bitmap.Config.RGB_565)
            every { copyPixelsFromBuffer(ByteBuffer.allocate(2)) } just Runs
        }

        every { Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565) } returns exp

        val act: Bitmap? = ByteBuffer.allocate(2).toBitmap(1, 1, Bitmap.Config.RGB_565)

        assertEquals(exp.width, act?.width)
    }

    /*@Test
    fun testingMockStaticPlusExt() {

        val exp: Bitmap = mockk<Bitmap>().apply {
            every { width } returns (1)
            every { height } returns (1)
            every { config } returns (Bitmap.Config.RGB_565)
            every { byteCount } returns (2)
            every { copyPixelsFromBuffer(ByteBuffer.wrap(ByteArray(1) { 1 })) } just Runs
        }

        every { Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565) } returns exp

        val act: Bitmap? = ByteBuffer.wrap(ByteArray(1) { 1 }).toBitmap(1, 1, Bitmap.Config.RGB_565)

        assertEquals(exp.width, act?.width)
    }*/

    @Test
    fun bmpToByteArrayTest() {

        val exp: Int = 1009

        val bmpInput: Bitmap = mockk<Bitmap>().apply {
            every { byteCount } returns (exp)
            every { copyPixelsToBuffer(ByteBuffer.allocate(exp)) } just Runs
        }

        val act: Int? = bmpInput.toByteArray()?.size

        assertEquals(exp, act)
    }

    @Test
    fun fff() {

        val exp = "test"

        val act: String? =
            subject!!::class.memberFunctions.find {
                it.name == "insertTest" && !it.isAccessible
            }?.apply {
                isAccessible = true
            }?.call(subject, exp, { v: String -> v }) as? String

        assertEquals(exp, act)
    }

    @Test
    fun `insert bytes into bmp`() {

        val exp = ByteArray(1) { 1 }

        val act: ByteArray? = subject!!::class.memberFunctions.find {
            it.name == "insert" && !it.isAccessible
        }
            ?.apply {
                isAccessible = true
            }?.call(subject, exp, { v: String -> v }) as? ByteArray?

        assertEquals(exp, act)
    }

    @Test
    fun bb() {

        val exp = 100

        val o: Any? =
            subject!!::class.java.declaredClasses.find { it.simpleName == "BitmapDataTest" }
                ?.declaredConstructors?.find { it.parameterCount == 1 }?.let {
                it.isAccessible = true
                it.newInstance(exp)
            }

        val act: Int? = subject!!::class.memberFunctions.find {
            it.name == "test" && !it.isAccessible
        }?.apply {
            isAccessible = true
        }?.call(subject, o) as? Int

        assertEquals(exp, act)
    }

    @Test
    fun bb2() {

        val exp = 100

        val o: Any? =
            subject!!::class.java.declaredClasses.find { it.simpleName == "BitmapDataTest" }
                ?.declaredConstructors?.find { it.parameterCount == 1 }?.let {
                it.isAccessible = true
                it.newInstance(exp - 1)
            }

        val act: Int? = subject!!::class.java.declaredMethods.find {
            it.name == "test2" && !it.isAccessible
        }?.apply {
            isAccessible = true
        }?.invoke(subject, o)?.let { obj ->
            obj::class.java.declaredFields.find { it.name == "data" }?.apply { isAccessible = true }
                ?.get(obj)
        } as? Int

        assertEquals(exp, act)
    }

    @Test
    fun bb3() {

        val exp = 100

        val o: Any? =
            subject!!::class.java.declaredClasses.find { it.simpleName == "BitmapDataTest" }
                ?.declaredConstructors?.find { it.parameterCount == 1 }?.let {
                it.isAccessible = true
                it.newInstance(exp - 1)
            }

        val act: Int? = subject?.call("test2", o, parameterCount = 1)?.field("data") as? Int

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
}