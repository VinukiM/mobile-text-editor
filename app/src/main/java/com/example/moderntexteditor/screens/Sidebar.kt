package com.example.moderntexteditor.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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

@Composable
fun Sidebar(
    onNewFileCreated: (String) -> Unit
) {

    var showNewFileDialog by remember {
        mutableStateOf(false)
    }

    var fileNameInput by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .width(230.dp)
            .fillMaxHeight()
            .background(Color(0xff111827))
            .padding(15.dp)
    ) {

        // ==========================================
        // HEADER
        // ==========================================

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Editor",
                color = Color.Cyan
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.LightGray
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // ==========================================
        // NEW FILE
        // ==========================================

        Button(
            onClick = {
                fileNameInput = ""
                showNewFileDialog = true
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New File"
            )

            Spacer(
                modifier = Modifier.width(5.dp)
            )

            Text(
                text = "New File"
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // ==========================================
        // RECENT FILES
        // ==========================================

        Text(
            text = "RECENT FILES",
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "📄 README.md",
            color = Color.White
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "<> Utils.kt",
            color = Color.White
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "🎨 styles.css",
            color = Color.White
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "⚙ Settings",
            color = Color.LightGray
        )
    }


    // ==============================================
    // NEW FILE DIALOG
    // ==============================================

    if (showNewFileDialog) {

        AlertDialog(

            onDismissRequest = {
                showNewFileDialog = false
            },

            title = {
                Text(
                    text = "Create New File"
                )
            },

            text = {

                OutlinedTextField(

                    value = fileNameInput,

                    onValueChange = {
                        fileNameInput = it
                    },

                    label = {
                        Text("File name")
                    },

                    placeholder = {
                        Text("Example: Notes.md")
                    },

                    singleLine = true,

                    modifier = Modifier.fillMaxWidth()
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        val newFileName =
                            fileNameInput.trim()

                        if (newFileName.isNotEmpty()) {

                            // Close dialog FIRST
                            showNewFileDialog = false

                            // Tell EditorScreen about the new file
                            onNewFileCreated(newFileName)
                        }
                    }
                ) {

                    Text(
                        text = "Create"
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        showNewFileDialog = false
                    }

                ) {

                    Text(
                        text = "Cancel"
                    )
                }
            }
        )
    }
}