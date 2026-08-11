package com.example.moderntexteditor

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.moderntexteditor.screens.EditorScreen
import com.example.moderntexteditor.screens.HomeScreen
import com.example.moderntexteditor.screens.RecentFilesManager
import com.example.moderntexteditor.ui.theme.ModernTextEditorTheme
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ModernTextEditorTheme {

                // ==========================================
                // RECENT FILE MANAGER
                // ==========================================

                val recentFilesManager = remember {
                    RecentFilesManager(this@MainActivity)
                }

                // ==========================================
                // SCREEN STATE
                // ==========================================

                var showEditor by remember {
                    mutableStateOf(false)
                }

                // ==========================================
                // CURRENT FILE
                // ==========================================

                var currentFileName by remember {
                    mutableStateOf("Untitled.txt")
                }

                var currentFileContent by remember {
                    mutableStateOf("")
                }

                // ==========================================
                // RECENT FILES
                // ==========================================

                var recentFiles by remember {
                    mutableStateOf(
                        recentFilesManager.getRecentFiles()
                    )
                }

                // ==========================================
                // OPEN FILE PICKER
                // ==========================================

                val filePicker =
                    rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.OpenDocument()
                    ) { uri: Uri? ->

                        if (uri != null) {

                            try {

                                // --------------------------------
                                // GET FILE NAME
                                // --------------------------------

                                var fileName = "OpenedFile.txt"

                                val cursor =
                                    contentResolver.query(
                                        uri,
                                        null,
                                        null,
                                        null,
                                        null
                                    )

                                cursor?.use { c ->

                                    val nameIndex =
                                        c.getColumnIndex(
                                            OpenableColumns.DISPLAY_NAME
                                        )

                                    if (
                                        c.moveToFirst() &&
                                        nameIndex >= 0
                                    ) {

                                        fileName =
                                            c.getString(
                                                nameIndex
                                            )
                                    }
                                }

                                // --------------------------------
                                // READ FILE CONTENT
                                // --------------------------------

                                val content =
                                    contentResolver
                                        .openInputStream(uri)
                                        ?.bufferedReader()
                                        ?.use { reader ->
                                            reader.readText()
                                        }
                                        ?: ""

                                // --------------------------------
                                // STORE CURRENT FILE
                                // --------------------------------

                                currentFileName = fileName
                                currentFileContent = content

                                // --------------------------------
                                // ADD TO RECENT FILES
                                // --------------------------------

                                recentFilesManager.addRecentFile(
                                    fileName
                                )

                                recentFiles =
                                    recentFilesManager.getRecentFiles()

                                // --------------------------------
                                // OPEN EDITOR
                                // --------------------------------

                                showEditor = true

                            } catch (e: Exception) {

                                currentFileName =
                                    "OpenedFile.txt"

                                currentFileContent = ""

                                showEditor = true
                            }
                        }
                    }

                // ==========================================
                // NAVIGATION
                // ==========================================

                if (showEditor) {

                    // ======================================
                    // EDITOR SCREEN
                    // ======================================

                    EditorScreen(
                        fileName = currentFileName,
                        initialText = currentFileContent,

                        onBack = {

                            recentFiles =
                                recentFilesManager.getRecentFiles()

                            showEditor = false
                        }
                    )

                } else {

                    // ======================================
                    // HOME SCREEN
                    // ======================================

                    HomeScreen(

                        recentFiles = recentFiles,

                        // ==================================
                        // NEW FILE
                        // ==================================

                        onNewFileClick = { fileName: String ->

                            currentFileName =
                                fileName

                            currentFileContent =
                                ""

                            recentFilesManager.addRecentFile(
                                fileName
                            )

                            recentFiles =
                                recentFilesManager.getRecentFiles()

                            showEditor = true
                        },

                        // ==================================
                        // OPEN FILE
                        // ==================================

                        onOpenFileClick = {

                            filePicker.launch(
                                arrayOf(
                                    "text/*",
                                    "application/octet-stream",
                                    "*/*"
                                )
                            )
                        },

                        // ==================================
                        // OPEN RECENT FILE
                        // ==================================

                        onRecentFileClick = {
                                fileName: String ->

                            val file =
                                File(
                                    filesDir,
                                    fileName
                                )

                            if (file.exists()) {

                                currentFileName =
                                    fileName

                                currentFileContent =
                                    file.readText()

                                recentFilesManager.addRecentFile(
                                    fileName
                                )

                                recentFiles =
                                    recentFilesManager.getRecentFiles()

                                showEditor = true

                            } else {

                                // If the file doesn't
                                // exist anymore, remove
                                // it from recent files.

                                recentFilesManager.removeRecentFile(
                                    fileName
                                )

                                recentFiles =
                                    recentFilesManager.getRecentFiles()
                            }
                        },

                        // ==================================
                        // FILE DELETED / RENAMED
                        // ==================================

                        onFileDeleted = {

                            recentFiles =
                                recentFilesManager.getRecentFiles()
                        }
                    )
                }
            }
        }
    }
}