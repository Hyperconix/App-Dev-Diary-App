package com.hyperconix.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.hyperconix.data.DiaryEntry

/**
 * This class represents a SQLiteHelper which is
 * used for encapsulating all CRUD (Create, Retrieve, Update and Delete)
 * operations to the database which is being used for persistent
 * storage in this application.
 *
 *
 *
 * @param context The current context where the DatabaseHelper is being used.
 *
 * @author Luke S
 */
class DatabaseHelper (context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    /**
     * This companion object is responsible for
     * storing constants which represent the
     * column names for the database table
     * as well as the name and versioning
     * information
     *
     */
    companion object {
        private const val DATABASE_NAME = "DIARY_ENTRIES_DB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "diary_entries"
        private const val ID = "id"
        private const val TITLE = "title"
        private const val DATE = "date"
        private const val ATTACHED_IMAGE_PATH = "attached_image_path"
        private const val CONTENT = "content"
    }

    /**
     * This function is responsible for performing
     * the insert operation and adding a diary
     * entry to the database.
     *
     * @param diaryEntry The diary entry to be added to the database.
     */
    fun addDiaryEntry(diaryEntry: DiaryEntry) {
        val contentValues = ContentValues()

        val db = this.writableDatabase

        contentValues.put(TITLE, diaryEntry.title)

        contentValues.put(DATE, diaryEntry.date)

        contentValues.put(ATTACHED_IMAGE_PATH, diaryEntry.attachedImagePath)

        contentValues.put(CONTENT, diaryEntry.content)

        db.insert(TABLE_NAME, null, contentValues)

        db.close()
    }

    /**
     * This function is responsible for performing the
     * retrieve operation and returning all current
     * entries stored in the database as a list
     *
     * @return The list of diary entries currently stored in the database.
     */
    fun getDiaryEntries() : MutableList<DiaryEntry> {
        val diaryEntries = ArrayList<DiaryEntry>()

        val selectEntriesQuery = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase

        val cursor = db.rawQuery(selectEntriesQuery, null)

        if(cursor.moveToFirst()) {
            do {

                val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))

                val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))

                val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))

                val attachedImagePath = cursor.getString(cursor.getColumnIndexOrThrow(
                    ATTACHED_IMAGE_PATH))

                val content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT))

                val currentDiaryEntry = DiaryEntry(title, date, attachedImagePath, content)

                currentDiaryEntry.id = id

                diaryEntries.add(currentDiaryEntry)

            } while(cursor.moveToNext())
        }

        cursor.close()

        db.close()

        return diaryEntries
    }

    /**
     * This function is responsible for performing
     * a full delete operation and deleting all
     * entries which are currently stored
     * in the database.
     *
     */
    fun deleteAllEntries() {
        val db = this.writableDatabase

        db.execSQL("DELETE FROM $TABLE_NAME")


        db.close()
    }

    /**
     * This function is responsible for performing
     * the delete operation and deleting a
     * specific entry using the provided
     * unique identifier.
     *
     * @param id The id of the entry to remove from the database.
     */
    fun deleteByID(id: Int) {
        val db = this.writableDatabase

        db.execSQL("DELETE FROM $TABLE_NAME WHERE $ID = $id")

        db.close()
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TITLE TEXT, $DATE TEXT, $ATTACHED_IMAGE_PATH TEXT, $CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db!!.execSQL(dropTableQuery)
        onCreate(db)
    }

}