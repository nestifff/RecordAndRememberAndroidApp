package com.nestifff.recordrememberproj.views.learnWords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nestifff.recordrememberproj.R;


// TODO: количество слов для изучения сделать удобнее
// TODO: например, пару кнопок для самого частого количества и кнопку чтобы набрать количество вручную

public class ChooseSetToLearnActivity extends AppCompatActivity {

    private TextView wayToLearnText;
    boolean rusEngWay = true;
    private static final String LEARN_OR_RECALL = "learnOrRecallChooseNum";
    private boolean isLearnInProcess;


    public static Intent newIntent(
            Context packageContext,
            boolean isInProcess
    ) {

        Intent intent = new Intent(packageContext, ChooseSetToLearnActivity.class);
        intent.putExtra(LEARN_OR_RECALL, isInProcess);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_set_to_learn);

        isLearnInProcess = getIntent().getBooleanExtra(LEARN_OR_RECALL, false);

        Button learnWordsInProcess_5_Button;
        Button learnWordsInProcess_10_Button;
        Button learnWordsInProcess_20_Button;
        Button learnWordsInProcess_30_Button;
        Button learnWordsInProcess_50_Button;

        Button changeWayOfLearnButton;

        learnWordsInProcess_5_Button = findViewById(R.id.learnWordsInProcess_5_Button);
        learnWordsInProcess_10_Button = findViewById(R.id.learnWordsInProcess_10_Button);
        learnWordsInProcess_20_Button = findViewById(R.id.learnWordsInProcess_20_Button);
        learnWordsInProcess_30_Button = findViewById(R.id.learnWordsInProcess_30_Button);
        learnWordsInProcess_50_Button = findViewById(R.id.learnWordsInProcess_50_Button);

        changeWayOfLearnButton = findViewById(R.id.button_changeWayToLearn);
        wayToLearnText =  findViewById(R.id.tv_wayToLearnValue);

        changeWayOfLearnButton.setOnClickListener(v -> {
            String text;
            if(rusEngWay) {
                rusEngWay = false;
                text  = getString(R.string.text_engRusWayToLearn);
            } else {
                rusEngWay = true;
                text  = getString(R.string.text_rusEngWayToLearn);
            }
            wayToLearnText.setText(text);
        });

        learnWordsInProcess_5_Button.setOnClickListener(v -> {
            Intent intent = LearnWordsActivity.newIntent(
                    ChooseSetToLearnActivity.this, rusEngWay, 5, isLearnInProcess);
            startActivity(intent);
        });

        learnWordsInProcess_10_Button.setOnClickListener(v -> {
            Intent intent = LearnWordsActivity.newIntent(
                    ChooseSetToLearnActivity.this, rusEngWay, 10, isLearnInProcess);
            startActivity(intent);
        });

        learnWordsInProcess_20_Button.setOnClickListener(v -> {
            Intent intent = LearnWordsActivity.newIntent(
                    ChooseSetToLearnActivity.this, rusEngWay, 20, isLearnInProcess);
            startActivity(intent);
        });

        learnWordsInProcess_30_Button.setOnClickListener(v -> {
            Intent intent = LearnWordsActivity.newIntent(
                    ChooseSetToLearnActivity.this, rusEngWay, 30, isLearnInProcess);
            startActivity(intent);
        });

        learnWordsInProcess_50_Button.setOnClickListener(v -> {
            Intent intent = LearnWordsActivity.newIntent(
                    ChooseSetToLearnActivity.this, rusEngWay, 50, isLearnInProcess);
            startActivity(intent);
        });
    }
}
