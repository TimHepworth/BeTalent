package com.betalent.betalent.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.betalent.betalent.Model.Question;

import java.util.List;

@Dao
public interface QuestionDAO {

    @Query("SELECT * FROM questions")
    public List<Question> getQuestions();

    @Query("SELECT COUNT(*) FROM questions WHERE (campaignId = :campaignId)")
    public int getNumQuestions(int campaignId);

    @Query("SELECT * FROM questions WHERE (campaignId = :campaignId) AND (questionNo = :questionNo)")
    public Question getQuestion(int campaignId, int questionNo);

    @Query("SELECT * FROM questions Q INNER JOIN campaigns C ON C.campaignId = Q.campaignId WHERE (Q.campaignId = :campaignId) AND (Q.questionNo = C.currentQuestionNo)")
    public Question getNextQuestion(int campaignId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertQuestion(Question question);

}
