package ru.sterus.history.itmemory.model

data class QuizQuestion(val question: String, val order: Long, val options: ArrayList<QuizQuestionOption>)
