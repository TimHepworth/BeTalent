package com.betalent.betalent.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "productSections")
public class ProductSection {

    @PrimaryKey
    private int productSectionId;
    private String sectionName;
    private int displayOrder;
    private int scaleId;
    private String sectionInstructions;

    public ProductSection() {
    }

    public ProductSection(int productSectionId, String sectionName, int displayOrder, int scaleId, String sectionInstructions) {
        this.productSectionId = productSectionId;
        this.sectionName = sectionName;
        this.displayOrder = displayOrder;
        this.scaleId = scaleId;
        this.sectionInstructions = sectionInstructions;
    }

    public int getProductSectionId() {
        return productSectionId;
    }

    public void setProductSectionId(int productSectionId) {
        this.productSectionId = productSectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getScaleId() {
        return scaleId;
    }

    public void setScaleId(int scaleId) {
        this.scaleId = scaleId;
    }

    public String getSectionInstructions() {
        return sectionInstructions;
    }

    public void setSectionInstructions(String sectionInstructions) {
        this.sectionInstructions = sectionInstructions;
    }
}
