package com.betalent.betalent.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.betalent.betalent.Local.CampaignDAO;
import com.betalent.betalent.Local.ProductSectionDAO;
import com.betalent.betalent.Local.QuestionChoiceDAO;
import com.betalent.betalent.Local.QuestionDAO;
import com.betalent.betalent.Local.TagDAO;
import com.betalent.betalent.Local.UserDAO;

//
//  Singleton pattern class for database reference
//

@Database(entities = { User.class, Campaign.class, Question.class, QuestionChoice.class, Tag.class, ProductSection.class }, version = 1)
public abstract class BeTalentDB extends RoomDatabase {

    public abstract UserDAO getUserDao();
    public abstract CampaignDAO getCampaignDao();
    public abstract QuestionDAO getQuestionDao();
    public abstract QuestionChoiceDAO getQuestionChoiceDao();
    public abstract TagDAO getTagDao();
    public abstract ProductSectionDAO getProductSectionDao();

    private static BeTalentDB betalentDB;

    public static BeTalentDB getInstance(Context context) {
        if (null == betalentDB) {
            betalentDB = buildDatabaseInstance(context);
        }
        return betalentDB;
    }

    private static BeTalentDB buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                BeTalentDB.class,
                "betalent_db")
                .allowMainThreadQueries()
                .build();
    }

    public void cleanUp() {
        betalentDB = null;
    }
}

