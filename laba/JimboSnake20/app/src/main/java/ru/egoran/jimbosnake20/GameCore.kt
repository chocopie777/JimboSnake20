package ru.egoran.jimbosnake20

import android.util.Log
import android.widget.FrameLayout
const val START_SPEED = 600L
const val END_SPEED = 300L
//обозначаю класс синглтоном
object GameCore {
    var nextMove:() -> Unit = {}
    var isPlay = true
    var thread: Thread
    var speedSnake = START_SPEED
    //побочный поток в котором будет обрабатыватся движение змеи
    init{
        thread = Thread(Runnable {
            while (true){
                Thread.sleep(speedSnake)
                if(isPlay) {
                    nextMove()
                }
            }
        })
        thread.start()
    }
    fun startTheGame(){
        speedSnake = START_SPEED
        isPlay = true
    }
}