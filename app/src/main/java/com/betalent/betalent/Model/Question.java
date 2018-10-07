package com.betalent.betalent.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question {

    @PrimaryKey
    private int questionId;
    private String sectionName;
    private int pageNumber;
    private int displayOrder;
    private int numScaleChoices;
    private String questionType;
    private String questionTextSelf;
    private String questionTextOthers;
    private String questionChoices;

    public Question() {

    }

    public Question(int questionId,
                    String sectionName,
                    int pageNumber,
                    int displayOrder,
                    int numScaleChoices,
                    String questionType,
                    String questionTextSelf,
                    String questionTextOthers) {
        this.questionId = questionId;
        this.sectionName = sectionName;
        this.pageNumber = pageNumber;
        this.displayOrder = displayOrder;
        this.numScaleChoices = numScaleChoices;
        this.questionType = questionType;
        this.questionTextSelf = questionTextSelf;
        this.questionTextOthers = questionTextOthers;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getNumScaleChoices() {
        return numScaleChoices;
    }

    public void setNumScaleChoices(int numScaleChoices) {
        this.numScaleChoices = numScaleChoices;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionTextSelf() {
        return questionTextSelf;
    }

    public void setQuestionTextSelf(String questionTextSelf) {
        this.questionTextSelf = questionTextSelf;
    }

    public String getQuestionTextOthers() {
        return questionTextOthers;
    }

    public void setQuestionTextOthers(String questionTextOthers) {
        this.questionTextOthers = questionTextOthers;
    }

    public String getQuestionChoices() {
        return questionChoices;
    }

    public void setQuestionChoices(String questionChoices) {
        this.questionChoices = questionChoices;
    }
}
