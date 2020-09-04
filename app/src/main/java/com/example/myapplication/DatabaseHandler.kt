package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.support.annotation.RequiresApi
import java.io.Closeable

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ImageDatabase"
        private const val TABLE_CONTACTS = "ImgcomTable"
        private const val KEY_COMMENT = "comment"
        private const val KEY_TITLE = "title"
        private const val KEY_LINK = "link"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val sql = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_COMMENT + " TEXT," + KEY_TITLE + " TEXT,"
                + KEY_LINK + " TEXT, PRIMARY KEY (" + KEY_LINK + "))")
        db?.execSQL(sql)
    }

    fun checkComment(link:String):Boolean{
        val db = this.readableDatabase
        val sql = "SELECT EXISTS (SELECT * FROM ImgcomTable WHERE link='$link' LIMIT 1)"
        val cursor: Cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        // cursor.getInt(0) is 1 if column with value exists
        return if (cursor.getInt(0) == 1) {
            cursor.close()
            true
        } else {
            cursor.close()
            false
        }
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    //method to insert data
    fun addComment(data: DataModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_COMMENT, data.comment) // DataModelClass comment
        contentValues.put(KEY_LINK, data.link) // DataModelClass link
        contentValues.put(KEY_TITLE, data.title) // DataModelClass title
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to read data
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("Recycle")
    fun viewImageData():List<DataModelClass>{
        val dataList:ArrayList<DataModelClass> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        val cursor: Closeable?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var comment: String
        var link: String
        var title: String
        if (cursor.moveToFirst()) {
            do {
                comment = cursor.getString(cursor.getColumnIndex("comment"))
                link = cursor.getString(cursor.getColumnIndex("link"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                val data= DataModelClass(comment = comment, link = link, title = title)
                dataList.add(data)
            } while (cursor.moveToNext())
        }
        return dataList
    }
}
