package ru.sterus.history.itmemory

import androidx.cardview.widget.CardView
import ru.sterus.history.itmemory.model.QuizQuestionOption

interface ItemRVOptionClicked {
    fun onItemClicked(q0: QuizQuestionOption, parent: CardView)
}