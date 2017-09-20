package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";
    private static final String EXTRA_KEY_FOR_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_KEY_FOR_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;
    private boolean wasAnswerShown;

    private TextView mAnswerTextView;
    private TextView mAPILevelTextView;
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
                Log.d(TAG, "Answer Shown");
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

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }

            }
        });

        mAPILevelTextView = (TextView) findViewById(R.id.api_level_textview);
        mAPILevelTextView.setText(getResources().getString(R.string.api_level,android.os.Build.VERSION.SDK_INT));
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
