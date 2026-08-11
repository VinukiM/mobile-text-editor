package com.example.moderntexteditor.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Undo
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.nio.charset.Charset

// ==========================================================
// KOTLIN SYNTAX HIGHLIGHTING
// ==========================================================

private fun highlightKotlin(source: String): AnnotatedString {

    val keywords = setOf(
        "package",
        "import",
        "class",
        "object",
        "interface",
        "fun",
        "val",
        "var",
        "if",
        "else",
        "when",
        "for",
        "while",
        "do",
        "return",
        "break",
        "continue",
        "in",
        "is",
        "as",
        "this",
        "super",
        "null",
        "true",
        "false",
        "private",
        "public",
        "protected",
        "internal",
        "open",
        "override",
        "abstract",
        "final",
        "data",
        "sealed",
        "enum",
        "companion",
        "const",
        "lateinit",
        "suspend",
        "try",
        "catch",
        "finally",
        "throw",
        "init",
        "constructor",
        "by",
        "get",
        "set"
    )

    val types = setOf(
        "String",
        "Int",
        "Long",
        "Float",
        "Double",
        "Boolean",
        "Char",
        "Byte",
        "Short",
        "Unit",
        "Any",
        "Nothing",
        "List",
        "MutableList",
        "Array",
        "Map",
        "MutableMap",
        "Set",
        "MutableSet"
    )

    return buildAnnotatedString {

        var i = 0

        while (i < source.length) {

            // ------------------------------------------
            // SINGLE LINE COMMENT
            // ------------------------------------------

            if (
                i + 1 < source.length &&
                source[i] == '/' &&
                source[i + 1] == '/'
            ) {

                val endIndex = source.indexOf('\n', i)

                val end =
                    if (endIndex == -1) {
                        source.length
                    } else {
                        endIndex
                    }

                withStyle(
                    SpanStyle(
                        color = Color(0xFF6A9955)
                    )
                ) {
                    append(
                        source.substring(i, end)
                    )
                }

                i = end
                continue
            }

            // ------------------------------------------
            // MULTI LINE COMMENT
            // ------------------------------------------

            if (
                i + 1 < source.length &&
                source[i] == '/' &&
                source[i + 1] == '*'
            ) {

                val endIndex =
                    source.indexOf(
                        "*/",
                        i + 2
                    )

                val end =
                    if (endIndex == -1) {
                        source.length
                    } else {
                        endIndex + 2
                    }

                withStyle(
                    SpanStyle(
                        color = Color(0xFF6A9955)
                    )
                ) {
                    append(
                        source.substring(i, end)
                    )
                }

                i = end
                continue
            }

            // ------------------------------------------
            // STRING
            // ------------------------------------------

            if (source[i] == '"') {

                var end = i + 1

                while (end < source.length) {

                    if (
                        source[end] == '"' &&
                        source[end - 1] != '\\'
                    ) {
                        end++
                        break
                    }

                    end++
                }

                withStyle(
                    SpanStyle(
                        color = Color(0xFFCE9178)
                    )
                ) {
                    append(
                        source.substring(i, end)
                    )
                }

                i = end
                continue
            }

            // ------------------------------------------
            // CHARACTER
            // ------------------------------------------

            if (
                source[i] == '\'' &&
                i + 1 < source.length
            ) {

                var end = i + 1

                while (end < source.length) {

                    if (
                        source[end] == '\'' &&
                        source[end - 1] != '\\'
                    ) {
                        end++
                        break
                    }

                    end++
                }

                withStyle(
                    SpanStyle(
                        color = Color(0xFFCE9178)
                    )
                ) {
                    append(
                        source.substring(i, end)
                    )
                }

                i = end
                continue
            }

            // ------------------------------------------
            // NUMBER
            // ------------------------------------------

            if (source[i].isDigit()) {

                var end = i

                while (
                    end < source.length &&
                    (
                            source[end].isDigit() ||
                                    source[end] == '.'
                            )
                ) {
                    end++
                }

                withStyle(
                    SpanStyle(
                        color = Color(0xFFB5CEA8)
                    )
                ) {
                    append(
                        source.substring(i, end)
                    )
                }

                i = end
                continue
            }

            // ------------------------------------------
            // WORD
            // ------------------------------------------

            if (
                source[i].isLetter() ||
                source[i] == '_'
            ) {

                var end = i

                while (
                    end < source.length &&
                    (
                            source[end].isLetterOrDigit() ||
                                    source[end] == '_'
                            )
                ) {
                    end++
                }

                val word =
                    source.substring(i, end)

                when {

                    word in keywords -> {

                        withStyle(
                            SpanStyle(
                                color = Color(0xFFC586C0)
                            )
                        ) {
                            append(word)
                        }
                    }

                    word in types -> {

                        withStyle(
                            SpanStyle(
                                color = Color(0xFF4EC9B0)
                            )
                        ) {
                            append(word)
                        }
                    }

                    else -> {
                        append(word)
                    }
                }

                i = end
                continue
            }

            append(source[i])
            i++
        }
    }
}

// ==========================================================
// MARKDOWN SYNTAX HIGHLIGHTING
// ==========================================================

private fun highlightMarkdown(
    source: String
): AnnotatedString {

    return buildAnnotatedString {

        val lines =
            source.split("\n")

        lines.forEachIndexed { index, line ->

            when {

                line.startsWith("#") -> {

                    withStyle(
                        SpanStyle(
                            color = Color(0xFFFFC857)
                        )
                    ) {
                        append(line)
                    }
                }

                line.startsWith("- ") ||
                        line.startsWith("* ") -> {

                    withStyle(
                        SpanStyle(
                            color = Color(0xFF34D399)
                        )
                    ) {
                        append(line)
                    }
                }

                line.startsWith(">") -> {

                    withStyle(
                        SpanStyle(
                            color = Color(0xFFA78BFA)
                        )
                    ) {
                        append(line)
                    }
                }

                line.startsWith("```") -> {

                    withStyle(
                        SpanStyle(
                            color = Color(0xFF60A5FA)
                        )
                    ) {
                        append(line)
                    }
                }

                else -> {
                    append(line)
                }
            }

            if (index < lines.lastIndex) {
                append("\n")
            }
        }
    }
}

// ==========================================================
// SYNTAX HIGHLIGHT TRANSFORMATION
// ==========================================================

private class SyntaxHighlightTransformation(
    private val isKotlin: Boolean,
    private val isMarkdown: Boolean
) : VisualTransformation {

    override fun filter(
        text: AnnotatedString
    ): TransformedText {

        val highlighted =
            when {

                isKotlin ->
                    highlightKotlin(text.text)

                isMarkdown ->
                    highlightMarkdown(text.text)

                else ->
                    AnnotatedString(text.text)
            }

        return TransformedText(
            text = highlighted,
            offsetMapping = OffsetMapping.Identity
        )
    }
}

// ==========================================================
// EDITOR SCREEN
// ==========================================================

@Composable
fun EditorScreen(
    fileName: String,
    initialText: String = "",
    onBack: () -> Unit
) {

    val context =
        LocalContext.current

    // ======================================================
    // TEXT
    // ======================================================

    var textFieldValue by remember(fileName, initialText) {

        mutableStateOf(
            TextFieldValue(
                text = initialText
            )
        )
    }

    val editorText =
        textFieldValue.text

    // ======================================================
    // SAVE MESSAGE
    // ======================================================

    var savedMessage by remember {
        mutableStateOf("")
    }

    // ======================================================
    // SEARCH
    // ======================================================

    var showSearch by remember {
        mutableStateOf(false)
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var replaceText by remember {
        mutableStateOf("")
    }

    var searchMessage by remember {
        mutableStateOf("")
    }

    // ======================================================
    // ENCODING
    // ======================================================

    var selectedEncoding by remember {
        mutableStateOf("UTF-8")
    }

    var showEncodingMenu by remember {
        mutableStateOf(false)
    }

    // ======================================================
    // SAVE AS
    // ======================================================

    var showSaveAsDialog by remember {
        mutableStateOf(false)
    }

    var newFileName by remember {
        mutableStateOf("")
    }

    var selectedFileType by remember {

        mutableStateOf(
            when {

                fileName.endsWith(
                    ".kt",
                    ignoreCase = true
                ) ->
                    "Kotlin (.kt)"

                fileName.endsWith(
                    ".md",
                    ignoreCase = true
                ) ->
                    "Markdown (.md)"

                else ->
                    "Text (.txt)"
            }
        )
    }

    var showFileTypeMenu by remember {
        mutableStateOf(false)
    }

    // ======================================================
    // UNDO / REDO
    // ======================================================

    val undoStack =
        remember {
            mutableStateListOf<String>()
        }

    val redoStack =
        remember {
            mutableStateListOf<String>()
        }

    // ======================================================
    // FILE TYPE
    // ======================================================

    val isKotlin =
        fileName.endsWith(
            ".kt",
            ignoreCase = true
        )

    val isMarkdown =
        fileName.endsWith(
            ".md",
            ignoreCase = true
        )

    // ======================================================
    // SAVE DIRECTLY TO INTERNAL STORAGE
    // ======================================================

    fun saveCurrentFile(
        name: String
    ) {

        try {

            val file =
                File(
                    context.filesDir,
                    name
                )

            file.writeText(
                editorText,
                getSelectedCharset(selectedEncoding)
            )

            savedMessage =
                "Saved using $selectedEncoding"

        } catch (e: Exception) {

            savedMessage =
                "Error saving file"
        }
    }

    // ======================================================
    // SEARCH
    // ======================================================

    fun findNext() {

        if (searchText.isBlank()) {

            searchMessage =
                "Type something to search"

            return
        }

        val startPosition =
            textFieldValue.selection.end

        val firstSearch =
            editorText.indexOf(
                searchText,
                startIndex = startPosition
            )

        val index =
            if (firstSearch == -1) {

                editorText.indexOf(
                    searchText
                )

            } else {

                firstSearch
            }

        if (index >= 0) {

            textFieldValue =
                textFieldValue.copy(
                    selection =
                        TextRange(
                            index,
                            index + searchText.length
                        )
                )

            searchMessage =
                "Match found"

        } else {

            searchMessage =
                "No match found"
        }
    }

    // ======================================================
    // REPLACE FIRST
    // ======================================================

    fun replaceFirst() {

        if (searchText.isBlank()) {

            searchMessage =
                "Type something to search"

            return
        }

        val index =
            editorText.indexOf(
                searchText
            )

        if (index >= 0) {

            undoStack.add(editorText)
            redoStack.clear()

            val newText =
                editorText.replaceRange(
                    index,
                    index + searchText.length,
                    replaceText
                )

            textFieldValue =
                TextFieldValue(
                    text = newText
                )

            savedMessage = ""

            searchMessage =
                "Replaced"

        } else {

            searchMessage =
                "No match found"
        }
    }

    // ======================================================
    // REPLACE ALL
    // ======================================================

    fun replaceAll() {

        if (searchText.isBlank()) {

            searchMessage =
                "Type something to search"

            return
        }

        if (!editorText.contains(searchText)) {

            searchMessage =
                "No matches found"

            return
        }

        undoStack.add(editorText)
        redoStack.clear()

        val newText =
            editorText.replace(
                searchText,
                replaceText
            )

        textFieldValue =
            TextFieldValue(
                text = newText
            )

        savedMessage = ""

        searchMessage =
            "All matches replaced"
    }

    // ======================================================
    // UNDO
    // ======================================================

    fun undo() {

        if (undoStack.isNotEmpty()) {

            redoStack.add(editorText)

            val previous =
                undoStack.removeAt(
                    undoStack.lastIndex
                )

            textFieldValue =
                TextFieldValue(
                    text = previous
                )

            savedMessage = ""
        }
    }

    // ======================================================
    // REDO
    // ======================================================

    fun redo() {

        if (redoStack.isNotEmpty()) {

            undoStack.add(editorText)

            val next =
                redoStack.removeAt(
                    redoStack.lastIndex
                )

            textFieldValue =
                TextFieldValue(
                    text = next
                )

            savedMessage = ""
        }
    }

    // ======================================================
    // MAIN SCREEN
    // ======================================================

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFF111827)
                )
    ) {

        // ==================================================
        // TOP BAR
        // ==================================================

        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(0xFF1F2937)
                )
        ) {

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onBack
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.ArrowBack,

                        contentDescription =
                            "Back",

                        tint =
                            Color.White
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text = fileName,

                    color =
                        Color.White,

                    fontSize =
                        18.sp,

                    fontFamily =
                        FontFamily.Monospace
                )

                Spacer(
                    modifier =
                        Modifier
                            .width(8.dp)
                )

                IconButton(
                    onClick = {
                        saveCurrentFile(fileName)
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Save,

                        contentDescription =
                            "Save",

                        tint =
                            Color.Cyan
                    )
                }

                Button(
                    onClick = {

                        newFileName =
                            fileName

                        showSaveAsDialog =
                            true
                    }
                ) {

                    Text("Save As")
                }
            }
        }

        // ==================================================
        // SAVE MESSAGE
        // ==================================================

        if (savedMessage.isNotEmpty()) {

            Text(
                text =
                    savedMessage,

                color =
                    Color(0xFF4ADE80),

                fontSize =
                    13.sp,

                modifier =
                    Modifier.padding(
                        start = 20.dp,
                        bottom = 4.dp
                    )
            )
        }

        // ==================================================
        // EDITOR AREA
        // ==================================================

        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(12.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(0xFF0B1220)
                )
        ) {

            Row(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(12.dp),

                verticalAlignment =
                    Alignment.Top
            ) {

                // ------------------------------------------
                // LINE NUMBERS
                // ------------------------------------------

                Column(
                    modifier =
                        Modifier.width(38.dp),

                    horizontalAlignment =
                        Alignment.End
                ) {

                    val lineCount =
                        maxOf(
                            1,
                            editorText.count {
                                it == '\n'
                            } + 1
                        )

                    for (
                    number in
                    1..lineCount
                    ) {

                        Text(
                            text =
                                number.toString(),

                            color =
                                Color(0xFF64748B),

                            fontFamily =
                                FontFamily.Monospace,

                            fontSize =
                                14.sp,

                            modifier =
                                Modifier.padding(
                                    end = 8.dp
                                )
                        )
                    }
                }

                // ------------------------------------------
                // EDITOR
                // ------------------------------------------

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                start = 8.dp
                            )
                ) {

                    BasicTextField(

                        value =
                            textFieldValue,

                        onValueChange = { newValue ->

                            if (
                                newValue.text !=
                                editorText
                            ) {

                                undoStack.add(
                                    editorText
                                )

                                redoStack.clear()

                                savedMessage = ""
                            }

                            textFieldValue =
                                newValue
                        },

                        modifier =
                            Modifier.fillMaxSize(),

                        textStyle =
                            TextStyle(
                                color =
                                    Color.White,

                                fontSize =
                                    15.sp,

                                fontFamily =
                                    FontFamily.Monospace,

                                lineHeight =
                                    22.sp
                            ),

                        cursorBrush =
                            SolidColor(
                                Color.Cyan
                            ),

                        visualTransformation =
                            SyntaxHighlightTransformation(
                                isKotlin =
                                    isKotlin,

                                isMarkdown =
                                    isMarkdown
                            ),

                        singleLine =
                            false,

                        decorationBox = { innerTextField ->

                            Box(
                                modifier =
                                    Modifier.fillMaxSize()
                            ) {

                                if (
                                    editorText.isEmpty()
                                ) {

                                    Text(
                                        text =
                                            "Type anything...",

                                        color =
                                            Color(0xFF64748B),

                                        fontFamily =
                                            FontFamily.Monospace,

                                        fontSize =
                                            15.sp
                                    )
                                }

                                innerTextField()
                            }
                        }
                    )
                }
            }
        }

        // ==================================================
        // BOTTOM TOOLBAR
        // ==================================================

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .padding(
                        horizontal = 10.dp
                    ),

            horizontalArrangement =
                Arrangement.SpaceEvenly,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // UNDO ARROW

            IconButton(
                onClick = {
                    undo()
                }
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Undo,

                    contentDescription =
                        "Undo",

                    tint =
                        if (undoStack.isNotEmpty()) {
                            Color.Cyan
                        } else {
                            Color.DarkGray
                        }
                )
            }

            // REDO ARROW

            IconButton(
                onClick = {
                    redo()
                }
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Redo,

                    contentDescription =
                        "Redo",

                    tint =
                        if (redoStack.isNotEmpty()) {
                            Color.Cyan
                        } else {
                            Color.DarkGray
                        }
                )
            }

            // SEARCH

            IconButton(
                onClick = {
                    showSearch = true
                }
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Search,

                    contentDescription =
                        "Search",

                    tint =
                        Color.Cyan
                )
            }

            // ENCODING

            Button(
                onClick = {
                    showEncodingMenu = true
                }
            ) {

                Text(
                    selectedEncoding,
                    fontSize = 11.sp
                )
            }

            // SAVE

            IconButton(
                onClick = {
                    saveCurrentFile(fileName)
                }
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Save,

                    contentDescription =
                        "Save",

                    tint =
                        Color.Cyan
                )
            }
        }
    }

    // ======================================================
    // ENCODING MENU
    // ======================================================

    DropdownMenu(
        expanded =
            showEncodingMenu,

        onDismissRequest = {
            showEncodingMenu = false
        }
    ) {

        DropdownMenuItem(
            text = {
                Text("UTF-8")
            },

            onClick = {

                selectedEncoding =
                    "UTF-8"

                showEncodingMenu =
                    false
            }
        )

        DropdownMenuItem(
            text = {
                Text("UTF-16")
            },

            onClick = {

                selectedEncoding =
                    "UTF-16"

                showEncodingMenu =
                    false
            }
        )

        DropdownMenuItem(
            text = {
                Text("ISO-8859-1")
            },

            onClick = {

                selectedEncoding =
                    "ISO-8859-1"

                showEncodingMenu =
                    false
            }
        )
    }

    // ======================================================
    // SEARCH DIALOG
    // ======================================================

    if (showSearch) {

        AlertDialog(

            onDismissRequest = {
                showSearch = false
            },

            title = {
                Text("Search & Replace")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value =
                            searchText,

                        onValueChange = {
                            searchText = it
                            searchMessage = ""
                        },

                        label = {
                            Text("Search")
                        },

                        singleLine = true,

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value =
                            replaceText,

                        onValueChange = {
                            replaceText = it
                            searchMessage = ""
                        },

                        label = {
                            Text("Replace with")
                        },

                        singleLine = true,

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceEvenly
                    ) {

                        Button(
                            onClick = {
                                findNext()
                            }
                        ) {
                            Text("Find")
                        }

                        Button(
                            onClick = {
                                replaceFirst()
                            }
                        ) {
                            Text("Replace")
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Button(
                        onClick = {
                            replaceAll()
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {
                        Text("Replace All")
                    }

                    if (
                        searchMessage.isNotEmpty()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                searchMessage,

                            color =
                                Color.Cyan
                        )
                    }
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        showSearch = false
                    }
                ) {

                    Text("Close")
                }
            }
        )
    }

    // ======================================================
    // SAVE AS DIALOG
    // ======================================================

    if (showSaveAsDialog) {

        AlertDialog(

            onDismissRequest = {
                showSaveAsDialog = false
            },

            title = {
                Text("Save As")
            },

            text = {

                Column {

                    OutlinedTextField(

                        value =
                            newFileName,

                        onValueChange = {
                            newFileName = it
                        },

                        label = {
                            Text("File name")
                        },

                        singleLine = true,

                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

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
                            selectedFileType
                        )
                    }

                    DropdownMenu(

                        expanded =
                            showFileTypeMenu,

                        onDismissRequest = {
                            showFileTypeMenu = false
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

            confirmButton = {

                TextButton(

                    onClick = {

                        if (
                            newFileName.isNotBlank()
                        ) {

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

                            val trimmed =
                                newFileName.trim()

                            val cleanName =
                                trimmed
                                    .substringBeforeLast(
                                        ".",
                                        trimmed
                                    )

                            val finalName =
                                cleanName +
                                        extension

                            try {

                                val file =
                                    File(
                                        context.filesDir,
                                        finalName
                                    )

                                file.writeText(
                                    editorText,
                                    getSelectedCharset(
                                        selectedEncoding
                                    )
                                )

                                savedMessage =
                                    "Saved as $finalName"

                                showSaveAsDialog =
                                    false

                            } catch (
                                e: Exception
                            ) {

                                savedMessage =
                                    "Error saving file"
                            }
                        }
                    }
                ) {

                    Text("Save")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showSaveAsDialog = false
                    }
                ) {

                    Text("Cancel")
                }
            }
        )
    }
}

// ==========================================================
// ENCODING FUNCTION
// ==========================================================

private fun getSelectedCharset(
    encoding: String
): Charset {

    return when (encoding) {

        "UTF-16" ->
            Charsets.UTF_16

        "ISO-8859-1" ->
            Charsets.ISO_8859_1

        else ->
            Charsets.UTF_8
    }
}