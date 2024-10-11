package ru.sterus.history.itmemory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sterus.history.itmemory.model.QuizQuestionOption

class RVQuestionOptionsAdapter(private val data: ArrayList<QuizQuestionOption>, private val context: Context?, private val onItemClickCallBack: ItemRVOptionClicked) : RecyclerView.Adapter<RVQuestionOptionsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rec_view_quiz_question, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if ("://" in data[position].image){
            holder.optionImageView.visibility = View.VISIBLE
            if (context != null) {
                Glide.with(context)
                    .load(data[position].image)
                    .into(holder.optionImageView)
            }
            holder.optionImageView.setOnClickListener{onItemClickCallBack.onItemClicked(data[position], holder.frame)}
        } else {
            holder.optionTextView.text = data[position].text
            holder.frame.setOnClickListener{onItemClickCallBack.onItemClicked(data[position], holder.frame)}
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val optionTextView : TextView = itemView.findViewById(R.id.quiz_question_option)
        val optionImageView: ImageView = itemView.findViewById(R.id.quiz_question_option_image)
        val frame: CardView = itemView.findViewById(R.id.frame_for_option)
    }


}