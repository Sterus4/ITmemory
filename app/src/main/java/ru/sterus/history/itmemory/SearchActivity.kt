package ru.sterus.history.itmemory

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.sterus.history.itmemory.databinding.ActivitySearchBinding
import ru.sterus.history.itmemory.fragments.NewsFragment
import ru.sterus.history.itmemory.model.Article
import ru.sterus.history.itmemory.model.CustomSearchArticle
import ru.sterus.history.itmemory.model.Parser
import kotlin.math.min

class SearchActivity : AppCompatActivity(), ItemRVNewsClicked {
    val allArticles = ArrayList<Article>()
    val currentArticles = ArrayList<CustomSearchArticle>()
    private lateinit var vb : ActivitySearchBinding
    val indexesCount = 40
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySearchBinding.inflate(layoutInflater)
        val adapter = SearchNewsDataAdapter(currentArticles, this, this)
        setupRecView(adapter)
        val db = Firebase.firestore
        db
            .collection("articles")
            .get()
            .addOnSuccessListener {result ->
                for (document in result){
                    val gallery = Parser.parsePhotos(document.data)
                    val art = Parser.parseArticle(document.data, gallery)
                    allArticles.add(art)
                }
                //adapter.notifyDataSetChanged()
            }.addOnFailureListener{
                vb.failToFindInformation.visibility = View.VISIBLE
                print(it.stackTrace)
            }

        vb.searchEditText.doOnTextChanged{ text, start, count, after ->
            currentArticles.clear()
            println(text)
            println(allArticles)
            currentArticles.addAll(findArticlesByKeyWord(text.toString()))
            if (currentArticles.isNotEmpty()){
                adapter.notifyDataSetChanged()
                vb.failToFindInformation.visibility = View.GONE
            } else {
                adapter.notifyDataSetChanged()
                vb.failToFindInformation.visibility = View.VISIBLE
            }

        }
        setContentView(vb.root)
    }

    private fun setupRecView(adapter: SearchNewsDataAdapter) {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        vb.searchNewsRecyclerView.layoutManager = linearLayoutManager
        vb.searchNewsRecyclerView.adapter = adapter
    }

    private fun findArticlesByKeyWord(text: String): Collection<CustomSearchArticle> {
        val localArticles = ArrayList<CustomSearchArticle>()
        if (text == ""){
            return localArticles
        }
        for (article in allArticles){
            val subStr = text.lowercase()
            val str = Html.fromHtml(article.content).toString().lowercase()
            val realText = Html.fromHtml(article.content).toString()
            if (subStr in article.title.lowercase() || subStr in getSection(article.section)){
                val correctInfo = (realText.substring(0, min(80, realText.length) - 1).replace("\n"," "))
                localArticles.add(CustomSearchArticle(article, correctInfo,0, 0))
                continue
            }
            if (subStr in article.date.lowercase()){
                val correctInfo = article.date
                localArticles.add(CustomSearchArticle(article, correctInfo,0, 0))
                continue
            }
            if (subStr in str){
                val firstIndex = str.indexOf(subStr)
                println(article.title)
                println(firstIndex)
                println()
                if (realText.length <= 2 * indexesCount + 10){
                    localArticles.add(CustomSearchArticle(article, realText, 0, 0))
                } else if (firstIndex <= indexesCount){
                    val correctInfo = (realText.substring(0, firstIndex + subStr.length + indexesCount) + "...").replace("\n"," ")
                    localArticles.add(CustomSearchArticle(article, correctInfo,correctInfo.lowercase().indexOf(subStr), correctInfo.lowercase().indexOf(subStr) + subStr.length))
                } else if (firstIndex >= str.length - indexesCount){
                    val correctInfo = ("..." + realText.substring(firstIndex - indexesCount, realText.length - 1)).replace("\n"," ")
                    localArticles.add(CustomSearchArticle(article, correctInfo, correctInfo.lowercase().indexOf(subStr), correctInfo.lowercase().indexOf(subStr) + subStr.length))
                } else {
                    val correctInfo = ("..." + realText.substring(firstIndex - indexesCount, firstIndex + subStr.length + indexesCount - 4) + "...").replace("\n"," ")
                    localArticles.add(CustomSearchArticle(article, correctInfo, correctInfo.lowercase().indexOf(subStr), correctInfo.lowercase().indexOf(subStr) + subStr.length))
                }
            }
        }
        return localArticles
    }

    private fun getSection(section: String): String {
        return when(section){
            "kronv" -> "Кронверкский"
            "birzh" -> "Биржевая линия Биржа"
            "center" -> "Центр"
            "vyazyma" -> "Вяземский переулок Вязьма"
            else -> {""}
        }
    }

    override fun onItemClicked(article: Article) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra(NewsFragment.TAG, article)
        startActivity(intent)
    }
}