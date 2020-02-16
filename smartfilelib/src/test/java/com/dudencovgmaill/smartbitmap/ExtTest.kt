package com.dudencovgmaill.smartbitmap

import com.dudencovgmaill.smartbitmaplib.util.read
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.rules.TemporaryFolder
import java.nio.ByteBuffer

class ExtTest {

    @get: Rule
    val folder = TemporaryFolder()

    @Test
    fun `read file test`() {

        val exp = ByteArray(4) { -128 }

        val file = folder.newFile("file")

        file.apply { writeBytes(ByteArray(100) { 127 }) }

        val input1 = file.apply { writeBytes(ByteArray(4) { -128 }) }
        val input2 = ByteBuffer.allocate(exp.size)

        input1.read(input2)

        val act = input2.array()

        assertTrue(exp.contentEquals(act))
        assertFalse(exp.contentEquals(act.apply { set(0, 127) }))
    }

    @Test
    fun `read file test2`() {

        val exp = ByteArray(2) { 127 }

        val file = folder.newFile("file")

        val input1 = file.apply {
            writeBytes(ByteArray(4).also {
                it[0] = 0
                it[1] = 127
                it[2] = 127
                it[3] = 0
            })
        }
        val input2 = ByteBuffer.allocate(exp.size)

        input1.read(input2, 1)

        val act = input2.array()

        assertTrue(exp.contentEquals(act))
        assertFalse(exp.contentEquals(act.apply { set(1, 0) }))
    }

    @Test
    fun `read file test3`() {

        val exp = ByteArray(1_000_000_000) { 1 }

        val file = folder.newFile("file")

        val input1 = file.apply { writeBytes(exp) }
        val input2 = ByteBuffer.allocate(exp.size)

        input1.read(input2)

        val act = input2.array()

        assertTrue(exp.contentEquals(act))
        assertFalse(exp.contentEquals(act.apply { set(1, 0) }))
    }
}