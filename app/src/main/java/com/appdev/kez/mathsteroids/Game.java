package com.appdev.kez.mathsteroids;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private List<Question> questions;
    private int numberCorrect;
    private int numberIncorrect;
    private int totalQuestions;
    private int score;

    String difficulty;
    private Question currentQuestion;

    public Game() {
        numberCorrect = 0;
        numberIncorrect = 0;
        totalQuestions = 0;
        this.difficulty = "Easy";
        score = 0;
        currentQuestion = new Question(difficulty);
        questions = new ArrayList<Question>();


    }

    public void makeNewQuestion() {
        currentQuestion = new Question(difficulty);
        totalQuestions++;
        questions.add(currentQuestion);
    }

    public boolean checkAnswer(int submitted) {
        boolean isCorrect;
        if (currentQuestion.getAnswer() == submitted) {
            numberCorrect++;
            isCorrect = true;
            score += 10;
        } else {
            numberIncorrect++;
            isCorrect = false;
        }

        return isCorrect;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getNumberCorrect() {
        return numberCorrect;
    }


    public int getNumberIncorrect() {
        return numberIncorrect;
    }


    public int getTotalQuestions() {
        return totalQuestions;
    }


    public int getScore() {
        return score;
    }


    public Question getCurrentQuestion() {
        return currentQuestion;
    }


    public void changeDifficulty() {
        if (difficulty.equals("Easy")) {
            this.difficulty = "Medium";
            this.totalQuestions = 0;
            this.numberCorrect = 0;
        } else if (difficulty.equals("Medium")) {
            this.difficulty = "Hard";
            this.totalQuestions = 0;
            this.numberCorrect = 0;
        } else {
            this.difficulty = "End";
        }
    }

}
