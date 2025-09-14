package com.aarti.ai

import android.content.Context
import java.io.File

object MarkdownNoteManager {
    private fun notesDir(context: Context): File {
        val dir = File(context.filesDir, "markdown_notes")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun saveNote(context: Context, fileName: String, content: String) {
        val file = File(notesDir(context), "$fileName.md")
        file.writeText(content)
    }

    fun loadNote(context: Context, fileName: String): String? {
        val file = File(notesDir(context), "$fileName.md")
        return if (file.exists()) file.readText() else null
    }

    fun deleteNote(context: Context, fileName: String) {
        val file = File(notesDir(context), "$fileName.md")
        if (file.exists()) file.delete()
    }

    fun listNotes(context: Context): List<String> {
        return notesDir(context).listFiles()?.map { it.nameWithoutExtension } ?: emptyList()
    }
}
