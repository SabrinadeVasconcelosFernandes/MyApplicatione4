package com.sabsrocambole.myapplicationo.Services

import android.app.DownloadManager
import android.content.Context
import android.util.Log
import com.sabsrocambole.myapplicationo.Utilities.URL_LOGIN
import com.sabsrocambole.myapplicationo.Utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method

object AuthService {

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){

        val url = URL_REGISTER
        val jsonBody = JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { _ ->
            complete(true)},
            Response.ErrorListener {error ->
                Log.d("ERROR","Could not register user: $error")
                complete(false)
            })
        {
            override fun getBodyContextType(): String{
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray{
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }
    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){
        val jsonBody = JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        val requestBody = jsonBody.toString()

        val loginRequest = object:JSONObjectRequest(Method.POST, URL_LOGIN,null, Response.Listener{response->

            try {

                userEmail = response.getString("user")
                authToken= response.getString("token")
                isLoggedIn = true
                complete(true)

            } catch (e: JSONException){
                Log.d("JSON","EXC:" + e.localizedMessage)
                complete(false)
            }
        },
        Response.ErrorListener{ error->
            //where we deal with our errors
            Log.d("ERROR","Could not register user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String{
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray{
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(loginRequest)
    }
}