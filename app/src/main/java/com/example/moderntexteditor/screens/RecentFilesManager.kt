 package com.example.moderntexteditor.screens

import android.content.Context
import org.json.JSONArray
import java.io.File
import java.nio.charset.Charset

class RecentFilesManager(
    private val context: Context
) {

    companion object {

        private const val PREFS_NAME =
            "recent_files_preferences"

        private const val KEY_RECENT_FILES =
            "recent_files"

        private const val MAX_RECENT_FILES = 10
    }

    private val preferences =
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

    // ==========================================================
    // CREATE FILE
    // ==========================================================

    fun createFile(
        fileName: String
    ): File {

        val file =
            File(
                context.filesDir,
                fileName
            )

        if (!file.exists()) {
            file.createNewFile()
        }

        return file
    }

    // ==========================================================
    // SAVE FILE
    // ==========================================================

    fun saveFile(
        fileName: String,
        content: String,
        charset: Charset = Charsets.UTF_8
    ) {

        val file =
            File(
                context.filesDir,
                fileName
            )

        file.writeText(
            content,
            charset
        )

        addRecentFile(fileName)
    }

    // ==========================================================
    // SAVE AS
    // ==========================================================

    fun saveAsFile(
        oldFileName: String,
        newFileName: String,
        content: String,
        charset: Charset = Charsets.UTF_8
    ): Boolean {

        val newFile =
            File(
                context.filesDir,
                newFileName
            )

        if (
            newFile.exists() &&
            oldFileName != newFileName
        ) {
            return false
        }

        newFile.writeText(
            content,
            charset
        )

        if (oldFileName != newFileName) {

            removeRecentFile(
                oldFileName
            )
        }

        addRecentFile(
            newFileName
        )

        return true
    }

    // ==========================================================
    // READ FILE
    // ==========================================================

    fun readFile(
        fileName: String,
        charset: Charset = Charsets.UTF_8
    ): String {

        val file =
            File(
                context.filesDir,
                fileName
            )

        return if (file.exists()) {

            file.readText(
                charset
            )

        } else {

            ""
        }
    }

    // ==========================================================
    // GET ALL SAVED FILES
    // ==========================================================

    fun getSavedFiles(): List<String> {

        return context.filesDir
            .listFiles()
            ?.filter { file ->
                file.isFile
            }
            ?.map { file ->
                file.name
            }
            ?.sorted()
            ?: emptyList()
    }

    // ==========================================================
    // DELETE FILE
    // ==========================================================

    fun deleteFile(
        fileName: String
    ): Boolean {

        val file =
            File(
                context.filesDir,
                fileName
            )

        val deleted =
            if (file.exists()) {
                file.delete()
            } else {
                false
            }

        if (deleted) {

            removeRecentFile(
                fileName
            )
        }

        return deleted
    }

    // ==========================================================
    // RENAME FILE
    // ==========================================================

    fun renameFile(
        oldFileName: String,
        newFileName: String
    ): Boolean {

        val oldFile =
            File(
                context.filesDir,
                oldFileName
            )

        val newFile =
            File(
                context.filesDir,
                newFileName
            )

        if (!oldFile.exists()) {
            return false
        }

        if (newFile.exists()) {
            return false
        }

        val renamed =
            oldFile.renameTo(
                newFile
            )

        if (renamed) {

            removeRecentFile(
                oldFileName
            )

            addRecentFile(
                newFileName
            )
        }

        return renamed
    }

    // ==========================================================
    // GET RECENT FILES
    // ==========================================================

    fun getRecentFiles(): List<String> {

        val json =
            preferences.getString(
                KEY_RECENT_FILES,
                "[]"
            ) ?: "[]"

        return try {

            val jsonArray =
                JSONArray(json)

            val files =
                mutableListOf<String>()

            for (
            i in 0 until jsonArray.length()
            ) {

                val fileName =
                    jsonArray.getString(i)

                val file =
                    File(
                        context.filesDir,
                        fileName
                    )

                if (file.exists()) {

                    files.add(
                        fileName
                    )
                }
            }

            files

        } catch (
            e: Exception
        ) {

            emptyList()
        }
    }

    // ==========================================================
    // ADD RECENT FILE
    // ==========================================================

    fun addRecentFile(
        fileName: String
    ) {

        if (fileName.isBlank()) {
            return
        }

        val files =
            getRecentFiles()
                .toMutableList()

        files.remove(
            fileName
        )

        files.add(
            0,
            fileName
        )

        while (
            files.size >
            MAX_RECENT_FILES
        ) {

            files.removeAt(
                files.lastIndex
            )
        }

        saveRecentFiles(
            files
        )
    }

    // ==========================================================
    // REMOVE RECENT FILE
    // ==========================================================

    fun removeRecentFile(
        fileName: String
    ) {

        val files =
            getRecentFiles()
                .toMutableList()

        files.remove(
            fileName
        )

        saveRecentFiles(
            files
        )
    }

    // ==========================================================
    // CLEAR RECENT FILES
    // ==========================================================

    fun clearRecentFiles() {

        preferences
            .edit()
            .remove(
                KEY_RECENT_FILES
            )
            .apply()
    }

    // ==========================================================
    // SAVE RECENT FILE LIST
    // ==========================================================

    private fun saveRecentFiles(
        files: List<String>
    ) {

        val jsonArray =
            JSONArray()

        files.forEach { file ->

            jsonArray.put(
                file
            )
        }

        preferences
            .edit()
            .putString(
                KEY_RECENT_FILES,
                jsonArray.toString()
            )
            .apply()
    }
}
