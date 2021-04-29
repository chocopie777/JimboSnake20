package ru.egoran.jimbosnake20

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.egoran.jimbosnake20.GameCore.thread
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnStartGame: Button = findViewById(R.id.btnStartGame)
        val btnInfo: Button = findViewById(R.id.btnInfo)
        btnStartGame.setOnClickListener(this)
        btnInfo.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnStartGame -> {
                val intent = Intent(this, Game::class.java)
                startActivity(intent)
                finish()
            }
            R.id.btnInfo -> {
                val dialogAlert = AlertDialog.Builder(this)
                dialogAlert.setTitle("Информация о приложении")
                dialogAlert.setMessage("""
                    ---JIMBOSNAKE2.0---
                    Разработчик: Егор Бардин
                    Студент 3 курса, группы 1183б
                """.trimIndent())
                dialogAlert.setIcon(R.mipmap.ic_launcher)
                dialogAlert.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                }
                dialogAlert.show()
            }
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle("Выйти из игры?")
                .setPositiveButton("Да") { _, _ ->
                    finish()
                }
                .setNegativeButton("нет") { _, _ ->

                }
                .setCancelable(false)
                .create()
                .show()
    }
}