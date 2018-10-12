package com.betalent.betalent.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tags")
public class Tag {

    @PrimaryKey
    private int tagId;
    private String tagName;
    private String tagImage;
    private String cardText;
    private String tagType;

    public Tag() {

    }

    @Ignore
    public Tag(int tagId, String tagName, String tagImage, String cardText, String tagType) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagImage = tagImage;
        this.cardText = cardText;
        this.tagType = tagType;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagImage() {
        return tagImage;
    }

    public void setTagImage(String tagImage) {
        this.tagImage = tagImage;
    }

    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
}
