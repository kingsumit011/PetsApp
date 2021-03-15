package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

public class PetsContract {

    public static final String  CONTENT_AUTHORITY = "com.example.android.pets";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final String PATH_PETS = "Pets";


    //    Constructor of Pets Contract
    private PetsContract(){}

    /*Inner Class in PetsContract for storing name of coloum*/

    public  static final class PetsEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI ,PATH_PETS);

        public static  final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"+ CONTENT_AUTHORITY + "/"+PATH_PETS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"+ CONTENT_AUTHORITY + "/"+PATH_PETS;

        public static final String TABLE_NAME = "Pets";

        public static final String COLUMN_PETS_ID ="ID";

        public static final String COLUMN_PETS_NAME ="Name";

        public static final String COLUMN_PETS_GENDER ="Gender";

        public static final String COLUMN_PETS_WEIGHT ="Weight";

        public static final String COLUMN_PETS_BREED ="Breed";

        public static final int GENDER_UNKNOWN =0;
        public static final int GENDER_MALE =1;
        public static final int GENDER_FEMALE = 2;

        public static final boolean idValidGender(int gender){
            if(gender==GENDER_FEMALE||gender==GENDER_MALE||gender==GENDER_UNKNOWN){
                return true;
            }
            return false;
        }
    }
}
