package ru.sterus.history.itmemory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sterus.history.itmemory.model.Article

class RVNewsAdapter(private val data: ArrayList<Article>, private val context: Context?, private val onItemClickCallBack: ItemRVNewsClicked) : RecyclerView.Adapter<RVNewsAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_news_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (data[position].image.isNotEmpty()){
            if (context != null) {
                Glide
                    .with(context)
                    .load(data[position].image)
                    .into(holder.imageViewArticle)
            }
        } else {
            holder.imageViewArticle.setImageResource(R.drawable.ic_baseline_photo_camera_24)
        }
        if (data[position].title.isNotEmpty()){
            holder.newsNameOfTheArticle.text = data[position].title
        } else {
            holder.newsNameOfTheArticle.text = "Title not found"
        }
        setOnClickForItem(holder, data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun setOnClickForItem(holder: MyViewHolder, article: Article){
        holder.imageViewArticle.setOnClickListener{ onItemClickCallBack.onItemClicked(article) }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageViewArticle: ImageView = itemView.findViewById(R.id.image_view_article)
        val newsNameOfTheArticle : TextView = itemView.findViewById(R.id.news_name_of_the_article)
    }
}