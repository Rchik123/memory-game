package ge.rchikovani.memory_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var cardViews: MutableList<ImageView>
    private lateinit var successView: TextView
    private lateinit var failView: TextView

    private val cardMap = mutableMapOf<ImageView, Int?>()

    private var successNum = 0
    private var failNum = 0

    private var firstImageClicked: ImageView? = null
    private var secondImageClicked: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardViews = mutableListOf(
            findViewById(R.id.cardImage0),
            findViewById(R.id.cardImage1),
            findViewById(R.id.cardImage2),
            findViewById(R.id.cardImage3),
            findViewById(R.id.cardImage4),
            findViewById(R.id.cardImage5)
        )
        successView = findViewById(R.id.successView)
        failView = findViewById(R.id.failView)

        initGame()

        findViewById<Button>(R.id.restartButton).setOnClickListener {
            initGame()
        }

        for (cardView in cardViews) {
            cardView.setOnClickListener { cardClicked(cardView) }
        }
    }

    private fun initGame(){
        cardMap.clear()
        successNum = 0
        failNum = 0
        firstImageClicked = null
        secondImageClicked = null

        successView.text = getString(R.string.success_text, successNum)
        successView.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray))
        failView.text = getString(R.string.fail_text, failNum)
        failView.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray))

        for (cardView in cardViews) {
            cardView.visibility = View.VISIBLE
            cardView.setImageResource(R.drawable.card_back)
        }

        val intToCard = mapOf(0 to R.drawable.card_jack, 1 to R.drawable.card_queen, 2 to R.drawable.card_king)
        val cardsLeft = mutableMapOf(R.drawable.card_jack to 2, R.drawable.card_queen to 2, R.drawable.card_king to 2)

        for (cardView in cardViews) {
            while(true) {
                val card = intToCard[Random.nextInt(0, 3)]

                if (cardsLeft[card]!! > 0) {
                    cardMap[cardView] = card!!
                    cardsLeft[card]?.let {
                        cardsLeft[card] = it - 1
                    }
                    break
                }
            }
        }
    }

    private fun cardClicked(cardView: ImageView) {
        if (cardView == firstImageClicked || cardView == secondImageClicked) return

        cardView.setImageResource(cardMap[cardView]!!)

        if (firstImageClicked == null && secondImageClicked == null){
            firstImageClicked = cardView
        } else if (firstImageClicked != null && secondImageClicked == null) {
            secondImageClicked = cardView

            if (cardMap[firstImageClicked] == cardMap[secondImageClicked]){
                successNum++
                successView.text = getString(R.string.success_text, successNum)
                successView.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
            } else {
                failNum++
                failView.text = getString(R.string.fail_text, failNum)
                failView.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
            }
        } else {
            if (cardMap[firstImageClicked] == cardMap[secondImageClicked]){
                firstImageClicked?.visibility = View.INVISIBLE
                secondImageClicked?.visibility = View.INVISIBLE
            } else {
                firstImageClicked?.setImageResource(R.drawable.card_back)
                secondImageClicked?.setImageResource(R.drawable.card_back)
            }

            successView.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray))
            failView.setTextColor(ContextCompat.getColor(applicationContext, R.color.gray))

            firstImageClicked = cardView
            secondImageClicked = null
        }
    }
}