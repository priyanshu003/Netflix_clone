package com.example.netflix

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.netflix.Constants.API_KEY
import com.example.netflix.Constants.BASE_URL_APPLICATION
import com.example.netflix.Constants.MOVIE
import com.example.netflix.Constants.NOW
import com.example.netflix.Constants.POPULAR_MOVIES
import com.example.netflix.Constants.TOP_RATED
import com.example.netflix.Constants.UPCOMING
import com.example.netflix.adapters.*
import com.example.netflix.models.MovieList
import com.example.netflix.utils.Utils
import com.example.netflix.volley.WebApiRequest
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.Serializable


class MainActivity : AppCompatActivity(),OnMovieItemClickListener{
    lateinit var rvMoviesList: RecyclerView
    lateinit var rvMoviesList1: RecyclerView
    lateinit var rvMoviesList2: RecyclerView
    lateinit var rvMoviesList3: RecyclerView
    lateinit var shimmer: ShimmerFrameLayout
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var linearLayoutManager1: LinearLayoutManager
    lateinit var linearLayoutManager2: LinearLayoutManager
    lateinit var linearLayoutManager3: LinearLayoutManager
    lateinit var feedAdapter:FeeDAdapter
    lateinit var feedAdapter1:FeedAdapterOne
    lateinit var feedAdapter2:FeedAdaptertwo
    lateinit var feedAdapter3:FeedAdapterthree
    lateinit var bgimage:ImageView
    lateinit var tText: TextView
    lateinit var yearT :TextView
    lateinit var runT : TextView
    lateinit var cut :ImageView
    lateinit var des :TextView
    lateinit var detailButton : LinearLayout
    lateinit var sheetView :View

    lateinit var text:TextView
    lateinit var text1:TextView
    lateinit var text2:TextView
    lateinit var text3:TextView
    //For Load more functionality
//    private val previousTotal = 0
//    private val loading = true
//    private val visibleThreshold = 2
//    private val firstVisibleItem = 0
//    private val visibleItemCount = 0
//    private val totalItemCount = 0


    lateinit var pos_image :ImageView

    //current page number
    private var pageNumber = 1


    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
       // SystemClock.sleep(7000)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_feed_horizontal_list)

        text = findViewById(R.id.title_text)
        text1 = findViewById(R.id.title_text_1)
        text2 = findViewById(R.id.title_text_2)
        text3 = findViewById(R.id.title_text_3)
        text.text = "Top Rated"

        text1.text = "Popular On Netflix"
        text2.text = "Upcoming Hits"
        text3.text = "Now Straeming"
        shimmer = findViewById(R.id.loader)


        sheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)


        pos_image = sheetView.findViewById(R.id.poster_image_bottom)
        tText = sheetView.findViewById(R.id.title_text_bottom)
        yearT = sheetView.findViewById(R.id.year_text_bottom)
        runT = sheetView.findViewById(R.id.runtime_text_bottom)
        cut = sheetView.findViewById(R.id.close_icon_bottom)
        des = sheetView.findViewById(R.id.overview_text_bottom)
        detailButton = sheetView.findViewById(R.id.details_button_bottom)

        bgimage = findViewById(R.id.background_image)


        rvMoviesList = findViewById(R.id.posters_list)
        rvMoviesList1 = findViewById(R.id.posters_list_1)
        rvMoviesList2 = findViewById(R.id.posters_list_2)
        rvMoviesList3 = findViewById(R.id.posters_list_3)
        feedAdapter = FeeDAdapter(this)
        feedAdapter1 = FeedAdapterOne(this)
        feedAdapter2 = FeedAdaptertwo(this)
        feedAdapter3 = FeedAdapterthree(this)
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        linearLayoutManager1 = LinearLayoutManager(this)
        linearLayoutManager1.orientation = LinearLayoutManager.HORIZONTAL

        linearLayoutManager2 = LinearLayoutManager(this)
        linearLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        linearLayoutManager3 = LinearLayoutManager(this)
        linearLayoutManager3.orientation = LinearLayoutManager.HORIZONTAL

        rvMoviesList.layoutManager = linearLayoutManager
        rvMoviesList1.layoutManager = linearLayoutManager1
        rvMoviesList2.layoutManager = linearLayoutManager2

        rvMoviesList3.layoutManager = linearLayoutManager3
        rvMoviesList.setHasFixedSize(true)
        rvMoviesList1.setHasFixedSize(true)
        rvMoviesList2.setHasFixedSize(true)
        rvMoviesList3.setHasFixedSize(true)
        rvMoviesList.adapter = feedAdapter
        rvMoviesList1.adapter = feedAdapter1
        rvMoviesList2.adapter = feedAdapter2
        rvMoviesList3.adapter = feedAdapter3

        callGetTopRatedMoviesApi("trend")
        callGetTopRatedMoviesApi("popular")
        callGetTopRatedMoviesApi("upcoming")
        callGetTopRatedMoviesApi("now")



    }

    private fun View?.removeSelf() {
        this ?: return
        val parentView = parent as? ViewGroup ?: return
        parentView.removeView(this)
    }

    fun showBackIcon(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                        ContextCompat.getColor(
                                this,
                                R.color.black
                        )
                )
        )
    }

    private fun callGetTopRatedMoviesApi(type: String) {
        /**
         * Checking internet connection before api call.
         * Very important always take care.
         */

        shimmer.startShimmer()
        shimmer.visibility = View.VISIBLE
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(
                    this,
                    "No internet ..Please connect to internet and start app again",
                    Toast.LENGTH_SHORT
            ).show()

        }
      //  showProgress()


        //constructing api url
        if(type.equals("trend")){
//            shimmer.startShimmer()
//            shimmer.visibility = View.VISIBLE
            val ws_url: String = BASE_URL_APPLICATION + MOVIE + TOP_RATED.toString() +
                    "?api_key=" + API_KEY.toString() + "&language=en-US&page=" + pageNumber

            //Using Volley to call api
            val webApiRequest = WebApiRequest(
                    Request.Method.GET,
                    ws_url,
                    ReqSuccessListener(type) as Response.Listener<String>?,
                    ReqErrorListener() as Response.ErrorListener?
            )
            Volley.newRequestQueue(this).add(webApiRequest)
        }else if (type.equals("upcoming")){
//            shimmer.startShimmer()
//            shimmer.visibility = View.VISIBLE
            val ws_url: String = BASE_URL_APPLICATION + MOVIE + UPCOMING.toString() +
                    "?api_key=" + API_KEY.toString() + "&language=en-US&page=" + pageNumber

            //Using Volley to call api
            val webApiRequest = WebApiRequest(
                    Request.Method.GET,
                    ws_url,
                    ReqSuccessListener(type) as Response.Listener<String>?,
                    ReqErrorListener() as Response.ErrorListener?
            )
            Volley.newRequestQueue(this).add(webApiRequest)
        }else if(type.equals("now")){

            val ws_url: String = BASE_URL_APPLICATION + MOVIE + NOW.toString() +
                    "?api_key=" + API_KEY.toString() + "&language=en-US&page=" + pageNumber

            //Using Volley to call api
            val webApiRequest = WebApiRequest(
                    Request.Method.GET,
                    ws_url,
                    ReqSuccessListener(type) as Response.Listener<String>?,
                    ReqErrorListener() as Response.ErrorListener?
            )
            Volley.newRequestQueue(this).add(webApiRequest)

        }
        else{
//            shimmer.startShimmer()
//            shimmer.visibility = View.VISIBLE
            val ws_url: String = BASE_URL_APPLICATION + MOVIE + POPULAR_MOVIES.toString() +
                    "?api_key=" + API_KEY.toString() + "&language=en-US&page=" + pageNumber

            //Using Volley to call api
            val webApiRequest = WebApiRequest(
                    Request.Method.GET,
                    ws_url,
                    ReqSuccessListener(type) as Response.Listener<String>?,
                    ReqErrorListener() as Response.ErrorListener?
            )
            Volley.newRequestQueue(this).add(webApiRequest)


        }





    }

    /**
     * Success listener to handle the movie listing
     * process after api returns the movie list
     *
     * @return
     */
    private fun ReqSuccessListener(option: String): Any {
                        shimmer.stopShimmer()
                        shimmer.visibility = View.GONE
        Log.e(TAG, "list empty==")
        return Response.Listener<String?> { response ->
            Log.e("movie list_response", response)
            try {
              //  hideProgress()
                pageNumber++
                val movieListModel = Utils.jsonToPojo(
                        response,
                        MovieList::class.java
                ) as MovieList
                if (movieListModel.results!!.isNotEmpty()
                ) {
                    if(option == "trend"){
                        val rnds = (0..8).random()
                        val url = Constants.BASE_URL_IMAGE_ORIGINAL + movieListModel.results[rnds].posterPath
                        Glide.with(this).load(url).into(bgimage)
                        feedAdapter.addMovies(movieListModel.results)
                    }else if(option.equals("upcoming")){
//                        shimmer.stopShimmer()
//                        shimmer.visibility = View.GONE
                        feedAdapter2.addMovies(movieListModel.results)
                    }else if(option == "now"){
                        feedAdapter3.addMovies(movieListModel.results)
                    }
                    else{
//                        shimmer.stopShimmer()
//                        shimmer.visibility = View.GONE
                        feedAdapter1.addMovies(movieListModel.results)
                    }

                } else {
                    Log.e(TAG, "list empty==")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception==" + e.localizedMessage)
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



    override fun onItemClicked(item: MovieList.Result) {
//        Toast.makeText(this, item.originalTitle,Toast.LENGTH_LONG).show()


        val mBottomSheetDialog = BottomSheetDialog(this@MainActivity)

        sheetView.removeSelf()


        tText.text = item.originalTitle
        yearT.text = item.releaseDate
        des.text = item.overview
        val url = Constants.BASE_URL_IMAGE_ORIGINAL+ item.posterPath
        Glide.with(this).load(url).into(pos_image)
        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()

        cut.setOnClickListener{
            mBottomSheetDialog.dismiss()
        }

        detailButton.setOnClickListener{
//
//            val intent = Intent(this, MovieDetailActivity::class.java)
//            val arrayGuide = ArrayList<Any>()
//            arrayGuide.add(item)
//            intent.putIntegerArrayListExtra("info",arrayGuide)
//            startActivity(intent)

            val dataMovie : MovieList.Result = item
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("info",dataMovie)
            startActivity(intent)
            mBottomSheetDialog.dismiss()

        }

    }





}