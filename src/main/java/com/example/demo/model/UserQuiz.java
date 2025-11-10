package com.example.demo.model;

public class UserQuiz {
    private int currentIndex = 0;
    private int score = 0;

    public UserQuiz() {
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void incrementIndex() {
        this.currentIndex++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore() {
        this.score++;
    }

}
