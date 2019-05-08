package com.example.konnekt.Init

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.konnekt.R
import com.example.konnekt.registerlogin.LoginActivity

class MainActivity : AppCompatActivity() {

    private var mTextView: TextView? = null
    private var mImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTextView = findViewById(R.id.itourtext)
        mImageView = findViewById(R.id.itourLogo)

        val transitionAnimation = AnimationUtils.loadAnimation(this, R.anim.transition)
        mTextView!!.startAnimation(transitionAnimation)
        mImageView!!.startAnimation(transitionAnimation)
        val startloginOrSignUp = Intent(this, LoginActivity::class.java)
        val timer = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    startActivity(startloginOrSignUp)
                    finish()
                }
            }
        }
        timer.start()

    }
}
