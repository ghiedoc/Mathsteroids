package com.appdev.kez.mathsteroids;

import java.util.Random;

public class Question {

    private int firstNumber;
    private int secondNumber;
    private int answer;
    private int[] answerArray;
    private int answerPosition;
    private int upperLimit;
    private String questionPhrase;

    public Question(String difficulty) {
        Random randomNumberMaker = new Random();

        this.firstNumber = randomNum1(difficulty);
        this.secondNumber = randomNum2();
        this.answer = this.firstNumber * this.secondNumber;
        this.questionPhrase = firstNumber + " X " + secondNumber;

        this.answerPosition = randomNumberMaker.nextInt(4);
        this.answerArray = new int[]{1, 2, 3, 4};
        this.answerArray[0] = answer + 1;
        this.answerArray[1] = answer + 10;
        this.answerArray[2] = answer + 5;
        this.answerArray[3] = answer + 2;

        this.answerArray = shuffleArray(this.answerArray);
        answerArray[answerPosition] = answer;
    }

    private int[] shuffleArray(int[] array) {
        int index, temp;
        Random randomNumberGen = new Random();

        for (int i = array.length - 1; i > 0; i--) {
            index = randomNumberGen.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }


    public int getFirstNumber() {
        return firstNumber;
    }


    public int getSecondNumber() {
        return secondNumber;
    }


    public int getAnswer() {
        return answer;
    }


    public int[] getAnswerArray() {
        return answerArray;
    }

    public int getAnswerPosition() {
        return answerPosition;
    }

    public int getUpperLimit() {
        return upperLimit;
    }


    public String getQuestionPhrase() {
        return questionPhrase;
    }


    public int randomNum1(String difficulty) {
        if (difficulty.equals("Easy")) {
            return (int) (Math.random() * 6);
        } else if (difficulty.equals("Medium")) {
            return (int) (Math.random() * 2) + 6;
        } else {
            return (int) (Math.random() * 3) + 8;
        }
    }

    public int randomNum2() {
        return (int) (Math.random() * 11);
    }

}
