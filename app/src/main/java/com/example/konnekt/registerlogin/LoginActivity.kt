package com.example.konnekt.registerlogin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.konnekt.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    private var emailAsk: EditText? = null
    private var passwordAsk: EditText? = null
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailAsk = findViewById(R.id.loginemailPrompt)
        passwordAsk = findViewById(R.id.signupemailPrompt)

        signUp.setOnClickListener {
            val gotoSignup = Intent(this, RegisterActivity::class.java)
            startActivity(gotoSignup)
        }
    }

}





