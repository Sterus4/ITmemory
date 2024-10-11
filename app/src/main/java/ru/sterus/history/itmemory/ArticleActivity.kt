package ru.sterus.history.itmemory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sterus.history.itmemory.databinding.ActivityArticleBinding
import ru.sterus.history.itmemory.fragments.NewsFragment
import ru.sterus.history.itmemory.model.Article


class ArticleActivity : AppCompatActivity() {
    private lateinit var vb: ActivityArticleBinding
    companion object {
        private const val ERROR_MESSAGE = "Sorry, Article not found. Have a nice day :-)"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        vb = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.articleBackButton.setOnClickListener { finish() }
        val localArticle = intent.getParcelableExtra<Article>(NewsFragment.TAG)
        if (localArticle != null){
            setupScreen(localArticle)
        } else {
            vb.articleNameTextView.text = ERROR_MESSAGE
        }
    }

    private fun setupScreen(article: Article){
        vb.articleNameTextView.text = article.title
        Glide.with(this)
            .load(article.image)
            .into(vb.articleImageImageView)
        vb.articleContent.text = Html.fromHtml(article.content)
        val uriYandex = Uri.parse(article.yandexMapsURl)
        val mapIntent = Intent(Intent.ACTION_VIEW, uriYandex)
        vb.yandexMapButton.setOnClickListener { startActivity(mapIntent) }
        val uriWiki = Uri.parse(article.wikipediaURL)
        val wikiIntent = Intent(Intent.ACTION_VIEW, uriWiki)
        vb.wikiInfoButton.setOnClickListener { startActivity(wikiIntent) }
        if (article.photos.size == 0){
            vb.galleryTextView.visibility = View.GONE
        } else {
            val rvGalleryAdapter = RVGalleryAdapter(article.photos, this)
            setupRecyclerView(rvGalleryAdapter)
        }
    }

    private fun setupRecyclerView(rvGalleryAdapter: RVGalleryAdapter) {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        vb.galleryRecView.layoutManager = linearLayoutManager
        vb.galleryRecView.adapter = rvGalleryAdapter

    }


}