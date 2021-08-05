package com.example.netflix.adapters

import android.app.PendingIntent.getActivity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.netflix.Constants
import com.example.netflix.OnMovieItemClickListener
import com.example.netflix.R
import com.example.netflix.models.MovieList
import com.google.android.material.bottomsheet.BottomSheetDialog


class FeeDAdapter(private val listener: OnMovieItemClickListener) : RecyclerView.Adapter<FeeDAdapter.ViewHolder>() {

    private  val moviesList: ArrayList<MovieList.Result?> = ArrayList()

    class ViewHolder(iteamView: View) : RecyclerView.ViewHolder(iteamView) {
        //val titleText:TextView = iteamView.findViewById(R.id.title_text)
        val image:ImageView = iteamView.findViewById(R.id.poster_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                        R.layout.item_poster,
                        parent,
                        false
                )
        val viewHolder  = ViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(moviesList[viewHolder.adapterPosition]!!)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = Constants.BASE_URL_IMAGE_ORIGINAL+ moviesList.get(position)?.posterPath

        Glide.with(holder.image.context).load(url).into(holder.image)
        System.out.println("url--------------------->" + url);


    }

    fun addMovies(movies: List<MovieList.Result?>?) {
        moviesList.addAll(movies!!)
        Log.e(TAG, "size of movie list==" + moviesList.size)
       // System.out.println("this --------------------------------------->"+movies)
        notifyDataSetChanged()


//       val one = Category(Constants.TOP_RATED,moviesList)
//        val two = Category(Constants.TOP_RATED,moviesList)
//        val three = Category(Constants.TOP_RATED,moviesList)
//        subjects.add(one)
//        subjects.add(two)
//        subjects.add(three)
    }

    override fun getItemCount(): Int {
        return moviesList.size

    }
}
//interface OnMovieItemClickListener{
//    fun onItemClicked(item: MovieList.Result)
//}