package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";
    private static final String EXTRA_KEY_FOR_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_KEY_FOR_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;
    private boolean wasAnswerShown;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_KEY_FOR_ANSWER_IS_TRUE, true);

        wasAnswerShown = false;

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        if (savedInstanceState != null) {
            wasAnswerShown = savedInstanceState.getBoolean(EXTRA_KEY_FOR_ANSWER_SHOWN);
            if (wasAnswerShown) {
                Log.d(TAG,"Answer Shown");
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(getResources().getString(R.string.true_button));
                } else {
                    mAnswerTextView.setText(getResources().getString(R.string.false_button));
                }
                setAnswerAndShowResult(true);
            }
        }

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(getResources().getString(R.string.true_button));
                } else {
                    mAnswerTextView.setText(getResources().getString(R.string.false_button));
                }
                setAnswerAndShowResult(true);
                wasAnswerShown = true;
            }
        });
    }

    public static Intent newIntent(Context context, boolean answerIsTrue) {
        Intent i = new Intent(context, CheatActivity.class);
        i.putExtra(EXTRA_KEY_FOR_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    private void setAnswerAndShowResult(boolean wasAnswerShown) {
        Intent i = new Intent();
        i.putExtra(EXTRA_KEY_FOR_ANSWER_SHOWN, wasAnswerShown);
        setResult(RESULT_OK, i);
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_KEY_FOR_ANSWER_SHOWN, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(EXTRA_KEY_FOR_ANSWER_SHOWN, wasAnswerShown);

    }
}
