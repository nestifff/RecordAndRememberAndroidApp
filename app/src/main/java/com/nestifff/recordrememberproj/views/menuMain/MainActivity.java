package com.nestifff.recordrememberproj.views.menuMain;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nestifff.recordrememberproj.R;
import com.nestifff.recordrememberproj.views.learnWords.ChooseSetToLearnActivity;
import com.nestifff.recordrememberproj.views.viewWords.WordsListActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.recallWordsButton).setOnClickListener(v -> {
            Intent intent = ChooseSetToLearnActivity.newIntent(MainActivity.this, false);
            startActivity(intent);
        });

        findViewById(R.id.learnWordsButton).setOnClickListener(v -> {
            Intent intent = ChooseSetToLearnActivity.newIntent(MainActivity.this, true);
            startActivity(intent);
        });

        findViewById(R.id.viewMyWordsButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, WordsListActivity.class);
            startActivity(intent);
        });

    }
}

