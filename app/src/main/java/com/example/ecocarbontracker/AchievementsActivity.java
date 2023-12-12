package com.example.ecocarbontracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievementsActivity extends AppCompatActivity {
    private int userLevel;
    private ImageView level1Image;
    private ImageView level2Image;
    private ImageView level3Image;
    private ImageView level4Image;
    private ImageView level5Image;
    private Button quiz;
    private Button mainpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        // Retrieve userLevel from the Intent
        userLevel = getIntent().getIntExtra("userLevel", 1);

        // Initialize ImageViews
        level1Image = findViewById(R.id.level1Image);
        level2Image = findViewById(R.id.level2Image);
        level3Image = findViewById(R.id.level3Image);
        level4Image = findViewById(R.id.level4Image);
        level5Image = findViewById(R.id.level5Image);
        quiz = findViewById(R.id.returnQuizButton);
        mainpage = findViewById(R.id.returnMainButton);
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AchievementsActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AchievementsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        // Display images based on userLevel
        displayImages(userLevel);
    }

    private void displayImages(int userLevel) {
        // Make corresponding images visible based on the user's level
        switch (userLevel) {
            case 2:
                level1Image.setVisibility(ImageView.VISIBLE);
                level2Image.setVisibility(ImageView.INVISIBLE);
                level3Image.setVisibility(ImageView.INVISIBLE);
                level4Image.setVisibility(ImageView.INVISIBLE);
                level5Image.setVisibility(ImageView.INVISIBLE);
                break;
            case 3:
                level1Image.setVisibility(ImageView.VISIBLE);
                level2Image.setVisibility(ImageView.VISIBLE);
                level3Image.setVisibility(ImageView.INVISIBLE);
                level4Image.setVisibility(ImageView.INVISIBLE);
                level5Image.setVisibility(ImageView.INVISIBLE);
                break;
            case 4:
                level1Image.setVisibility(ImageView.VISIBLE);
                level2Image.setVisibility(ImageView.VISIBLE);
                level3Image.setVisibility(ImageView.VISIBLE);
                level4Image.setVisibility(ImageView.INVISIBLE);
                level5Image.setVisibility(ImageView.INVISIBLE);
                break;
            case 5:
                level1Image.setVisibility(ImageView.VISIBLE);
                level2Image.setVisibility(ImageView.VISIBLE);
                level3Image.setVisibility(ImageView.VISIBLE);
                level4Image.setVisibility(ImageView.VISIBLE);
                level5Image.setVisibility(ImageView.INVISIBLE);
                break;
            case 6:
                level1Image.setVisibility(ImageView.VISIBLE);
                level2Image.setVisibility(ImageView.VISIBLE);
                level3Image.setVisibility(ImageView.VISIBLE);
                level4Image.setVisibility(ImageView.VISIBLE);
                level5Image.setVisibility(ImageView.VISIBLE);
                break;
            // Add more cases if you have higher levels
            default:
                level1Image.setVisibility(ImageView.INVISIBLE);
                level2Image.setVisibility(ImageView.INVISIBLE);
                level3Image.setVisibility(ImageView.INVISIBLE);
                level4Image.setVisibility(ImageView.INVISIBLE);
                level5Image.setVisibility(ImageView.INVISIBLE);
                // Handle other cases
                break;
        }
    }
}