package com.betalent.betalent.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.betalent.betalent.Model.Campaign;
import com.betalent.betalent.Model.Tag;

import java.util.List;

@Dao
public interface TagDAO {

    @Query("SELECT * FROM tags")
    public List<Campaign> getTags();

    @Query("SELECT * FROM tags WHERE tagId = :tagId")
    public Campaign getTag(int tagId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertTag(Tag tag);

}
