package com.example.netflix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflix.R
import com.example.netflix.models.Category

class CategoryAdapter(private val context: Context, private val categoryList: ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(iteamView: View) : RecyclerView.ViewHolder(iteamView) {
        val recyclerView :RecyclerView = iteamView.findViewById(R.id.posters_list)
        val titleText: TextView = iteamView.findViewById(R.id.title_text)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_feed_horizontal_list,
                parent,
                false
            )
        )
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.recyclerView.adapter = FeeDAdapter(context, categoryList[position].category)
        holder.recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerView.setHasFixedSize(true)
        holder.titleText.text = categoryList[position].movieHeading


    }

    override fun getItemCount(): Int {
       return categoryList.size
    }
}