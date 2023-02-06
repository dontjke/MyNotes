package com.example.mynotes.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mynotes.R;
import com.example.mynotes.domain.Note;


public class NotesFragment extends Fragment {

    static final String SELECTED_INDEX = "index";
    static final String SELECTED_NOTE = "note";
    int selectedIndex = 0;
    private Note note;
    public NotesFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SELECTED_NOTE, note);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            // selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, 0);
            note = savedInstanceState.getParcelable(SELECTED_NOTE);
        }
        //инициализируем список с заметками
        initNotes(view.findViewById(R.id.data_container));
        if (isLandscape()) {
            showLandNoteDetails(note);
        }
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initNotes(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        // В этом цикле создаём элемент TextView, заполняем его значениями,и добавляем на экран.
        for (int i = 0; i < Note.getNotes().size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(Note.getNotes().get(i).getTitle());
            textView.setTextSize(24);
            linearLayout.addView(textView);
            final int index = i;
            textView.setOnClickListener(view1 -> showNoteDetails(Note.getNotes().get(index)));
        }
    }

    private void showNoteDetails(Note note) {
        //selectedIndex = index;
        this.note = note;
        if (isLandscape()) {
            showLandNoteDetails(note);
        } else {
            showPortNoteDetails(note);
        }
    }

    private void showPortNoteDetails(Note note) {
        NoteFragment noteFragment = NoteFragment.newInstance(note);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        // добавляем фрагмент через add
        fragmentTransaction.add(R.id.notes_container, noteFragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); //анимация
        fragmentTransaction.commit();
    }

    private void showLandNoteDetails(Note note) {
        NoteFragment noteFragment = NoteFragment.newInstance(note);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // добавляем фрагмент через add
        fragmentTransaction.replace(R.id.note_container, noteFragment);//замена фрагмента и без добавления в бэкстэк
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); //анимация
        fragmentTransaction.commit();

    }

}