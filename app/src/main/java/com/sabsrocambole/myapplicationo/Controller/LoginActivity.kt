package com.sabsrocambole.myapplicationo.Controller

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.sabsrocambole.myapplicatione4.R
import com.sabsrocambole.myapplicationo.Services.AuthService



class LoginActivity : AppCompatActivity() {

    var loginEmailTxt = findViewById<TextView>(R.id.loginEmailTxt)
    var loginPasswordText = findViewById<TextView>(R.id.loginPasswordText)
    var loginSpinner = findViewById<ProgressBar>(R.id.loginSpinner)
    var loginLoginBtn = findViewById<Button>(R.id.loginLoginBtn)
    var loginCreateUserBtn = findViewById<Button>(R.id.loginCreateUserBtn)

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }


    fun loginLoginBtnClicked(view: View){

        enableSpinner(true)
        val email = loginEmailTxt.text.toString()
        val password = loginPasswordText.text.toString()

        hideKeyboard()

        if (email.isNotEmpty() && password.isNotEmpty()){
            AuthService.loginUser(this,email, password){loginSuccess ->
                if(loginSuccess){
                    AuthService.findUsersByEmail(this){ findSuccess ->
                        if (findSuccess){
                            enableSpinner(false)
                            finish()
                        }
                        else{
                            errorToast()
                        }
                    }
                }
                else{
                    errorToast()
                }
            }
        }

        else{
            Toast.makeText(this,"Please fill in both e-mail and password",Toast.LENGTH_LONG).show()
        }

    }

    fun loginCreateUserBtnClicked(view: View){
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun errorToast(){
        Toast.makeText(this,"Something went wrong, please try again.",
            Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean){
        if (enable){
            loginSpinner.visibility = View.VISIBLE
        }
        else{
            loginSpinner.visibility = View.INVISIBLE
        }
        loginLoginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE)
        if (inputManager.isAccepitingText){
            inputManager.hidesSoftInputFromWindow(currentFocus.windowToken,0)
        }
    }

}