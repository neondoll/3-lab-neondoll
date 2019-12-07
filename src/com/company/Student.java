package com.company;

public class Student extends User {
    private int questionsCount;
    private int rightAnswers;

    Student(String name, String login, String password) {
        super(name, login, password);
        questionsCount = 0;
        rightAnswers = 0;
    }
    void getAnswer(Question question, String answer) {
        questionsCount++;
        if (question.isCorrect(answer)) rightAnswers++;
    }
    Integer getQuestionsCount() { return questionsCount; }
    Integer getRightAnswers() { return rightAnswers; }
    public void clear() {
        questionsCount = 0;
        rightAnswers = 0;
    }
    void printStudent() { System.out.println("name: " + name + "; questionsCount: " + questionsCount + "; rightAnswer: " + rightAnswers); }

}
