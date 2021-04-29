package ru.egoran.jimbosnake20.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.egoran.jimbosnake20.db.ScoreDB.CREATE_TABLE
import ru.egoran.jimbosnake20.db.ScoreDB.DATABASE_NAME
import ru.egoran.jimbosnake20.db.ScoreDB.DATABASE_VERSION
import ru.egoran.jimbosnake20.db.ScoreDB.SQL_DELETE_TABLE

class ScoreDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}