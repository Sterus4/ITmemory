package ru.sterus.history.itmemory

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.sterus.history.itmemory.model.CustomSearchArticle

class SearchNewsDataAdapter(private val data: ArrayList<CustomSearchArticle>, private val context: Context?, private val onItemClickCallBack: ItemRVNewsClicked) :
    RecyclerView.Adapter<SearchNewsDataAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.search_title)
        val infoSearchTextView : TextView = itemView.findViewById(R.id.search_item_info)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_search_layout, parent, false)
        return SearchNewsDataAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val strTitle = SpannableStringBuilder(data[position].article.title)
        strTitle.setSpan(StyleSpan(Typeface.BOLD), 0, data[position].article.title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val strInfo = SpannableStringBuilder(data[position].info)
        strInfo.setSpan(StyleSpan(Typeface.BOLD), data[position].start, data[position].end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.titleTextView.text = strTitle
        holder.infoSearchTextView.text = strInfo
        holder.titleTextView.setOnClickListener { onItemClickCallBack.onItemClicked(data[position].article) }
        holder.infoSearchTextView.setOnClickListener { onItemClickCallBack.onItemClicked(data[position].article) }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}