package com.example.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {
    private TextView tvNumberofQuestions,tvQuestion;
    private AppCompatButton btnoption1, btnoption2, btnoption3, btnoption4;
    private AppCompatButton btnNext;
    private Timer quizTimer;
    private int correctAnswers= 0;
    private int inCorrectAnswers= 0;
    private int totalTimeInMins = 1;
    private int seconds = 0;
    private final List<QuestionsListModel> questionsLists  = new ArrayList<>();
    private  int currentQuestionPosition = 0;
    private  int currentAnswerPosition = 0;
    private String selectedOptionByUser="";
    private String getSelectedTopicName;
    private ImageView ivSymbolofBack;
    private TextView tvTimer;
    private TextView tvSelectedTopicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        tvNumberofQuestions = findViewById(R.id.tvNumberofQuestions);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnoption1 = findViewById(R.id.btnOption1);
        btnoption2 = findViewById(R.id.btnOption2);
        btnoption3 = findViewById(R.id.btnOption3);
        btnoption4 = findViewById(R.id.btnOption4);
        btnNext = findViewById(R.id.btnNext);
        ivSymbolofBack = findViewById(R.id.ivSymbolBack);
        tvTimer = findViewById(R.id.tvTimer);
        tvSelectedTopicName = findViewById(R.id.tvTopicName);
        getDatabaseReference();
    }

    private void getDatabaseReference() {
        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");
        tvSelectedTopicName.setText(getSelectedTopicName);
        startTimer(tvTimer);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReferenceFromUrl(" https://onlinequiz-ceccc-default-rtdb.firebaseio.com");
        takeQuestionFromDatabase(databaseReference);
    }

    private void takeQuestionFromDatabase(DatabaseReference databaseReference) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child(getSelectedTopicName).getChildren()) {
                    final String getQuestion = dataSnapshot.child("question").getValue(String.class);
                    final String getOption1 = dataSnapshot.child("option1").getValue(String.class);
                    final String getOption2 = dataSnapshot.child("option2").getValue(String.class);
                    final String getOption3 = dataSnapshot.child("option3").getValue(String.class);
                    final String getOption4 = dataSnapshot.child("option4").getValue(String.class);
                    final String getAnswer = dataSnapshot.child("answer").getValue(String.class);
                    QuestionsListModel questionsList = new QuestionsListModel(getQuestion, getOption1,
                            getOption2, getOption3, getOption4, getAnswer);
                    questionsLists.add(questionsList);
                }
                knowCurrentPositionofQuestions();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        setClickListener();
    }

    private void setClickListener()
    {
        onClickingOption1();
        onClickingOption2();
        onClickingOption3();
        onClickingOption4();
        changetoNextQuestion();
        moveToHomeActivity();
    }

    private void onClickingOption1() {
        btnoption1.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = btnoption1.getText().toString();
                btnoption1.setBackgroundResource(R.drawable.round_back_red10);
                btnoption1.setTextColor(Color.WHITE);
                revealAnswer();
                questionsLists.get(currentQuestionPosition).setUserSelectedAnswer();
            }
        });
    }

    private void onClickingOption2() {
        btnoption2.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = btnoption2.getText().toString();
                btnoption2.setBackgroundResource(R.drawable.round_back_red10);
                btnoption2.setTextColor(Color.WHITE);
                revealAnswer();
                questionsLists.get(currentQuestionPosition).setUserSelectedAnswer();
            }
        });
    }

    private void onClickingOption3() {

        btnoption3.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = btnoption3.getText().toString();
                btnoption3.setBackgroundResource(R.drawable.round_back_red10);
                btnoption3.setTextColor(Color.WHITE);
                revealAnswer();
                questionsLists.get(currentQuestionPosition).setUserSelectedAnswer();
            }
        });
    }

    private void onClickingOption4() {
        btnoption4.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty()) {
                selectedOptionByUser = btnoption4.getText().toString();
                btnoption4.setBackgroundResource(R.drawable.round_back_red10);
                btnoption4.setTextColor(Color.WHITE);
                revealAnswer();
                questionsLists.get(currentQuestionPosition).setUserSelectedAnswer();
            }
        });
    }

    private void changetoNextQuestion() {
        btnNext.setOnClickListener(view -> {
            if(selectedOptionByUser.isEmpty()){
                Toast.makeText(QuizActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
            }
            else{
                getCorrectAnswers();
                moveToNextQuestion();
            }
        });
    }

    private void moveToHomeActivity() {
        ivSymbolofBack.setOnClickListener(view -> {
            quizTimer.purge();
            quizTimer.cancel();
            startActivity(new Intent(QuizActivity.this, HomeActivity.class));
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void knowCurrentPositionofQuestions() {
        tvNumberofQuestions.setText((currentQuestionPosition + 1) + "/" + questionsLists.size());
        tvQuestion.setText(questionsLists.get(0).getQuestion());
        btnoption1.setText(questionsLists.get(0).getOption1());
        btnoption2.setText(questionsLists.get(0).getOption2());
        btnoption3.setText(questionsLists.get(0).getOption3());
        btnoption4.setText(questionsLists.get(0).getOption4());

    }

    @SuppressLint("SetTextI18n")
    private void moveToNextQuestion(){
        currentQuestionPosition++;
        if((currentQuestionPosition+1) == questionsLists.size()){
            btnNext.setText(R.string.submit);
        }
        if(currentQuestionPosition < questionsLists.size()) {
            selectedOptionByUser="";
            btnoption1.setBackgroundResource(R.drawable.round_back_white_stroke2);
            btnoption1.setTextColor(Color.parseColor("#1FBBB8"));
            btnoption2.setBackgroundResource(R.drawable.round_back_white_stroke2);
            btnoption2.setTextColor(Color.parseColor("#1FBBB8"));
            btnoption3.setBackgroundResource(R.drawable.round_back_white_stroke2);
            btnoption3.setTextColor(Color.parseColor("#1FBBB8"));
            btnoption4.setBackgroundResource(R.drawable.round_back_white_stroke2);
            btnoption4.setTextColor(Color.parseColor("#1FBBB8"));
            tvNumberofQuestions.setText((currentQuestionPosition+1)+"/"+questionsLists.size());
            tvQuestion.setText(questionsLists.get(currentQuestionPosition).getQuestion());
            btnoption1.setText(questionsLists.get(currentQuestionPosition).getOption1());
            btnoption2.setText(questionsLists.get(currentQuestionPosition).getOption2());
            btnoption3.setText(questionsLists.get(currentQuestionPosition).getOption3());
            btnoption4.setText(questionsLists.get(currentQuestionPosition).getOption4());
        }
        else{
            Intent intent = new Intent(QuizActivity.this,QuizResults.class);
            intent.putExtra("correct",correctAnswers);
            intent.putExtra("incorrect",inCorrectAnswers);
            startActivity(intent);
            finish();
        }
    }

    private void startTimer(TextView timerTextView){
    quizTimer = new Timer();
    quizTimer.scheduleAtFixedRate(new TimerTask() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
          if(seconds == 0) {
              if ( totalTimeInMins == 0) {
                  Intent intent = new Intent(QuizActivity.this, QuizResults.class);
                  getCorrectAnswers();
                  intent.putExtra("correct", correctAnswers);
                  intent.putExtra("incorrect", inCorrectAnswers);

                  startActivity(intent);
                  quizTimer.purge();
                  quizTimer.cancel();

                  finish();

              } else {
                  totalTimeInMins--;
                  seconds = 59;
              }
          }
          else {
              seconds--;
          }
          runOnUiThread(() -> {

             String finalMinutes= String.valueOf(totalTimeInMins);
             String finalSeconds= String.valueOf(seconds);

            if(finalMinutes.length() == 1){
                finalMinutes = "0" +finalMinutes;
            }
            if(finalSeconds.length() == 1){
                finalSeconds = "0" +finalSeconds;
            }

            timerTextView.setText(finalMinutes +":"+finalSeconds);
          });
        }
    },1000,1000);
    }

    private void getCorrectAnswers(){
          final String getAnswer= questionsLists.get(currentAnswerPosition).getAnswer();
          if(selectedOptionByUser.equals(getAnswer)){
              correctAnswers+=1;
          }else{
              inCorrectAnswers+=1;
          }
        currentAnswerPosition+=1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        quizTimer.purge();
        quizTimer.cancel();
        startActivity(new Intent(QuizActivity.this, HomeActivity.class));
        finish();
    }

    private void revealAnswer(){
        final String getAnswer = questionsLists.get(currentQuestionPosition).getAnswer();
        if(btnoption1.getText().toString().equals(getAnswer)){
           btnoption1.setBackgroundResource(R.drawable.round_back_green10);
           btnoption1.setTextColor(Color.WHITE);
        }
        else if(btnoption2.getText().toString().equals(getAnswer)){
            btnoption2.setBackgroundResource(R.drawable.round_back_green10);
            btnoption2.setTextColor(Color.WHITE);
        }
        else if(btnoption3.getText().toString().equals(getAnswer)){
            btnoption3.setBackgroundResource(R.drawable.round_back_green10);
            btnoption3.setTextColor(Color.WHITE);
        }

        else if(btnoption4.getText().toString().equals(getAnswer)){
            btnoption4.setBackgroundResource(R.drawable.round_back_green10);
            btnoption4.setTextColor(Color.WHITE);
        }
    }
}