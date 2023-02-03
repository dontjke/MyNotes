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
import com.example.mynotes.data.NotesRepository;


public class NotesFragment extends Fragment {


    static final String SELECTED_INDEX = "index";
    int selectedIndex = 0;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_INDEX, selectedIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    // Этот метод вызывается, когда макет экрана создан и готов к отображению информации. Создаем список с заметками.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, 0);
        }
        //инициализируем список с заметками
        initNotes(view.findViewById(R.id.data_container));

        if (isLandscape()) {
            showLandNoteDetails(selectedIndex);
        }
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    private void initNotes(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        // В этом цикле создаём элемент TextView, заполняем его значениями,и добавляем на экран.
        for (int i = 0; i < NotesRepository.getInstance().getNotes().length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(NotesRepository.getInstance().getNotes()[i].getTitle());
            textView.setTextSize(24);
            linearLayout.addView(textView);

            final int index = i;
            textView.setOnClickListener(view1 -> showNoteDetails(index));
        }
    }

    //определяем в какой ориентации находится смартфон
    private void showNoteDetails(int index) {
        selectedIndex = index;
        if (isLandscape()) {
            showLandNoteDetails(index);
        } else {
            showPortNoteDetails(index);
        }
    }

    private void showPortNoteDetails(int index) {
        NoteFragment noteFragment = NoteFragment.newInstance(index);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        // добавляем фрагмент через add
        fragmentTransaction.add(R.id.notes_container, noteFragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); //анимация
        fragmentTransaction.commit();
    }

    private void showLandNoteDetails(int index) {
        NoteFragment noteFragment = NoteFragment.newInstance(index);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // добавляем фрагмент через add
        fragmentTransaction.replace(R.id.note_container, noteFragment);//замена фрагмента и без добавления в бэкстэк
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); //анимация
        fragmentTransaction.commit();
    }
}