package ru.egoran.jimbosnake20.db

import android.provider.BaseColumns

object ScoreDB : BaseColumns {
    const val TABLE_NAME = "score_table"
    const val COLUMN_NAME_NICKNAME = "nickname"
    const val COLUMN_NAME_SCORE = "score"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "ScoreDb.db"

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

    const val CREATE_TABLE = """CREATE TABLE IF NOT EXISTS $TABLE_NAME (
        ${BaseColumns._ID} INTEGER PRIMARY KEY,
        $COLUMN_NAME_NICKNAME TEXT,
        $COLUMN_NAME_SCORE INTEGER)
    """
}