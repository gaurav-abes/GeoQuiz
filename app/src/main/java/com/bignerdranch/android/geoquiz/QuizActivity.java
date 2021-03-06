package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_SCORE = "score";
    private static final String KEY_IS_CHEATER = "cheater";
    private static final String KEY_FOR_CHEAT_TOKENS = "cheat_tokens";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private TextView mCheatTokenTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_sachin, true),
            new Question(R.string.question_food, true),
            new Question(R.string.question_studies, false),
            new Question(R.string.question_television, false),
            new Question(R.string.question_work, true)
    };

    Set<Integer> mAnsweredQuestionsSet = new HashSet<>();

    private int mCurrentIndex = 0;
    private int mScore = 0;
    private boolean mIsCheater;
    private int mCheatTokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Log.d(TAG, "onCreate() called");

        mCheatTokens = 3;

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mScore = savedInstanceState.getInt(KEY_SCORE, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
            mCheatTokens = savedInstanceState.getInt(KEY_FOR_CHEAT_TOKENS);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mCheatTokenTextView = (TextView) findViewById(R.id.cheat_token_textview);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCheater = false;
                mCurrentIndex = (mCurrentIndex + 1);
                if (mCurrentIndex == mQuestionBank.length)
                    mCurrentIndex--;
                updateQuestion();
            }
        });

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = mCurrentIndex - 1;
                if (mCurrentIndex < 0)
                    mCurrentIndex = 0;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex].isAnswerTrue());
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCheater = false;
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                if (mCurrentIndex == mQuestionBank.length)
                    mCurrentIndex--;
                updateQuestion();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null)
                return;
            mIsCheater = CheatActivity.wasAnswerShown(data);
            if (mIsCheater) {
                mCheatTokens--;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        mCheatTokenTextView.setText(getResources().getString(R.string.cheat_tokens, mCheatTokens));
        if (mCheatTokens <= 0)
            mCheatButton.setClickable(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState(Bundle) called");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(KEY_SCORE, mScore);
        outState.putBoolean(KEY_IS_CHEATER, mIsCheater);
        outState.putInt(KEY_FOR_CHEAT_TOKENS, mCheatTokens);
    }

    private void updateQuestion() {
        int questionID = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(questionID);
        enableAnswerButtons();
    }

    private void disableAnswerButtons() {
        mTrueButton.setClickable(false);
        mFalseButton.setClickable(false);
    }

    private void enableAnswerButtons() {
        mTrueButton.setClickable(true);
        mFalseButton.setClickable(true);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageId;

        if (mIsCheater) {
            messageId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageId = R.string.correct_toast;
                mScore++;
            } else
                messageId = R.string.incorrect_toast;
        }

        Toast.makeText(QuizActivity.this, messageId, Toast.LENGTH_SHORT).show();
        if (!mAnsweredQuestionsSet.contains(mCurrentIndex))
            mAnsweredQuestionsSet.add(mCurrentIndex);
        if (mAnsweredQuestionsSet.size() == mQuestionBank.length) {
            Toast.makeText(QuizActivity.this, getResources().getString(R.string.total_score, mScore), Toast.LENGTH_SHORT).show();
            disableAllUserInteractions();
        }
        disableAnswerButtons();
    }

    private void disableAllUserInteractions() {
        mNextButton.setClickable(false);
        mPreviousButton.setClickable(false);
        mQuestionTextView.setClickable(false);
    }
}
