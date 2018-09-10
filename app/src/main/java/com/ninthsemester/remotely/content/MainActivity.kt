package com.ninthsemester.remotely.content

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ninthsemester.remotely.R
import com.ninthsemester.remotely.content.sources.unsplash.Unsplash

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val swapi = Swapi(this)
        val unsplash = Unsplash()
        unsplash.topPicksOfTheDay({
            Log.d(TAG, it.size.toString())
        }, {
            Log.d(TAG, it.toString())
        })

    }

    companion object {
        const val TAG = "Main : ";
    }
}
