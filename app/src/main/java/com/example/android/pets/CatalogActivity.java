/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.*;
import com.example.android.pets.data.PetsContract.*;



/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    private PetsdbHelper mDbHelper;

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new PetsdbHelper(this);
//        displayDatabaseInfo();


    }
    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        String[] projection = {
                PetsEntry.COLUMN_PETS_ID,
                PetsEntry.COLUMN_PETS_NAME,
                PetsEntry.COLUMN_PETS_WEIGHT,
                PetsEntry.COLUMN_PETS_BREED,
                PetsEntry.COLUMN_PETS_GENDER
        };

        // to get a Cursor that contains all rows from the pets table.

        Cursor cursor= getContentResolver().query( PetsEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        // Display the number of rows in the Cursor (which reflects the number of rows in the
        // pets table in the database).
        TextView displayView = findViewById(R.id.text_view_pet);
        try {

            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
            Log.e("cursor count", String.valueOf(cursor.getCount()));

            displayView.append("\n"+PetsContract.PetsEntry.COLUMN_PETS_ID + "  -  "+ PetsContract.PetsEntry.COLUMN_PETS_NAME);

            int idColoumIndex = cursor.getColumnIndex(PetsContract.PetsEntry.COLUMN_PETS_ID);

            int nameColoumIndex = cursor.getColumnIndex(PetsContract.PetsEntry.COLUMN_PETS_NAME);

            int genderColoumIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PETS_GENDER);

            int breedColoumIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PETS_BREED);

            int weightColoumIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PETS_WEIGHT);


            while(cursor.moveToNext()){

                int currentId = cursor.getInt(idColoumIndex);

                String currentName = cursor.getString(nameColoumIndex);

                int gender = cursor.getInt(genderColoumIndex);

                String breed = cursor.getString(breedColoumIndex);

                int weight = cursor.getInt(weightColoumIndex);

                displayView.append("\n"+currentId + "  -  "+currentName+" - "+
                        gender+" - "+ breed+" - "+ weight);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                insertsPets();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

   //function for insering dummy data in the app
    private void insertsPets(){
        ContentValues cv = new ContentValues();

        cv.put(PetsContract.PetsEntry.COLUMN_PETS_NAME,"Toto");
        cv.put(PetsContract.PetsEntry.COLUMN_PETS_GENDER , 0);
        cv.put(PetsContract.PetsEntry.COLUMN_PETS_BREED, "Dog");
        cv.put(PetsContract.PetsEntry.COLUMN_PETS_WEIGHT,76);

        Uri uri = getContentResolver().insert(PetsEntry.CONTENT_URI,cv);

        long newRowid = ContentUris.parseId(uri);

        if(newRowid==-1){
             Log.e("Database","Data is not added");
         }
    }

}
