package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private String selectedTopicName="";
    private TextView tvJava,tvAndroid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final LinearLayout java = findViewById(R.id.javalayout);
        final LinearLayout android= findViewById(R.id.androidlayout);
        final Button startbtn =findViewById(R.id.btnStartQuiz);
        tvJava = findViewById(R.id.tvJava);
        tvAndroid= findViewById(R.id.tvAndroid);


        java.setOnClickListener(view -> {
           selectedTopicName = tvJava.getText().toString();
            java.setBackgroundResource(R.drawable.round_back_white_stroke10);
            android.setBackgroundResource(R.drawable.round_back_white10);
        });
        android.setOnClickListener(view -> {
            selectedTopicName =tvAndroid.getText().toString();
            android.setBackgroundResource(R.drawable.round_back_white_stroke10);
            java.setBackgroundResource(R.drawable.round_back_white10);
        });
        startbtn.setOnClickListener(view -> {
            if(selectedTopicName.isEmpty()){
                Toast.makeText(HomeActivity.this, "Please Select The Topic", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(HomeActivity.this,QuizActivity.class);
                intent.putExtra("selectedTopic",selectedTopicName);
                startActivity(intent);
            }
        });
    }
}