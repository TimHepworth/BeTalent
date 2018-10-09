package com.betalent.betalent.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.betalent.betalent.Model.QuestionChoice;

import java.util.List;

@Dao
public interface QuestionChoiceDAO {

    @Query("SELECT * FROM questionChoices WHERE questionId = :questionId")
    public List<QuestionChoice> getQuestionChoices(int questionId);

    @Query("SELECT * FROM questionChoices WHERE questionChoiceId = :questionChoiceId")
    public QuestionChoice getQuestionChoice(int questionChoiceId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertQuestionChoice(QuestionChoice questionChoice);

}
