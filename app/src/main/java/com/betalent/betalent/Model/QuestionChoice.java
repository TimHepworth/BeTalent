package com.betalent.betalent.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(primaryKeys = {"questionChoiceId", "questionId"}, tableName = "questionChoices")
public class QuestionChoice {

    private int questionChoiceId;
    private int questionId;
    private String choiceText;
    private int score;
    private int personalAttributeId;
    private int displayOrder;

    public QuestionChoice() {
    }

    public QuestionChoice(int questionChoiceId, int questionId, String choiceText,
                          int score, int personalAttributeId, int displayOrder) {
        this.questionChoiceId = questionChoiceId;
        this.questionId = questionId;
        this.choiceText = choiceText;
        this.score = score;
        this.personalAttributeId = personalAttributeId;
        this.displayOrder = displayOrder;
    }

    public int getQuestionChoiceId() {
        return questionChoiceId;
    }

    public void setQuestionChoiceId(int questionChoiceId) {
        this.questionChoiceId = questionChoiceId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPersonalAttributeId() {
        return personalAttributeId;
    }

    public void setPersonalAttributeId(int personalAttributeId) {
        this.personalAttributeId = personalAttributeId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

}
