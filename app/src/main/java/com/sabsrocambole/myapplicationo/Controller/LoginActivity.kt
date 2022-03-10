package com.sabsrocambole.myapplicationo.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.sabsrocambole.myapplicatione4.R
import com.sabsrocambole.myapplicationo.Services.AuthService



class LoginActivity : AppCompatActivity() {

    var loginEmailTxt = findViewById<TextView>(R.id.loginEmailTxt)
    var loginPasswordText = findViewById<TextView>(R.id.loginPasswordText)

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun loginLoginBtnClicked(view: View){
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordText.text.toString()

        AuthService.loginUser(this,email, password){loginSuccess ->
            if(loginSuccess){
                AuthService.findUsersByEmail(this){ findSuccess ->
                    finish()
                }
            }
        }
    }

    fun loginCreateUserBtnClicked(view: View){
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }
}