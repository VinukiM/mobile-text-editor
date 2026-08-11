 package com.example.moderntexteditor.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

@Composable
fun HomeScreen(
    recentFiles: List<String>,
    onNewFileClick: (String) -> Unit,
    onOpenFileClick: () -> Unit,
    onRecentFileClick: (String) -> Unit,
    onFileDeleted: () -> Unit
) {

    // ======================================================
    // CONTEXT
    // ======================================================

    val context = androidx.compose.ui.platform.LocalContext.current

    // ======================================================
    // RECENT FILE MANAGER
    // ======================================================

    val recentFilesManager = remember {
        RecentFilesManager(context)
    }

    // ======================================================
    // NEW FILE DIALOG
    // ======================================================

    var showNewFileDialog by remember {
        mutableStateOf(false)
    }

    var fileName by remember {
        mutableStateOf("")
    }

    var selectedFileType by remember {
        mutableStateOf("Markdown (.md)")
    }

    var showFileTypeMenu by remember {
        mutableStateOf(false)
    }

    // ======================================================
    // DELETE
    // ======================================================

    var fileToDelete by remember {
        mutableStateOf<String?>(null)
    }

    // ======================================================
    // RENAME
    // ======================================================

    var fileToRename by remember {
        mutableStateOf<String?>(null)
    }

    var renameFileName by remember {
        mutableStateOf("")
    }

    var renameError by remember {
        mutableStateOf("")
    }

    // ======================================================
    // MAIN SCREEN
    // ======================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111827))
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        // ==================================================
        // LOGO
        // ==================================================

        Text(
            text = "📝",
            fontSize = 55.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // ==================================================
        // TITLE
        // ==================================================

        Text(
            text = "Modern Text Editor",
            color = Color.White,
            fontSize = 28.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Kotlin & Markdown Editor",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // ==================================================
        // NEW FILE BUTTON
        // ==================================================

        Button(
            onClick = {

                fileName = ""

                selectedFileType =
                    "Markdown (.md)"

                showNewFileDialog = true
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {

            Text(
                text = "New File",
                fontSize = 18.sp
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // ==================================================
        // OPEN FILE BUTTON
        // ==================================================

        Button(
            onClick = onOpenFileClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {

            Text(
                text = "Open File",
                fontSize = 18.sp
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        // ==================================================
        // RECENT FILES TITLE
        // ==================================================

        Text(
            text = "Recent Files",
            color = Color.Cyan,
            fontSize = 20.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // ==================================================
        // RECENT FILES
        // ==================================================

        if (recentFiles.isEmpty()) {

            Text(
                text = "No recent files",
                color = Color.Gray
            )

        } else {

            recentFiles.forEach { file ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color(0xFF1F2937)
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        // ==================================
                        // FILE NAME
                        // ==================================

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onRecentFileClick(file)
                                }
                                .padding(5.dp),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text = "📄",
                                fontSize = 22.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(10.dp)
                            )

                            Column {

                                Text(
                                    text = file,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )

                                Text(
                                    text =
                                        when (
                                            file
                                                .substringAfterLast(
                                                    ".",
                                                    ""
                                                )
                                                .lowercase()
                                        ) {

                                            "kt" ->
                                                "Kotlin file"

                                            "md" ->
                                                "Markdown file"

                                            "txt" ->
                                                "Text file"

                                            else ->
                                                "File"
                                        },

                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // ==================================
                        // RENAME BUTTON
                        // ==================================

                        IconButton(
                            onClick = {

                                fileToRename = file

                                renameFileName =
                                    file.substringBeforeLast(
                                        ".",
                                        file
                                    )

                                renameError = ""
                            }
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Edit,

                                contentDescription =
                                    "Rename",

                                tint =
                                    Color.Cyan
                            )
                        }

                        // ==================================
                        // DELETE BUTTON
                        // ==================================

                        IconButton(
                            onClick = {

                                fileToDelete = file
                            }
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.Delete,

                                contentDescription =
                                    "Delete",

                                tint =
                                    Color.Red
                            )
                        }
                    }
                }
            }
        }
    }

    // ======================================================
    // CREATE NEW FILE DIALOG
    // ======================================================

    if (showNewFileDialog) {

        AlertDialog(

            onDismissRequest = {

                showNewFileDialog = false
            },

            title = {

                Text(
                    "Create New File"
                )
            },

            text = {

                Column {

                    // ======================================
                    // FILE NAME
                    // ======================================

                    OutlinedTextField(

                        value = fileName,

                        onValueChange = {
                            fileName = it
                        },

                        label = {
                            Text("File name")
                        },

                        placeholder = {
                            Text("Example: MyNotes")
                        },

                        singleLine = true,

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    // ======================================
                    // FILE TYPE
                    // ======================================

                    Text(
                        text = "File type",
                        color = Color.Gray
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Button(

                        onClick = {

                            showFileTypeMenu = true
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text =
                                selectedFileType
                        )
                    }

                    // ======================================
                    // FILE TYPE MENU
                    // ======================================

                    DropdownMenu(

                        expanded =
                            showFileTypeMenu,

                        onDismissRequest = {

                            showFileTypeMenu =
                                false
                        }
                    ) {

                        DropdownMenuItem(

                            text = {
                                Text("Kotlin (.kt)")
                            },

                            onClick = {

                                selectedFileType =
                                    "Kotlin (.kt)"

                                showFileTypeMenu =
                                    false
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text("Markdown (.md)")
                            },

                            onClick = {

                                selectedFileType =
                                    "Markdown (.md)"

                                showFileTypeMenu =
                                    false
                            }
                        )

                        DropdownMenuItem(

                            text = {
                                Text("Text (.txt)")
                            },

                            onClick = {

                                selectedFileType =
                                    "Text (.txt)"

                                showFileTypeMenu =
                                    false
                            }
                        )
                    }
                }
            },

            // ==========================================
            // CREATE
            // ==========================================

            confirmButton = {

                TextButton(

                    onClick = {

                        if (fileName.isNotBlank()) {

                            val extension =
                                when (
                                    selectedFileType
                                ) {

                                    "Kotlin (.kt)" ->
                                        ".kt"

                                    "Markdown (.md)" ->
                                        ".md"

                                    else ->
                                        ".txt"
                                }

                            val trimmedName =
                                fileName.trim()

                            val cleanName =
                                trimmedName
                                    .substringBeforeLast(
                                        ".",
                                        trimmedName
                                    )

                            val finalFileName =
                                cleanName + extension

                            // ----------------------------------
                            // CREATE FILE DIRECTLY
                            // ----------------------------------

                            val file =
                                File(
                                    context.filesDir,
                                    finalFileName
                                )

                            if (!file.exists()) {

                                file.createNewFile()
                            }

                            // ----------------------------------
                            // ADD TO RECENT FILES
                            // ----------------------------------

                            recentFilesManager.addRecentFile(
                                finalFileName
                            )

                            showNewFileDialog =
                                false

                            onNewFileClick(
                                finalFileName
                            )
                        }
                    }
                ) {

                    Text(
                        "Create"
                    )
                }
            },

            // ==========================================
            // CANCEL
            // ==========================================

            dismissButton = {

                TextButton(

                    onClick = {

                        showNewFileDialog =
                            false
                    }
                ) {

                    Text(
                        "Cancel"
                    )
                }
            }
        )
    }

    // ======================================================
    // DELETE FILE DIALOG
    // ======================================================

    if (fileToDelete != null) {

        AlertDialog(

            onDismissRequest = {

                fileToDelete = null
            },

            title = {

                Text(
                    "Delete File?"
                )
            },

            text = {

                Text(
                    "Are you sure you want to delete \"$fileToDelete\"?"
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        val fileNameToDelete =
                            fileToDelete

                        if (
                            fileNameToDelete != null
                        ) {

                            // ----------------------------------
                            // DELETE ACTUAL FILE
                            // ----------------------------------

                            val file =
                                File(
                                    context.filesDir,
                                    fileNameToDelete
                                )

                            if (file.exists()) {

                                file.delete()
                            }

                            // ----------------------------------
                            // REMOVE FROM RECENT FILES
                            // ----------------------------------

                            recentFilesManager
                                .removeRecentFile(
                                    fileNameToDelete
                                )

                            // ----------------------------------
                            // REFRESH HOME SCREEN
                            // ----------------------------------

                            onFileDeleted()
                        }

                        fileToDelete = null
                    }
                ) {

                    Text(
                        "Delete",
                        color = Color.Red
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        fileToDelete = null
                    }
                ) {

                    Text(
                        "Cancel"
                    )
                }
            }
        )
    }

    // ======================================================
    // RENAME FILE DIALOG
    // ======================================================

    if (fileToRename != null) {

        AlertDialog(

            onDismissRequest = {

                fileToRename = null
                renameError = ""
            },

            title = {

                Text(
                    "Rename File"
                )
            },

            text = {

                Column {

                    // ======================================
                    // NEW NAME
                    // ======================================

                    OutlinedTextField(

                        value =
                            renameFileName,

                        onValueChange = {

                            renameFileName = it
                            renameError = ""
                        },

                        label = {

                            Text(
                                "New file name"
                            )
                        },

                        singleLine = true,

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    // ======================================
                    // ERROR MESSAGE
                    // ======================================

                    if (
                        renameError.isNotEmpty()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                renameError,

                            color =
                                Color.Red,

                            fontSize =
                                13.sp
                        )
                    }
                }
            },

            // ==========================================
            // RENAME
            // ==========================================

            confirmButton = {

                TextButton(

                    onClick = {

                        val oldName =
                            fileToRename

                        if (
                            oldName != null &&
                            renameFileName.isNotBlank()
                        ) {

                            // ----------------------------------
                            // KEEP ORIGINAL EXTENSION
                            // ----------------------------------

                            val extension =
                                oldName.substringAfterLast(
                                    ".",
                                    ""
                                )

                            val trimmedName =
                                renameFileName.trim()

                            val cleanName =
                                trimmedName
                                    .substringBeforeLast(
                                        ".",
                                        trimmedName
                                    )

                            val newName =
                                if (
                                    extension.isNotEmpty()
                                ) {

                                    "$cleanName.$extension"

                                } else {

                                    cleanName
                                }

                            // ----------------------------------
                            // SAME NAME
                            // ----------------------------------

                            if (
                                newName == oldName
                            ) {

                                renameError =
                                    "Enter a different name."

                            } else {

                                // ------------------------------
                                // OLD FILE
                                // ------------------------------

                                val oldFile =
                                    File(
                                        context.filesDir,
                                        oldName
                                    )

                                // ------------------------------
                                // NEW FILE
                                // ------------------------------

                                val newFile =
                                    File(
                                        context.filesDir,
                                        newName
                                    )

                                // ------------------------------
                                // CHECK EXISTING FILE
                                // ------------------------------

                                if (
                                    newFile.exists()
                                ) {

                                    renameError =
                                        "A file with this name already exists."

                                } else if (
                                    !oldFile.exists()
                                ) {

                                    renameError =
                                        "The original file does not exist."

                                } else {

                                    // --------------------------
                                    // RENAME ACTUAL FILE
                                    // --------------------------

                                    val renamed =
                                        oldFile.renameTo(
                                            newFile
                                        )

                                    if (renamed) {

                                        // ----------------------
                                        // UPDATE RECENT FILES
                                        // ----------------------

                                        recentFilesManager
                                            .removeRecentFile(
                                                oldName
                                            )

                                        recentFilesManager
                                            .addRecentFile(
                                                newName
                                            )

                                        // ----------------------
                                        // REFRESH
                                        // ----------------------

                                        onFileDeleted()

                                        fileToRename = null
                                        renameError = ""

                                    } else {

                                        renameError =
                                            "Unable to rename the file."
                                    }
                                }
                            }

                        } else {

                            renameError =
                                "Please enter a file name."
                        }
                    }
                ) {

                    Text(
                        "Rename"
                    )
                }
            },

            // ==========================================
            // CANCEL
            // ==========================================

            dismissButton = {

                TextButton(

                    onClick = {

                        fileToRename = null
                        renameError = ""
                    }
                ) {

                    Text(
                        "Cancel"
                    )
                }
            }
        )
    }
}
