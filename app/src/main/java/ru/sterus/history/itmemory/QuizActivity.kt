package ru.sterus.history.itmemory

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.sterus.history.itmemory.databinding.FragmentQuizBinding
import ru.sterus.history.itmemory.fragments.QuizFragment
import ru.sterus.history.itmemory.model.Parser
import ru.sterus.history.itmemory.model.QuizQuestion
import ru.sterus.history.itmemory.model.QuizQuestionOption
import java.util.*

class QuizActivity : AppCompatActivity(), ItemRVOptionClicked {
    lateinit var vb: FragmentQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        vb = FragmentQuizBinding.inflate(layoutInflater)
        val localData : ArrayList<QuizQuestion> = ArrayList()
        var nowQuestionNumber = 0
        vb.startQuizButton.setOnClickListener {
            doAllVisible()
            Collections.sort(localData, QuizFragment.CustomComparator())
            initQuestion(localData[nowQuestionNumber])
        }
        vb.nextQuestionButton.setOnClickListener {
            if (nowQuestionNumber + 2 == localData.size) {
                vb.nextQuestionButton.text = "Завершить квиз"
            }
            if (nowQuestionNumber + 1 < localData.size) {
                nowQuestionNumber += 1
                vb.questionHeader.text = String.format("%d / %d", nowQuestionNumber + 1, localData.size)
                println(nowQuestionNumber.toString() + " " + localData.size)
                initQuestion(localData[nowQuestionNumber])
            } else {
                endQuiz(localData.size)
            }
        }

        val db = Firebase.firestore
        db
            .collection("quiz")
            .get()
            .addOnSuccessListener { result ->
                vb.startQuizButton.visibility = View.VISIBLE
                vb.quizProgressBar.visibility = View.GONE
                for (document in result){
                    println(Parser.parseQuiz(document.data))
                    localData.add(Parser.parseQuiz(document.data))
                }
            }
        setContentView(vb.root)
    }
    private fun endQuiz(size: Int) {
        endQuizVisibility()
    }

    private fun endQuizVisibility() {
        vb.nextQuestionButton.visibility = View.GONE
        vb.recViewQuizQuestions.visibility = View.GONE
        vb.quizQuestion.visibility = View.GONE
        vb.startQuizButton.visibility = View.GONE
        vb.questionHeader.visibility = View.GONE
        vb.endQuizPart1.visibility = View.VISIBLE
        vb.endQuizPart2.visibility = View.VISIBLE
    }

    private fun initQuestion(localQuestion: QuizQuestion) {
        vb.quizQuestion.text = localQuestion.question
        setupQuestionOptions(localQuestion.options)
        vb.nextQuestionButton.visibility = View.GONE
    }

    private fun setupQuestionOptions(options: ArrayList<QuizQuestionOption>) {
        options.shuffle()
        val questionOptionsLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val questionOptionsRVAdapter = RVQuestionOptionsAdapter(options, this, this)
        vb.recViewQuizQuestions.layoutManager = questionOptionsLayoutManager
        vb.recViewQuizQuestions.adapter = questionOptionsRVAdapter

    }

    private fun doAllVisible() {
        vb.startQuizButton.visibility = View.GONE
        vb.introTextView.visibility = View.GONE
        vb.quizQuestion.visibility = View.VISIBLE
        vb.nextQuestionButton.visibility = View.VISIBLE
        vb.questionHeader.visibility = View.VISIBLE
    }
    class CustomComparator : Comparator<QuizQuestion> {
        override fun compare(p0: QuizQuestion?, p1: QuizQuestion?): Int {
            return p0!!.order.compareTo(p1!!.order)
        }
    }

    override fun onItemClicked(q0: QuizQuestionOption, parent: CardView) {
        if (q0.isCorrect){
                vb.nextQuestionButton.visibility = View.VISIBLE
            parent.setCardBackgroundColor(
                ContextCompat.getColor(this,
                R.color.win_green
            ))
        } else {
            parent.setCardBackgroundColor(ContextCompat.getColor(this, R.color.red_bad))
        }
        Snackbar.make(vb.root, q0.feedback, Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).show()
    }
}