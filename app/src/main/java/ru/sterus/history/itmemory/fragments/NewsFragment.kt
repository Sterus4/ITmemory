package ru.sterus.history.itmemory.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.sterus.history.itmemory.ArticleActivity
import ru.sterus.history.itmemory.ItemRVNewsClicked
import ru.sterus.history.itmemory.RVNewsParentAdapter
import ru.sterus.history.itmemory.SearchActivity
import ru.sterus.history.itmemory.databinding.FragmentNewsBinding
import ru.sterus.history.itmemory.model.Article
import ru.sterus.history.itmemory.model.ParentData
import ru.sterus.history.itmemory.model.Parser

class NewsFragment : Fragment(), ItemRVNewsClicked {
    private lateinit var vb: FragmentNewsBinding
    private var data = ArrayList<Article>()
    private var parentData = ArrayList<ParentData>()
    companion object {
        const val TAG = "EXTRA_TAG"
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = FragmentNewsBinding.inflate(inflater, container, false)
        data = ArrayList()
        parentData = ArrayList()
        val searchIntent = Intent(activity, SearchActivity::class.java)
        vb.fab.setOnClickListener{ startActivity(searchIntent) }
        val parentAdapter = RVNewsParentAdapter(parentData, context, this)
        setupParentRecyclerView(parentAdapter)
        val db = Firebase.firestore

        db
            .collection("articles")
            .get()
            .addOnSuccessListener {result ->
                vb.newsProgressBar.visibility = View.GONE
                val dataK = ArrayList<Article>()
                val dataB = ArrayList<Article>()
                val dataC = ArrayList<Article>()
                val dataV = ArrayList<Article>()
                for (document in result){
                    val gallery = Parser.parsePhotos(document.data)
                    val art = Parser.parseArticle(document.data, gallery)
                    when(art.section){
                        "kronv" -> dataK
                        "birzh" -> dataB
                        "center" -> dataC
                        "vyazyma" -> dataV
                        else -> {ArrayList()}
                    }.add(art)
                }
                parentData.add(ParentData("Кронверкский", dataK))
                parentData.add(ParentData("Биржа", dataB))
                parentData.add(ParentData("Центр", dataC))
                parentData.add(ParentData("Вязьма", dataV))
                parentAdapter.notifyDataSetChanged()
            }.addOnFailureListener{
                vb.newsProgressBar.visibility = View.GONE
                print(it.stackTrace)
            }
        return vb.root
    }

    private fun setupParentRecyclerView(parentAdapter: RVNewsParentAdapter) {
        val parentLinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        vb.newsParentRecyclerView.layoutManager = parentLinearLayoutManager
        vb.newsParentRecyclerView.adapter = parentAdapter
    }
    override fun onItemClicked(article: Article) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra(TAG, article)
        startActivity(intent)
    }
}