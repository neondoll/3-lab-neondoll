package com.company;


class Question {
    private String text;
    private String answer;

    Question(String text, String answer) {
        this.text = text;
        this.answer = answer;
    }
    String getText() { return text; }
    boolean isCorrect(String answer) { return this.answer.equals(answer); }
}
