package com.betalent.betalent.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.betalent.betalent.Model.Campaign;

import java.util.List;

@Dao
public interface CampaignDAO {

    @Query("SELECT * FROM campaigns")
    public List<Campaign> getCampaigns();

    @Query("SELECT * FROM campaigns WHERE campaignId = :campaignId")
    public Campaign getCampaign(int campaignId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertCampaign(Campaign campaign);

    @Query("UPDATE campaigns SET currentQuestionNo = currentQuestionNo + :questionDelta WHERE campaignId = :campaignId")
    public long setNextQuestion(int campaignId, int questionDelta);

}
