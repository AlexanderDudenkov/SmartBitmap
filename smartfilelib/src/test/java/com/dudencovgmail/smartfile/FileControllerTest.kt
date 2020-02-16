package com.dudencovgmail.smartfile

import com.dudencovgmail.smartfilelib.FileController
import com.dudencovgmail.smartfilelib.IFileController
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.rules.TemporaryFolder
import java.io.File

class FileControllerTest {

    @get: Rule
    val folder = TemporaryFolder()

    var subject: IFileController = FileController()

    @Test
    fun `testing resulting file name`() {

        val input1 = folder.newFile("file")
        val input2 = ByteArray(1) { 1 }

        val exp = input1.absolutePath + "_modified"
        val act = subject.insert(input1, input2).absolutePath.substringBeforeLast('_')

        assertEquals(exp, act)
    }

    @Test
    fun `testing that creating new file`() {

        val file = folder.newFile("file")
        val bytes = ByteArray(1)

        val newFile = subject.insert(file, bytes)

        assertTrue(file.exists())
        assertTrue(newFile.exists())
    }

    @Test
    fun `testing different byte count`() {

        val input1 = folder.newFile("file")
        val input2 = ByteArray(400_000_000)

        val exp = input2
        val act = subject.let {
            it.extractBytes(it.insert(input1, input2))
        }

        assertArrayEquals(exp, act)
    }

    @Test
    fun `testing data transferring 1`() {

        val input1 = folder.newFile("file")
        val input2 = ByteArray(2_140_000_000)

        val exp: Long = 2_140_000_008
        val act: Long =
            subject.let { it.extractStreamBytes(it.insert(input1, input2)) }.channel.size()

        assertEquals(exp, act)
    }

    @Test
    fun `testing data transferring 2`() {

        val input1 = folder.newFile("file")
        val input2 = "data".toByteArray()

        val exp = input2
        val act = subject.let { it.extractBytes(it.insert(input1, input2)) }

        assertArrayEquals(exp, act)
    }

    @Test
    fun `exception will be thrown if the file wasn't created by this library`() {

        val input: File = folder.newFile("file")
        input.writeBytes(ByteArray(8) { 1 })
        val exp = "this file wasn't created by this library!"
        val act = assertThrows<Exception> { subject.extractBytes(input) }.message

        assertEquals(exp, act)
    }
}