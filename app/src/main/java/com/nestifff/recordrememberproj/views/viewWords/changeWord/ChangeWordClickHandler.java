package com.nestifff.recordrememberproj.views.viewWords.changeWord;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nestifff.recordrememberproj.R;
import com.nestifff.recordrememberproj.model.dao.SetWordsInProcess;
import com.nestifff.recordrememberproj.model.word.Word;
import com.nestifff.recordrememberproj.model.word.WordInProcess;

public class ChangeWordClickHandler implements View.OnClickListener {

    private EditText rusWordEditText;
    private EditText engWordEditText;
    private LinearLayout linearLayout;

    private Word word;

    private Context context;

    TextView rusWordTextView;
    TextView engWordTextView;

    Button acceptButton;

    @Override
    public void onClick(View v) {

        try {

            if (!(word instanceof WordInProcess)) {
                return;
            }
            linearLayout = v.findViewById(R.id.ll_changeWord);

            if (linearLayout.getVisibility() == View.VISIBLE) {
                linearLayout.setVisibility(View.GONE);
                return;
            } else if (linearLayout.getVisibility() != View.GONE) {
                return;
            }

            linearLayout.setVisibility(View.VISIBLE);

            acceptButton = v.findViewById(R.id.button_changeWord);
            acceptButton.setEnabled(false);

            rusWordEditText = v.findViewById(R.id.editText_changeRusWord);
            engWordEditText = v.findViewById(R.id.editText_changeEngWord);

            rusWordTextView = v.findViewById(R.id.textView_viewRusWord);
            engWordTextView = v.findViewById(R.id.textView_viewEngWord);

            rusWordEditText.setText(word.rus);
            engWordEditText.setText(word.eng);
            rusWordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            engWordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

            engWordEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    acceptButton.setEnabled(true);
                }
            });
            rusWordEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    acceptButton.setEnabled(true);
                }
            });

            acceptButton.setOnClickListener(v1 -> {
                changeWord();

                rusWordTextView.setText(word.rus);
                engWordTextView.setText(word.eng);

                linearLayout.setVisibility(View.GONE);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void changeWord() {

        if (!rusWordEditText.getText().toString().equals(word.rus) ||
                !engWordEditText.getText().toString().equals(word.eng)) {

            word.eng = engWordEditText.getText().toString();
            word.rus = rusWordEditText.getText().toString();

            SetWordsInProcess.get(context).updateWord((WordInProcess) word);
        }
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
