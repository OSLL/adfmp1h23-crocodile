package ru.croco.crocodile

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.BufferedReader
import java.io.IOException


/**
 * Created by ravi on 15/03/18.
 */
class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Note.CREATE_TABLE)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.version = oldVersion
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS $DATABASE_NAME")
            onCreate(db)
        }
    }

    fun insertNote(word: String?, description: String?, level: Int?, area: String?): Long {
        val db = this.writableDatabase
        print("TAG")

        val values = ContentValues().apply {
            put(Note.COLUMN_WORD, word)
            put(Note.COLUMN_DESCRIPTION, description)
            put(Note.COLUMN_LEVEL, level)
            put(Note.COLUMN_AREA, area)
        }

        val newRowId = db.insert(Note.TABLE_NAME, null, values)
        if (newRowId == (-1).toLong()) {
            print("Error in db")
        }
        db.close()

        return newRowId
    }

    @SuppressLint("Range")
    fun getNote(id: Long): Note {
        val db = this.readableDatabase
        val projection = arrayOf(
            Note.COLUMN_ID,
            Note.COLUMN_WORD,
            Note.COLUMN_DESCRIPTION,
            Note.COLUMN_LEVEL,
            Note.COLUMN_AREA
        )

        val selection = "${Note.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(
            Note.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null,
            null
        )

        cursor?.moveToFirst()

        val note = Note(
            cursor!!.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(Note.COLUMN_WORD)),
            cursor.getString(cursor.getColumnIndex(Note.COLUMN_DESCRIPTION)),
            cursor.getInt(cursor.getColumnIndex(Note.COLUMN_LEVEL)),
            cursor.getString(cursor.getColumnIndex(Note.COLUMN_AREA)),
        )

        // close the db connection
        cursor.close()
        return note
    }

    // Select All Query
    val allNotes: List<Note>
        @SuppressLint("Range")
        get() {
            val notes: MutableList<Note> = ArrayList()

            // Select All Query
            val selectQuery = "SELECT  * FROM " + Note.TABLE_NAME
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                do {
                    val note = Note()
                    note.id = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID))
                    note.word = cursor.getString(cursor.getColumnIndex(Note.COLUMN_WORD))
                    note.description =
                        cursor.getString(cursor.getColumnIndex(Note.COLUMN_DESCRIPTION))
                    note.levell = cursor.getInt(cursor.getColumnIndex(Note.COLUMN_LEVEL))
                    note.area = cursor.getString(cursor.getColumnIndex(Note.COLUMN_AREA))
                    notes.add(note)
                } while (cursor.moveToNext())
            }
            // close db connection
            db.close()
            return notes
        }

    val notesCount: Int
        get() {
            val countQuery = "SELECT  * FROM " + Note.TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            val count = cursor.count
            println("count: ${count}")
            cursor.close()

            return count
        }

    fun updateNote(note: Note): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Note.COLUMN_WORD, note.word)
        values.put(Note.COLUMN_DESCRIPTION, note.description)
        values.put(Note.COLUMN_LEVEL, note.levell)
        values.put(Note.COLUMN_AREA, note.area)

        // updating row
        return db.update(
            Note.TABLE_NAME,
            values,
            Note.COLUMN_ID + " = ?",
            arrayOf(java.lang.String.valueOf(note.id))
        )
    }

    fun deleteNote(note: Note) {
        val db = this.writableDatabase
        db.delete(
            Note.TABLE_NAME,
            Note.COLUMN_ID + " = ?",
            arrayOf(java.lang.String.valueOf(note.id))
        )
        db.close()
    }

    @Throws(IOException::class)
    fun insertFromFile(insertReader: BufferedReader): Int {
        println("insertFromFile")
        val db = this.writableDatabase
        db.execSQL(Note.DROP_TABLE)
        onCreate(db)
        this.onUpgrade(db,1, 1)
        var result = 0
        println("st")
        var st: String? = insertReader.readLine()
        while (st != null) {
            println("WARNINNNG st $st")
            db.execSQL(st)
            result++
            st = insertReader.readLine()
        }
        insertReader.close()

        return result
    }

    companion object {
        // Database Version & Name
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CROCO"
    }
}
