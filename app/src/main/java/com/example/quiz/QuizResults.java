package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class QuizResults extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

         final AppCompatButton startNewbtn = findViewById(R.id.btnStartNewQuiz);
         final TextView correctAnswer = findViewById(R.id.correctAnswers);
        final TextView incorrectAnswer = findViewById(R.id.incorrectAnswers);
        final int getCorrectAnswers = getIntent().getIntExtra(getString(R.string.correct),0);
        final int getInCorrectAnswers = getIntent().getIntExtra(getString(R.string.incorrect),0);
        correctAnswer.setText(getString(R.string.Correct) + getCorrectAnswers);
        incorrectAnswer.setText(getString(R.string.Incorrect)+ getInCorrectAnswers);

        startNewbtn.setOnClickListener(view -> {
            startActivity(new Intent(QuizResults.this, HomeActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuizResults.this, HomeActivity.class));
        finish();
    }
}