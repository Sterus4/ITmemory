package ru.sterus.history.itmemory.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.sterus.history.itmemory.QuizActivity
import ru.sterus.history.itmemory.SearchActivity
import ru.sterus.history.itmemory.databinding.FragmentServiceBinding

class ServiceFragment : Fragment() {
    lateinit var vb: FragmentServiceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentServiceBinding.inflate(layoutInflater, container, false)
        vb.quizButton.setOnClickListener {
            val quizIntent = Intent(activity, QuizActivity::class.java)
            startActivity(quizIntent)
        }
        vb.searchButtonSection.setOnClickListener {
            val searchIntent = Intent(activity, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        return vb.root
    }
}