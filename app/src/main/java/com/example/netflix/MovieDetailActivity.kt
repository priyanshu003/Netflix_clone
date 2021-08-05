package com.example.netflix

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.netflix.models.DetailVideo
import com.example.netflix.models.MovieList
import com.example.netflix.utils.Utils
import com.example.netflix.volley.WebApiRequest
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class MovieDetailActivity:AppCompatActivity() {
    lateinit var progress:LinearLayout
    lateinit var linearLayout: LinearLayout
    lateinit var bgImage : ImageView
    lateinit var titleText :TextView
    lateinit var year :TextView
    lateinit var hdText:TextView
    lateinit var rating :TextView
    lateinit var overviewText :TextView
    lateinit var mList :MovieList.Result
    lateinit var toolbar: Toolbar
    lateinit var playbtnContainer : FrameLayout
    lateinit var playbtn : ImageView
    lateinit var loader : CircularProgressIndicator
    private var pageNumber = 1
    lateinit var youTubePlayerView : YouTubePlayerView
    lateinit var movieId : String
    lateinit var thubnail : FrameLayout
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        movieId = ""

        // findview by it
        linearLayout = findViewById(R.id.container)
        progress = findViewById(R.id.loader)
        progress.visibility = View.VISIBLE

        bgImage = findViewById(R.id.backdrop_image)
        titleText = findViewById(R.id.title_text_header)
        year = findViewById(R.id.year_text_header)
        hdText = findViewById(R.id.hd_text)
        rating = findViewById(R.id.rating_text)
        overviewText = findViewById(R.id.overview_text)
        toolbar = findViewById(R.id.toolbar)
        loader = findViewById(R.id.video_loader)
        playbtnContainer  = findViewById(R.id.play_container)
        playbtn = findViewById(R.id.play_btn)
        thubnail = findViewById(R.id.thumbnail)
        youTubePlayerView = findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView);
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener{
            super.onBackPressed()
        }



        //Getting Data
        mList  = intent.getSerializableExtra("info") as MovieList.Result
        System.out.println("hiiiiiiiiiiiiiiii----------------------------------------------------" + mList.title);
       // Toast.makeText(this, mList.title, Toast.LENGTH_LONG).show()


        setContent()


        playbtnContainer.setOnClickListener {

            if(movieId == ""){
                Toast.makeText(this,"No Teasear Available" ,Toast.LENGTH_LONG).show()
            }else{
                System.out.println(movieId)
                playbtn.visibility = View.GONE
                loader.visibility = View.VISIBLE
                playVideo()
            }

        }


    }

    private fun playVideo() {

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                thubnail.visibility = View.GONE
                youTubePlayerView.visibility = View.VISIBLE
                val videoId = movieId.toString()
                youTubePlayer.loadVideo(videoId, 0f)

            }
        })

    }

    private fun setContent() {
        progress.visibility = View.GONE
        val url = Constants.BASE_URL_IMAGE_ORIGINAL + mList.backdropPath
        Glide.with(this).load(url).into(bgImage)
        titleText.text = mList.title
        year.text = mList.releaseDate
        hdText.text = "HD"
        rating.text = mList.voteAverage.toString()
        overviewText.text = mList.overview

        getMovieById()
    }

    private fun getMovieById() {
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(
                    this,
                    "No internet ..Please connect to internet and start app again",
                    Toast.LENGTH_SHORT
            ).show()

        }

        val ws_url: String = Constants.BASE_URL_APPLICATION + Constants.MOVIE + mList.id.toString() + "/" +  Constants.VIDEO +
                "?api_key=" + Constants.API_KEY.toString() + "&language=en-US&page=" + pageNumber

        println(ws_url)

        val webApiRequest = WebApiRequest(
                Request.Method.GET,
                ws_url,
                ReqSuccessListener() as Response.Listener<String>?,
                ReqErrorListener() as Response.ErrorListener?
        )
        Volley.newRequestQueue(this).add(webApiRequest)
    }

    /**
     * Success listener to handle the movie listing
     * process after api returns the movie list
     *
     * @return
     */
    private fun ReqSuccessListener(): Any {
        Log.e(ContentValues.TAG, "list empty==")
        return Response.Listener<String?> { response ->
            Log.e("movie list_response", response)
            try {
                  //hideProgress()
//                val obj = JSONObject(response)
//                movieId = obj.get("Key") as String
//                System.out.println("-------------------------"+movieId)
                pageNumber++

                val movieListDetail = Utils.jsonToPojo(
                        response,
                        DetailVideo::class.java
                ) as DetailVideo

                println(movieListDetail)
                if (movieListDetail.results.isNotEmpty()) {
                        movieId = movieListDetail.results[0].key

                    }else{
                    Log.e(ContentValues.TAG, "list empty==")
                }


                }

                catch (e: Exception) {
                Log.e(ContentValues.TAG, "Exception==" + e.localizedMessage)
            }
        }


    }


    /**
     * To Handle the error
     *
     * @return
     */
    private fun ReqErrorListener(): Any {
        return Response.ErrorListener {
            Log.e("volley error", "volley error")
            Toast.makeText(
                    this, "" +
                    "Server Error..Please try again after sometime", Toast.LENGTH_SHORT
            ).show()
        }
    }
}