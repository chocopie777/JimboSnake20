package ru.egoran.jimbosnake20.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.egoran.jimbosnake20.db.ScoreDB.COLUMN_NAME_NICKNAME
import ru.egoran.jimbosnake20.db.ScoreDB.COLUMN_NAME_SCORE
import ru.egoran.jimbosnake20.db.ScoreDB.CREATE_TABLE
import ru.egoran.jimbosnake20.db.ScoreDB.SQL_DELETE_TABLE
import ru.egoran.jimbosnake20.db.ScoreDB.TABLE_NAME

class ScoreDbManager(context: Context) {
    val scoreDbHelper = ScoreDbHelper(context)
    var db: SQLiteDatabase? = null
    val sortOrder = "$COLUMN_NAME_SCORE DESC"
    fun openDB() {
        db = scoreDbHelper.writableDatabase
    }
    fun closeDB(){
        scoreDbHelper.close()
    }
    fun insertToDB(nickname: String, score: String){
        val values = ContentValues().apply{
            put(COLUMN_NAME_NICKNAME, nickname)
            put(COLUMN_NAME_SCORE, score)
        }
        db?.insert(TABLE_NAME, null, values)
    }
    fun readDBNickName(_raw: Boolean) : ArrayList<String>{
        val dataList = ArrayList<String>()
        val cursor = db?.query(TABLE_NAME, null, null, null, null, null, sortOrder)
        while(cursor?.moveToNext()!!){
            if(_raw){
                val dataText = cursor?.getString(cursor.getColumnIndex(COLUMN_NAME_NICKNAME))
                dataList.add(dataText.toString())
            }
            else{
                val dataText = cursor?.getString(cursor.getColumnIndex(COLUMN_NAME_SCORE))
                dataList.add(dataText.toString())
            }
        }
        cursor?.close()
        return dataList
    }
    fun dropTable(){
        db?.execSQL(SQL_DELETE_TABLE)
        db?.execSQL(CREATE_TABLE)
    }
}