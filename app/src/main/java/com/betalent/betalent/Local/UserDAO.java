package com.betalent.betalent.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Insert;

import com.betalent.betalent.Model.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM users")
    public User getUser();

    @Query("SELECT * FROM users WHERE emailAddress = :emailAddress")
    public User getUser(String emailAddress);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertUser(User user);

}
