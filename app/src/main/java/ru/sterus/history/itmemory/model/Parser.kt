package ru.sterus.history.itmemory.model

class Parser {
    companion object{
        fun parseArticle(task: MutableMap<String, Any>, photos: ArrayList<Photos>) : Article{
            return Article(
                task["title"].toString(),
                task["photo"].toString(),
                task["content"].toString(),
                task["date"].toString(),
                task["description"].toString(),
                task["wikipedia"].toString(),
                task["yandexMapsUrl"].toString(),
                task["section"].toString(),
                photos
            )
        }
        fun parsePhoto(task: MutableMap<String, Any>) : Photos{
            return Photos(
                task["description"].toString(),
                task["source"].toString(),
                task["title"].toString(),
                task["url"].toString()
            )
        }
        fun parsePhotos(task: MutableMap<String, Any>) : ArrayList<Photos>{
            val gal = ArrayList<Photos>()
            val localTask = task["photos"] as ArrayList<*>
            for (i in localTask){
                gal.add(parsePhoto(i as MutableMap<String, Any>))
            }
            return gal
        }

        private fun parseQuizQuestionOption(task: MutableMap<String, Any>) : QuizQuestionOption{
            return QuizQuestionOption(task["feedback"].toString(), task["text"].toString(), task["isCorrect"] as Boolean, task["image"].toString())
        }
        fun parseQuiz(task: MutableMap<String, Any>): QuizQuestion{
            val options = ArrayList<QuizQuestionOption>()
            val localTask = task["options"] as ArrayList<*>
            for (i in localTask){
                options.add(parseQuizQuestionOption(i as MutableMap<String, Any>))
            }
            return QuizQuestion(
                task["question"].toString(),
                task["order"] as Long,
                options)
        }
        fun parseMarker(task: MutableMap<String, Any>) : MapsMarker {
            return MapsMarker(task["name"].toString(), task["latLng"] as ArrayList<Long>, task["image"].toString())
        }
    }
}