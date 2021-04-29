package ru.egoran.jimbosnake20

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.egoran.jimbosnake20.db.ScoreDbHelper
import ru.egoran.jimbosnake20.db.ScoreDbManager

class ScoreActivity : AppCompatActivity() {
    lateinit var tvNickName: TextView
    lateinit var tvPoints: TextView
    val scoreDbManager = ScoreDbManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        tvNickName = findViewById(R.id.tvNickName)
        tvPoints = findViewById(R.id.tvPoints)
        val intent: Intent = intent
        val name: String? = intent.getStringExtra("myName")
        val score: String? = intent.getStringExtra("myScore")
        var names: Array<String?> = arrayOf("King", "Knight", "Loser666", "Unlucky", "Samurai", "Ninja", "John Doe", "Poor", "Noob", "null")
        var points: Array<Long?> = arrayOf(350, 300, 200, 140, 120, 100, 70, 50, 10, 0)
        names[9] = name
        points[9] = score?.toLong()
        scoreDbManager.openDB()
        for(i in 0..9){
            scoreDbManager.insertToDB(names[i].toString(), points[i].toString())
        }
        var dataList = scoreDbManager.readDBNickName(true)
        addOnTv(dataList)
        dataList = scoreDbManager.readDBNickName(false)
        addOnTv1(dataList)
        scoreDbManager.dropTable()
        scoreDbManager.closeDB()


    }

    override fun onBackPressed() {

    }
    fun onClick(view: View) {
        val intent = Intent(this, Game::class.java)
        startActivity(intent)
        finish()
    }
   private fun addOnTv(value: ArrayList<String>){
       for(item in value){
           tvNickName.append(item)
           tvNickName.append("\n")
       }
    }
    private fun addOnTv1(value: ArrayList<String>){
        for(item in value){
            tvPoints.append(item)
            tvPoints.append("\n")
        }
    }
}