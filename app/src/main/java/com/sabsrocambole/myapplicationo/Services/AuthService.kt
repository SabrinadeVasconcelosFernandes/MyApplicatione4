package com.sabsrocambole.myapplicationo.Services

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.util.JsonReader
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sabsrocambole.myapplicationo.Utilities.*
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
    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit){
        val jsonBody = JSONObject()
        jsonBody.put("name",name)
        jsonBody.put("email",email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val createRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER,null,Response.Listener{response ->

            try {

                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("id")
                complete(true)

            } catch (e: JSONException){
                Log.d("JSON","EXC"+e.localizedMessage)
            }

        },Response.ErrorListener{error ->
            Log.d("ERROR","Could not add user: $error")
            complete(false)
        }){
            override fun getBodyContentType(): String{
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray{
                return requestBody.toByteArray()
            }
            override fun getHeaders(): MutableMap<String,String>{
                val headers = HashMap<String,String>()
                headers.put("Authorization","Bearer $authToken")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(loginRequest)
    }


    fun findUsersByEmail(context: Context, complete: (Boolean) -> Unit){
        val findUserRequest = object : JSONObjectRequest(Method.GET, "$URL_GET_USER$userEmail",null,Response.Listener{ response ->
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")

                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)

            } catch (e: JSONException){
                Log.d("JSON","EXC: " + e.localizedMessage)
            }
        }, Response.ErrorListener{ error ->
            Log.d("ERROR","Could not find user.")
            complete(false)
        }){
            override fun getBodyContentType(): String{
                return "application/json; charset=utf-8"
            }
            override fun getHeaders(): MutableMap<String,String>{
                val headers = HashMap<String,String>()
                headers.put("Authorization","Bearer $authToken")
                return headers
            }

        }
        Volley.newRequestQueue(context).add(findUserRequest)

    }

}