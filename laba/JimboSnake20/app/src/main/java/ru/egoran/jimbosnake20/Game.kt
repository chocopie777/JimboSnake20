package ru.egoran.jimbosnake20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import org.jetbrains.annotations.Nullable
import ru.egoran.jimbosnake20.GameCore.isPlay
import ru.egoran.jimbosnake20.GameCore.speedSnake
import ru.egoran.jimbosnake20.GameCore.startTheGame

//константа для измненеия размеров змейки и фруктов
const val HEAD_SIZE = 100
const val CELLS_ON_FIELD = 11
class Game : AppCompatActivity() {
    lateinit var tvScore: TextView
    //список где хранятся части хвоста змеи
    private val allTale = mutableListOf<Tale>()
    //создание обьекта фруктов на уровне класса
    private val fruit by lazy {
        ImageView(this)
                .apply {
                    this.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
                    this.setImageResource(R.drawable.ic_banana)
                }
    }
    //создание обьекта головы змеи на уровне класса
    private val head by lazy {
        ImageView(this)
                .apply {
                    this.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
                    this.setImageResource(R.drawable.snake_head)
                }
    }
    private var currentDirection = Directions.DOWN
    //создал обьект слоя
    lateinit var container: FrameLayout
    lateinit var ivPause: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        tvScore = findViewById(R.id.tvScore)
        //инициализировал слой индетификатором
        container = findViewById(R.id.container)
        //создал обьект картинки с индетификаторами
        val ivArrowUp: ImageView = findViewById(R.id.ivArrowUp)
        val ivArrowDown: ImageView = findViewById(R.id.ivArrowDown)
        val ivArrowLeft: ImageView = findViewById(R.id.ivArrowLeft)
        val ivArrowRight: ImageView = findViewById(R.id.ivArrowRight)
        ivPause = findViewById(R.id.ivPause)

        container.layoutParams = LinearLayout.LayoutParams(HEAD_SIZE * CELLS_ON_FIELD, HEAD_SIZE * CELLS_ON_FIELD)

        startTheGame()
        generateNewFruit()
        //при запуске игры змейка по дефолту ползет вниз
        GameCore.nextMove = { move(Directions.DOWN) }
        //обработка нажатия кнопок вверх вниз и т.д
        ivArrowUp.setOnClickListener { GameCore.nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.UP, Directions.DOWN) } }
        ivArrowDown.setOnClickListener { GameCore.nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.DOWN, Directions.UP) } }
        ivArrowLeft.setOnClickListener { GameCore.nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.LEFT, Directions.RIGHT) } }
        ivArrowRight.setOnClickListener { GameCore.nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.RIGHT, Directions.LEFT) } }
        ivPause.setOnClickListener {
            if (isPlay) {
                ivPause.setImageResource(R.drawable.ic_play)
            } else {
                ivPause.setImageResource((R.drawable.ic_pause))
            }
            isPlay = !isPlay
        }
        //добавил голову на слой
        container.addView(head)
        //сделал начальный отступ от левого грая на 500dpi и верхнего на 200
        (head.layoutParams as FrameLayout.LayoutParams).leftMargin = 500
        (head.layoutParams as FrameLayout.LayoutParams).topMargin = 200
    }

    override fun onStop() {
        super.onStop()
        isPlay = false
        ivPause.setImageResource(R.drawable.ic_play)
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
    override fun onBackPressed() {
        isPlay = false
        ivPause.setImageResource(R.drawable.ic_play)
        exitGame()
    }
    //метод проверка от направлении змейки на себя
    private fun checkIfCurrentDirectionIsNotOpposite(properDirections: Directions, oppositeDirections: Directions){
        if(currentDirection == oppositeDirections){
            move(currentDirection)
        }
        else{
            move(properDirections)
        }
    }
    //метод для генерации фруктов в игре
    private fun generateNewFruit() {
        val viewCoordinate = generateNewFruitCoordinates()
        (fruit.layoutParams as FrameLayout.LayoutParams).leftMargin = viewCoordinate.left
        (fruit.layoutParams as FrameLayout.LayoutParams).topMargin = viewCoordinate.top
        container.removeView(fruit)
        container.addView(fruit)
    }
    //чтобы фрукты не спавнились на теле змейки
    private fun generateNewFruitCoordinates() : ViewCoordinate{
        val viewCoordinate = ViewCoordinate(
                (0 until CELLS_ON_FIELD).random() * HEAD_SIZE,
                (0 until CELLS_ON_FIELD).random() * HEAD_SIZE
        )
        for(partTale in allTale){
            if(partTale.viewCoordinate == viewCoordinate){
                return generateNewFruitCoordinates()
            }
        }
        if(head.top == viewCoordinate.top && head.left ==  viewCoordinate.left){
            return generateNewFruitCoordinates()
        }
        return viewCoordinate
    }
    private fun exitGame(){
        AlertDialog.Builder(this)
                .setTitle("Выйти из игры?")
                .setMessage("Прогресс будет потрачен")
                .setPositiveButton("Да") {_, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("нет") {_, _ ->
                    ivPause.setImageResource((R.drawable.ic_pause))
                    isPlay = true

                }
                .setCancelable(false)
                .create()
                .show()
    }
    //проверка на то сьела ли змея фрукт
    private fun checkIfSnakeEatsFruit() {
        if (head.left == fruit.left && head.top == fruit.top) {
            tvScore.text = "${(allTale.size + 1) * 10}"
            generateNewFruit()
            addTale(head.top, head.left)
            increaseDifficulty()
        }
    }
    //увеличене скорости змеи каждые 5 фруктов
    private fun increaseDifficulty(){
        if(speedSnake <= END_SPEED){
            return
        }
        if(allTale.size % 5 == 0){
            speedSnake -= 100L
        }
    }
    //метод для добавления новой части хвоста в список
    private fun addTale(top:Int, left: Int){
        val talePart =  drawTale(top, left)
        allTale.add(Tale(ViewCoordinate(top, left), talePart))
    }
    //отрисовка тела змеи
    private fun drawTale(top: Int, left: Int): ImageView {
        val taleImage = ImageView(this)
        taleImage.setImageResource(R.drawable.snake_tale)
        taleImage.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        (taleImage.layoutParams as FrameLayout.LayoutParams).topMargin = top
        (taleImage.layoutParams as FrameLayout.LayoutParams).leftMargin = left

        container.addView(taleImage)
        return taleImage
    }

    //метод в котором будем передавать directions
    fun move(directions: Directions) {
        when (directions) {
            Directions.UP -> moveHeadAndRotate(Directions.UP, 180f, -HEAD_SIZE)
            Directions.RIGHT -> moveHeadAndRotate(Directions.RIGHT, 270f, HEAD_SIZE)
            Directions.LEFT -> moveHeadAndRotate(Directions.LEFT, 90f, -HEAD_SIZE)
            Directions.DOWN -> moveHeadAndRotate(Directions.DOWN, 0f, HEAD_SIZE)
        }
        runOnUiThread {
            if(checkIfSnakeSmash()){
                isPlay = false
                val intent = Intent(this, ScoreActivity::class.java)
                val numberPoints = allTale.size * 10
                intent.putExtra("myName", "JimboSnake(YOU)")
                intent.putExtra("myScore", numberPoints.toString())
                startActivity(intent)
                finish()
                return@runOnUiThread
            }
            taleMove()
            checkIfSnakeEatsFruit()
            container.removeView(head)
            container.addView(head)
        }
    }

    private fun moveHeadAndRotate(direction: Directions, angle: Float, coordinates: Int){
        head.rotation = angle
        when(direction){
            Directions.UP, Directions.DOWN -> {(head.layoutParams as FrameLayout.LayoutParams).topMargin += coordinates}
            Directions.LEFT, Directions.RIGHT -> {(head.layoutParams as FrameLayout.LayoutParams).leftMargin += coordinates}
        }
        currentDirection = direction

    }

    private fun checkIfSnakeSmash() : Boolean{
        for(talePart in allTale){
            if(talePart.viewCoordinate.left == head.left && talePart.viewCoordinate.top == head.top){
                return true
            }
        }
        if(head.left < 0 || head.top < 0 || head.top >= HEAD_SIZE * CELLS_ON_FIELD
                || head.left >= HEAD_SIZE * CELLS_ON_FIELD){
            return true
        }
        return false
    }
    private fun taleMove() {
        var tempTale: Tale? = null
        for(index in 0 until allTale.size){
            val talePart = allTale[index]
            container.removeView(talePart.imageView)
            if(index == 0){
                tempTale = talePart
                allTale[index] = Tale(ViewCoordinate(head.top, head.left), drawTale(head.top, head.left ))
            }
            else{
                var anotherTempTale = allTale[index]
                tempTale?.let{
                    allTale[index] = Tale(it.viewCoordinate, drawTale(it.viewCoordinate.top, it.viewCoordinate.left))
                }
                tempTale = anotherTempTale
            }
        }
    }
}

//класс, список заранее известных значений
enum class Directions {
    UP,
    RIGHT,
    LEFT,
    DOWN
}