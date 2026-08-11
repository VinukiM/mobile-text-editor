package com.example.moderntexteditor

import android.content.Context
import java.io.File
import java.nio.charset.Charset

class FileManager(private val context: Context) {

    fun createFile(fileName: String): File {
        val file = File(context.filesDir, fileName)

        if (!file.exists()) {
            file.createNewFile()
        }

        return file
    }

    fun saveFile(
        fileName: String,
        content: String,
        charset: Charset = Charsets.UTF_8
    ): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            file.writeText(content, charset)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun readFile(
        fileName: String,
        charset: Charset = Charsets.UTF_8
    ): String {
        return try {
            val file = File(context.filesDir, fileName)

            if (file.exists()) {
                file.readText(charset)
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    fun deleteFile(fileName: String): Boolean {
        return try {
            val file = File(context.filesDir, fileName)

            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun renameFile(
        oldFileName: String,
        newFileName: String
    ): Boolean {
        return try {
            val oldFile = File(context.filesDir, oldFileName)
            val newFile = File(context.filesDir, newFileName)

            if (!oldFile.exists()) {
                return false
            }

            if (newFile.exists()) {
                return false
            }

            oldFile.renameTo(newFile)
        } catch (e: Exception) {
            false
        }
    }

    fun saveAsFile(
        newFileName: String,
        content: String,
        charset: Charset = Charsets.UTF_8
    ): Boolean {
        return saveFile(
            fileName = newFileName,
            content = content,
            charset = charset
        )
    }

    fun getSavedFiles(): List<String> {
        return try {
            context.filesDir
                .listFiles()
                ?.filter { it.isFile }
                ?.map { it.name }
                ?.sorted()
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fileExists(fileName: String): Boolean {
        return File(
            context.filesDir,
            fileName
        ).exists()
    }
}