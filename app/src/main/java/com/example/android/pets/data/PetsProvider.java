package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.android.pets.data.PetsContract.*;

import com.example.android.pets.CatalogActivity;

import java.util.IllegalFormatException;

public class PetsProvider extends ContentProvider {
    private PetsdbHelper mdbHelper ;
    private static final int PETS =100;
    private static final int PETS_ID = 101;

    private static final UriMatcher mUriMAcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{

        mUriMAcher.addURI(PetsContract.CONTENT_AUTHORITY,PetsContract.PATH_PETS,PETS);

        mUriMAcher.addURI(PetsContract.CONTENT_AUTHORITY,PetsContract.PATH_PETS + "/#",PETS_ID);

    }



    @Override
    public boolean onCreate() {
        mdbHelper = new PetsdbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,  String sortOrder) {

        SQLiteDatabase database = mdbHelper.getReadableDatabase();

        Cursor cursor ;

        int match = mUriMAcher.match(uri);

        switch (match){
            case PETS:
                //TODO Quesry on whole Pets table
                cursor = database.query(PetsEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null ,
                        sortOrder);
                break;

            case PETS_ID:
                selection= PetsEntry.COLUMN_PETS_ID + "=?";

                selectionArgs =new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(PetsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                    throw new IllegalArgumentException("Cannot Query not in URI"+ uri);
        }
        return cursor;
    }

    @Override
    public String getType( Uri uri) {
        int match = mUriMAcher.match(uri);
        switch (match){
            case PETS:
                return PetsEntry.CONTENT_LIST_TYPE;
            case PETS_ID:
                return PetsEntry.CONTENT_ITEM_TYPE;
            default: throw new IllegalArgumentException("uri not match = "+ uri);
        }
    }

    @Override
    public Uri insert( Uri uri, ContentValues values) {

        //For sanity check for the value enter

        // Checking that name is valid(not Null)

        String name = values.getAsString(PetsEntry.COLUMN_PETS_NAME);
        if(name.isEmpty())  throw new IllegalArgumentException("Pets name canot be null");

        //Checking that gender is valid(it can be {0,1,2})

        Integer gender = values.getAsInteger(PetsEntry.COLUMN_PETS_GENDER);
        if(!PetsEntry.idValidGender(gender)) throw new IllegalArgumentException("gender is invalid ");

        //Checking that weight is valid

        Integer weight = values.getAsInteger(PetsEntry.COLUMN_PETS_WEIGHT);
        if((weight < 0) && (weight.equals(null))) throw new IllegalArgumentException("Weight is not valid");

        SQLiteDatabase database = mdbHelper.getWritableDatabase();

        long id;
        // match the uri by uri matcher for inserting
        int match = mUriMAcher.match(uri);

        switch (match) {
            case PETS:
            id = database.insert(PetsEntry.TABLE_NAME, null, values);
            break;

            default:
                throw new IllegalArgumentException("Can not query in the url"+ uri);
        }

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mdbHelper.getWritableDatabase();
        int match = mUriMAcher.match(uri);
        switch(match){
            case PETS:
                return database.delete(PetsEntry.TABLE_NAME,selection,selectionArgs);
            case PETS_ID:
                selection= PetsEntry.COLUMN_PETS_ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(PetsEntry.TABLE_NAME,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Uri not match");

        }
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //For sanity check for the value enter

        // Checking that name is valid(not Null)

        if(values.containsKey(PetsEntry.COLUMN_PETS_NAME)) {
            String name = values.getAsString(PetsEntry.COLUMN_PETS_NAME);
            if (name.isEmpty()) throw new IllegalArgumentException("Pets name canot be null");
        }

        //Checking that gender is valid(it can be {0,1,2})

        if(values.containsKey(PetsEntry.COLUMN_PETS_GENDER)) {
            Integer gender = values.getAsInteger(PetsEntry.COLUMN_PETS_GENDER);
            if (!PetsEntry.idValidGender(gender)) throw new IllegalArgumentException("gender is invalid ");
        }

        //Checking that weight is valid

        if(values.containsKey(PetsEntry.COLUMN_PETS_WEIGHT)) {
            Integer weight = values.getAsInteger(PetsEntry.COLUMN_PETS_WEIGHT);
            if ((weight < 0) && (weight.equals(null))) throw new IllegalArgumentException("Weight is not valid");
        }

        SQLiteDatabase database = mdbHelper.getWritableDatabase();

        // match the uri by uri matcher for inserting
        int match = mUriMAcher.match(uri);
        if(values.size()== 0){
            return 0;
        }

        switch (match) {
            case PETS:
                return database.update(PetsEntry.TABLE_NAME,values,selection,selectionArgs);
            case PETS_ID:
                selection =PetsEntry.COLUMN_PETS_ID + "=?";
                selectionArgs= new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.update(PetsEntry.TABLE_NAME,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Can not query in the url"+ uri);
        }
    }

}
