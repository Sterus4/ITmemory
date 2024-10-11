package ru.sterus.history.itmemory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.sterus.history.itmemory.model.ParentData

class RVNewsParentAdapter(private val data: ArrayList<ParentData>, private val context: Context?, private val onItemClickCallBack: ItemRVNewsClicked) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rv_news_parent_layout, parent, false)
            return ParentViewHolder(itemView)
        } else if (viewType == TYPE_HEADER){
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_parent_header, parent, false)
            return HeaderViewHolder(itemView)
        }
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_news_parent_layout, parent, false)
        return ParentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ParentViewHolder) {
            holder.sectionTitleName.text = data[position - 1].parentTitle
            val childLayoutManager = LinearLayoutManager(
                holder.childRecyclerView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            val childAdapter = RVNewsAdapter(data[position - 1].childData, context, onItemClickCallBack)
            holder.childRecyclerView.layoutManager = childLayoutManager
            holder.childRecyclerView.adapter = childAdapter
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0){
            return TYPE_HEADER
        }
        return TYPE_ITEM
    }
    override fun getItemCount(): Int {
        return data.size + 1
    }
    class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sectionTitleName : TextView = itemView.findViewById(R.id.section_title)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.child_recycler_view)
    }
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val pageTitleTextView : TextView = itemView.findViewById(R.id.news_page_header)
    }
}