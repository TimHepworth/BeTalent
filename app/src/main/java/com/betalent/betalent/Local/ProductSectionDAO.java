package com.betalent.betalent.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.betalent.betalent.Model.ProductSection;

import java.util.List;

@Dao
public interface ProductSectionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertProductSection(ProductSection productSection);

}
