package com.nestifff.recordrememberproj.views.viewWords.addWord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.nestifff.recordrememberproj.R;
import com.nestifff.recordrememberproj.model.dao.SetWordsInProcess;
import com.nestifff.recordrememberproj.model.word.WordInProcess;

public class AddWordDialogFragment extends DialogFragment {

    private EditText editTextRus;
    private EditText editTextEng;

    public interface AddWordDialogListener {
        void addWordDialogOnClick(WordInProcess newWord);
    }

    AddWordDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            listener = (AddWordDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("ViewChangeWordActivity must implement NoticeDialogListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_add_word, null);
        builder.setView(v);

        editTextRus = v.findViewById(R.id.word_rus);
        editTextEng = v.findViewById(R.id.word_eng);

        builder.setTitle("Add new word to your set");
        builder.setPositiveButton("Ok", (dialog, id) -> addWordDialog());

        builder.setNegativeButton("Cancel", (dialog, id) -> {
        });

        // Create the AlertDialog object and return it
        return builder.create();

    }

    private void addWordDialog() {

        WordInProcess newWord = new WordInProcess(editTextEng.getText().toString(),
                editTextRus.getText().toString());

        SetWordsInProcess.get(getContext()).addWord(newWord);

        listener.addWordDialogOnClick(newWord);
    }
}

