package com.betalent.betalent.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "campaigns")
public class Campaign {

    @PrimaryKey
    private int campaignUserId;
    private int campaignId;
    private int companyId;
    private int currentQuestionNo;
    private String campaignName;
    private Long endDate;
    private String assesseeForename;
    private String assesseeSurname;
    private String assessmentStatus;
    private String productIcon;

    public Campaign() {
    }

    public Campaign(int campaignUserId,
                    int campaignId,
                    int companyId,
                    int currentQuestionNo,
                    String campaignName,
                    Long endDate,
                    String assesseeForename,
                    String assesseeSurname,
                    String assessmentStatus,
                    String productIcon) {
        this.campaignUserId = campaignUserId;
        this.campaignId = campaignId;
        this.companyId = companyId;
        this.currentQuestionNo = currentQuestionNo;
        this.campaignName = campaignName;
        this.endDate = endDate;
        this.assesseeForename = assesseeForename;
        this.assesseeSurname = assesseeSurname;
        this.assessmentStatus = assessmentStatus;
        this.productIcon = productIcon;
    }

    public int getCampaignUserId() {
        return campaignUserId;
    }

    public void setCampaignUserId(int campaignUserId) {
        this.campaignUserId = campaignUserId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getAssesseeForename() {
        return assesseeForename;
    }

    public void setAssesseeForename(String assesseeForename) {
        this.assesseeForename = assesseeForename;
    }

    public String getAssesseeSurname() {
        return assesseeSurname;
    }

    public void setAssesseeSurname(String assesseeSurname) {
        this.assesseeSurname = assesseeSurname;
    }

    public String getAssessmentStatus() {
        return assessmentStatus;
    }

    public void setAssessmentStatus(String assessmentStatus) {
        this.assessmentStatus = assessmentStatus;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public int getCurrentQuestionNo() {
        return currentQuestionNo;
    }

    public void setCurrentQuestionNo(int currentQuestionNo) {
        this.currentQuestionNo = currentQuestionNo;
    }
}
