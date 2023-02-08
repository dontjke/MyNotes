package com.example.mynotes.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotes.R;
import com.example.mynotes.domain.Note;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    static final String ARG_INDEX = "index";
    static final String SELECTED_NOTE = "note";
    private Note note;
    private Toast deleteToast;


    public NoteFragment() {

    }

    public static NoteFragment newInstance(Note note) {
        // Создание фрагмента
        NoteFragment fragment = new NoteFragment();
        // Передача параметра через бандл
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.note_menu, menu);


        //Меню от Активити
        MenuItem menuItemAbout = menu.findItem(R.id.about_action);
        if (menuItemAbout != null) {
            menuItemAbout.setVisible(false);
        }
    }

    private Toast prepareCustomToast(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.my_toast_layout,
                (ViewGroup) view.findViewById(R.id.toast_layout_root));

        ImageView imageView = layout.findViewById(R.id.toast_image_view);
        imageView.setImageResource(android.R.drawable.ic_input_delete);

        TextView textView = layout.findViewById(R.id.toast_text_view);
        textView.setText("Заметка удалена!");

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        return toast;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_action) {
            Note.getNotes().remove(note);
            note = null;
            updateData();
            if (!isLandscape()) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
            //Toast.makeText(getContext(), "Заметка удалена", Toast.LENGTH_SHORT).show();
            deleteToast.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateData() {
        for (Fragment fragment : requireActivity().getSupportFragmentManager().getFragments()) {
            if (fragment instanceof NotesFragment) {
                ((NotesFragment) fragment).initNotes();
                break;
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();

        deleteToast = prepareCustomToast(view);

        Button buttonBack = view.findViewById(R.id.back_button);
        if (buttonBack != null)
            buttonBack.setOnClickListener(view1 ->
                    requireActivity()
                            .getSupportFragmentManager()
                            .popBackStack());
        if (arguments != null) {
            //int index = arguments.getInt(ARG_INDEX);
            note = arguments.getParcelable(SELECTED_NOTE);
            TextView textViewTitle = view.findViewById(R.id.title_text_view);
            if (note == null) return;
            textViewTitle.setText(note.getTitle());
            textViewTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    note.setTitle(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            TextView textViewDescription = view.findViewById(R.id.description_text_view);
            textViewDescription.setText(note.getDescription());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SELECTED_NOTE, note);
        super.onSaveInstanceState(outState);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}