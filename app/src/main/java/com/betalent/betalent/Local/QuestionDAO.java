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

    @Query("SELECT * FROM questions WHERE questionId = :questionId")
    public Question getQuestion(int questionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertQuestion(Question question);

}
