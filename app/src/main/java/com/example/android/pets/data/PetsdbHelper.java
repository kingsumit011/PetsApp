package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class PetsdbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Shelter.db";
    public PetsdbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE "+ PetsContract.PetsEntry.TABLE_NAME +"("+
                                        PetsContract.PetsEntry.COLUMN_PETS_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                                        PetsContract.PetsEntry.COLUMN_PETS_NAME +" TEXT NOT NULL," +
                                        PetsContract.PetsEntry.COLUMN_PETS_GENDER +" INTEGER NOT NULL ," +
                                        PetsContract.PetsEntry.COLUMN_PETS_BREED +" TEXT ," +
                                        PetsContract.PetsEntry.COLUMN_PETS_WEIGHT +" INTEGER NOT NULL DEFAULT 0 );";

        db.execSQL(SQL_CREATE_PETS_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
