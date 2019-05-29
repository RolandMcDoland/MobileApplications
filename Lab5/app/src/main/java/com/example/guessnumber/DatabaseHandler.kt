package com.example.guessnumber

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSIOM) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME" +
                "($ID Integer PRIMARY KEY AUTOINCREMENT NOT NULL, $FIRST_NAME TEXT, $LAST_NAME TEXT, $SCORE Integer)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    //Inserting (Creating) data
    fun addUser(user: Users): Boolean {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID, user.id)
        values.put(FIRST_NAME, user.username)
        values.put(LAST_NAME, user.password)
        values.put(SCORE, user.score)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedID", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    //get all users
    fun validateUser(user: String, pass: String): Users {
        var out: Users = Users()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var id = cursor.getInt(cursor.getColumnIndex(ID))
                    var firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    var lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
                    var score = cursor.getInt(cursor.getColumnIndex(SCORE))

                    if(user.equals(firstName) && pass.equals(lastName))
                    {
                        out.id = id
                        out.username = firstName
                        out.password = lastName
                        out.score = score
                        break
                    }
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return out
    }

    fun updateUser(user: Users) {
        val db = this.writableDatabase
        var values = ContentValues()
        values.put(ID, user.id)
        values.put(FIRST_NAME, user.username)
        values.put(LAST_NAME, user.password)
        values.put(SCORE, user.score)

        val retVal = db.update(TABLE_NAME, values, "id = " + user.id.toString(), null)

        db.close()

    }

    companion object {
        private val DB_NAME = "UsersDB"
        private val DB_VERSIOM = 1;
        private val TABLE_NAME = "users"
        private val ID = "id"
        private val FIRST_NAME = "username"
        private val LAST_NAME = "password"
        private val SCORE = "score"
    }
}