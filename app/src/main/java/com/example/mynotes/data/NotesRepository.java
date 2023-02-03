package com.example.mynotes.data;

import android.annotation.SuppressLint;

import com.example.mynotes.domain.Note;

import java.time.LocalDateTime;
import java.util.Random;

public class NotesRepository {

    private static NotesRepository notesRepository;
    private final Random random = new Random();
    private Note[] notes;  //коллекция заметок

    public static NotesRepository getInstance() {
        if (notesRepository == null) {
            notesRepository = new NotesRepository();
        }
        return notesRepository;
    }

    public Note[] getNotes() {
        return notes;
    }

    public void init() {

        //формируем список заметок из 10 элементов
        notes = new Note[10];
        for (int i = 0; i < notes.length; i++) {
            notes[i] = createNote(i);
        }
    }

    @SuppressLint("DefaultLocale")
    private Note createNote(int i) {
        String title = String.format("Note %d", i);
        String description = String.format("Note description %d", i);
        LocalDateTime creationDate = LocalDateTime.now().plusDays(-random.nextInt(5));
        return new Note(title, description, creationDate);
    }

}
