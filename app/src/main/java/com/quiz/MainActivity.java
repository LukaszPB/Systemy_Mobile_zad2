package com.quiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.intellij.lang.annotations.Flow;

public class MainActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;

    private Button hintButton;
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "com.quiz.correctAnswer";

    private static final int REQUEST_CODE_PROMPT = 0;
    private TextView questionTextView;

    private Question[] questions = {
            new Question(R.string.question1,false),
            new Question(R.string.question2,true),
            new Question(R.string.question3,false),
            new Question(R.string.question4,false),
            new Question(R.string.question5,true)
    };
    private int idx = 0;
    private int score = 0;
    private boolean answered = false;

    private boolean answerWasShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TestActivity","Create");

        if(savedInstanceState != null) {
            idx = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        hintButton = findViewById(R.id.podpowiedz_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered) checkAnswer(true);
                answered = true;
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered) checkAnswer(false);
                answered = true;
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answered)
                {
                    idx = (idx +1)%questions.length;
                    answerWasShown = false;
                    setNextQuestion();
                }
                answered = false;
            }
        });
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this,PromptActivity.class);
                boolean correctAnswer = questions[idx].getTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER,correctAnswer);
                startActivityForResult(intent,REQUEST_CODE_PROMPT);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TestActivity","Start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TestActivity","Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TestActivity","Destroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TestActivity","Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TestActivity","Resume");
    }

    private void checkAnswer(boolean userAnswer)
    {
        int resultMessageId = 0;

        if(answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        }
        else {
            if (userAnswer == questions[idx].getTrueAnswer()) {
                resultMessageId = R.string.correctAnswer;
                score++;
            } else {
                resultMessageId = R.string.wrongAnswer;
            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
        if(idx == questions.length-1)
        {
            Toast.makeText(this,"Twój wynik: " + score,Toast.LENGTH_SHORT).show();
            score = 0;
        }
    }
    private void setNextQuestion()
    {
        questionTextView.setText(questions[idx].getQuestionId());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TestActivity","On save instance state");
        outState.putInt(KEY_CURRENT_INDEX,idx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode != RESULT_OK) { return; }
        if(requestCode == REQUEST_CODE_PROMPT) {
            if(data == null) { return; }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN,false);
        }
    }
}