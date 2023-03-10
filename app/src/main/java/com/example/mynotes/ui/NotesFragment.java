package com.example.mynotes.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.mynotes.R;
import com.example.mynotes.domain.Note;
import com.google.android.material.snackbar.Snackbar;


public class NotesFragment extends Fragment {

    static final String SELECTED_INDEX = "index";
    static final String SELECTED_NOTE = "note";
    int selectedIndex = 0;
    View dataContainer;
    private Note note;

    public NotesFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        if (note == null)
            note = Note.getNotes().get(0);
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

        dataContainer = view.findViewById(R.id.data_container);
        //инициализируем список с заметками
        initNotes(view.findViewById(R.id.data_container));
        if (isLandscape()) {
            showLandNoteDetails(note);
        }
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void initNotes() {
        initNotes(dataContainer);
    }

    private void initNotes(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        linearLayout.removeAllViews();
        // В этом цикле создаём элемент TextView, заполняем его значениями,и добавляем на экран.
        for (int i = 0; i < Note.getNotes().size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(Note.getNotes().get(i).getTitle());
            textView.setTextSize(24);
            linearLayout.addView(textView);
            final int index = i;
            initPopupMenu(view, textView, index);
            textView.setOnClickListener(view1 -> showNoteDetails(Note.getNotes().get(index)));
        }
    }

    private void initPopupMenu(View rootView, TextView view, int index) {
        view.setOnLongClickListener(view1 -> {
            Activity activity = requireActivity();
            PopupMenu popupMenu = new PopupMenu(activity, view1);
            activity.getMenuInflater().inflate(R.menu.notes_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                        switch (menuItem.getItemId()) {
                            case R.id.delete_action_popup:
                                Note.getNotes().remove(index);
                                ((LinearLayout) rootView).removeView(view);
                                Snackbar.make(rootView, "Заметка удалена", Snackbar.LENGTH_LONG).show();
                                return true;
                        }
                        return true;
                    }
            );
            popupMenu.show();
            return true;
        });
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