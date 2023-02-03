package com.example.mynotes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mynotes.R;
import com.example.mynotes.data.NotesRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotesRepository.getInstance().init();

        if (savedInstanceState == null) getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.notes_container,new NotesFragment())
                .commit();
    }
}