package com.example.ecocarbontracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private String[] questions = {
            "Which of the following is a major greenhouse gas responsible for climate change?",
            "What is the primary source of renewable energy?",
            "Which activity contributes significantly to deforestation and climate change?",
            "What is the main purpose of the Paris Agreement?",
            "Which sector is a significant contributor to carbon emissions?",
            "What is the term for the process of capturing and storing carbon dioxide emissions?",
            "Which international day is dedicated to raising awareness about climate change?",
            "What is the impact of melting ice caps on sea levels?",
            "Which alternative transportation method helps reduce carbon emissions?",
            "What is the term for designing products to minimize environmental impact throughout their lifecycle?"
    };

    private String[][] options = {
            {"Carbon dioxide (CO2)", "Nitrous oxide (N2O)", "Methane (CH4)", "Ozone (O3)"},
            {"Solar", "Coal", "Natural gas", "Nuclear"},
            {"Agriculture", "Renewable energy", "Urbanization", "Logging"},
            {"Combatting deforestation", "Promoting biodiversity", "Reducing greenhouse gas emissions", "International trade agreements"},
            {"Transportation", "Agriculture", "Industry", "Residential"},
            {"Carbon capture", "Emission reduction", "Carbon footprint", "Deforestation"},
            {"Earth Day", "World Environment Day", "International Climate Action Day", "World Oceans Day"},
            {"Decrease in sea levels", "No impact", "Increase in sea levels", "Formation of new islands"},
            {"Walking", "Electric cars", "Private jets", "Motorcycles"},
            {"Greenwashing", "Sustainable design", "Eco-friendly manufacturing", "Carbon offsetting"}
    };

    private int[] correctAnswers = {2, 0, 3, 2, 0, 0, 1, 2, 1, 1};

       private int currentQuestionIndex = 0;
        private int score = 0;
        private int highScore;

        private TextView questionTextView;
        private RadioGroup optionsRadioGroup;
        private TextView scoreTextView;
        private TextView highScoreTextView;
        private int userLevel;
        private Button resetButton;
        private Button scoreReset;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_quiz);

            // Initialize UI elements
            questionTextView = findViewById(R.id.questionTextView);
            optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
            scoreTextView=findViewById(R.id.scoreTextView);
            highScoreTextView=findViewById(R.id.highScoreTextView);


            resetButton = findViewById(R.id.resetHighScoreButton);
            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetHighScore();
                    Toast.makeText(QuizActivity.this,"High Score Reset to 0", Toast.LENGTH_SHORT).show();
                }
            });
            loadHighScore();

            Button achievementsButton = findViewById(R.id.AchievementsButton);
            achievementsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(QuizActivity.this,AchievementsActivity.class);
                    intent.putExtra("userLevel",determineUserLevel(highScore));
                    startActivity(intent);
                }
            });
            // Display the first question

            displayQuestion();

            Button restartButton = findViewById(R.id.restartbutton);
            restartButton.setVisibility(View.INVISIBLE);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    restartQuiz();
                }
            });
        }

    private void resetHighScore() {
            score = 0;
            highScore = 0;
            updateHighScore();
            highScoreTextView.setText("High Score: " + highScore);

        }


    private void displayQuestion() {
            questionTextView.setText(questions[currentQuestionIndex]);

            optionsRadioGroup.removeAllViews();
            for (int i = 0; i < options[currentQuestionIndex].length; i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(options[currentQuestionIndex][i]);
                optionsRadioGroup.addView(radioButton);
            }
            scoreTextView.setText("Score: " + score);
            highScoreTextView.setText("High Score: " + highScore);
        }

        public void onAnswerSubmit(View view) {
            int selectedAnswerIndex = optionsRadioGroup.indexOfChild(findViewById(optionsRadioGroup.getCheckedRadioButtonId()));

            // Check if an option is selected
            if (selectedAnswerIndex != -1) {
                // Check if the selected answer is correct
                boolean isCorrect = (selectedAnswerIndex == correctAnswers[currentQuestionIndex]);

                displayAnswerFeedback(isCorrect);
                if(isCorrect){
                    score++;
                }
                updateHighScore();

                // Move to the next question or finish the quiz
                if (currentQuestionIndex < questions.length - 1) {
                    currentQuestionIndex++;
                    displayQuestion();
                } else {
                    // The quiz is finished, show the score or perform any other actions
                    showQuizResult();
                    Button restartButton = findViewById(R.id.restartbutton);
                    restartButton.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        }

    private void displayAnswerFeedback(boolean isCorrect) {
            String feedback = isCorrect ? "Correct!" : "Incorrect!";
            Toast.makeText(this,feedback,Toast.LENGTH_SHORT).show();
    }

    private void showQuizResult() {
        // Display the final score
        Toast.makeText(this, "Quiz Completed. Your Score: " + score + "/10", Toast.LENGTH_LONG).show();
        Button restartButton = findViewById(R.id.restartbutton);
        restartButton.setVisibility(View.VISIBLE);
        updateHighScore();

    }


    public void restartQuiz() {
            // Reset quiz variables
            currentQuestionIndex = 0;
            score = 0;
        displayQuestion();
        Button restartButton = findViewById(R.id.restartbutton);
        restartButton.setVisibility(View.INVISIBLE);
    }
    private void loadHighScore() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        highScore = preferences.getInt("highScore",0);
    }
    private void saveHighScore(){
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highScore",highScore);
            editor.apply();
    }
    private void updateHighScore(){
            if(score>highScore){
                highScore= score;
                saveHighScore();
                userLevel = determineUserLevel(highScore);
            }
    }
    private int determineUserLevel(int score) {
        if (score >= 10) {
            return 6;
        } else if (score >= 8) {
            return 5;
        } else if (score >= 6) {
            return 4;
        } else if (score >= 4) {
            return 3;
        } else if (score >= 2) {
            return 2;
        } else {
            return 1;
        }
    }

        }





