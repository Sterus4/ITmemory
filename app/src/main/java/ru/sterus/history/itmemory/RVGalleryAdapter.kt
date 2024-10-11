package ru.sterus.history.itmemory

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sterus.history.itmemory.model.Photos

class RVGalleryAdapter(private val data: ArrayList<Photos>, private val context: Context?) :
    RecyclerView.Adapter<RVGalleryAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageViewPhoto: ImageView = itemView.findViewById(R.id.gallery_photo)
        val textViewTitle: TextView = itemView.findViewById(R.id.gallery_image_title)
        val moreInformationButton : ImageButton = itemView.findViewById(R.id.more_information_photo_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_gallery_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (context != null) {
            Glide.with(context)
                .load(data[position].url)
                .into(holder.imageViewPhoto)
        }
        holder.textViewTitle.text = data[position].title
        println(data[position])
        if (data[position].source == "") {
            holder.moreInformationButton.visibility = View.GONE
        } else {
            val uriSource = Uri.parse(data[position].source)
            val sourceIntent = Intent(Intent.ACTION_VIEW, uriSource)
            holder.moreInformationButton.setOnClickListener { context?.startActivity(sourceIntent) }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}