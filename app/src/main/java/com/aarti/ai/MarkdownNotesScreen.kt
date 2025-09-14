package com.aarti.ai

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkdownNotesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var notes by remember { mutableStateOf(MarkdownNoteManager.listNotes(context)) }
    var selectedNote by remember { mutableStateOf<String?>(null) }
    var noteContent by remember { mutableStateOf("") }
    var newNoteName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Markdown Notes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.NoteAdd, contentDescription = "Add Note")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedNote == null) {
                OutlinedTextField(
                    value = newNoteName,
                    onValueChange = { newNoteName = it },
                    label = { Text("New Note Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (newNoteName.isNotBlank()) {
                            selectedNote = newNoteName
                            noteContent = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create New Note")
                }
                Divider()
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(notes) { note ->
                        MarkdownNoteRow(note, onOpen = {
                            selectedNote = note
                            noteContent = MarkdownNoteManager.loadNote(context, note) ?: ""
                        }, onDelete = {
                            MarkdownNoteManager.deleteNote(context, note)
                            notes = MarkdownNoteManager.listNotes(context)
                        })
                    }
                }
            } else {
                OutlinedTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    label = { Text("Markdown Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            selectedNote?.let {
                                MarkdownNoteManager.saveNote(context, it, noteContent)
                                notes = MarkdownNoteManager.listNotes(context)
                            }
                        }
                    ) { Text("Save") }
                    OutlinedButton(
                        onClick = { selectedNote = null }
                    ) { Text("Back to Notes") }
                }
            }
        }
    }
}

@Composable
private fun MarkdownNoteRow(note: String, onOpen: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(note, modifier = Modifier.weight(1f))
        IconButton(onClick = onOpen) {
            Icon(Icons.Default.Edit, contentDescription = "Open")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}
