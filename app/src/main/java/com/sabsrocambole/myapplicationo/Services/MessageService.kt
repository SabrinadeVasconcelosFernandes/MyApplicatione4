package com.sabsrocambole.myapplicationo.Services

import android.content.Context
import android.util.Log
import com.sabsrocambole.geradordeabas.Model.Channel
import com.sabsrocambole.myapplicationo.Controller.App
import com.sabsrocambole.myapplicationo.Utilities.URL_GET_CHANNELS
import org.json.JSONArray
import org.json.JSONException
import java.lang.reflect.Method
import java.util.concurrent.CountedCompleter

object MessageService {
    //downloading channels and store messages

    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit){
        val channelsRequest = object : JSONArrayRequest(Method.GET, URL_GET_CHANNELS, null, Rsponse.Listener{
            try{
                for (x in 0 until response.length()){
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val chanDesc = channel.getString("description")
                    val channelId = channel.getString("_id")

                    val newChannel = Channel(name, chanDesc, channelId)
                    this.channels.add(newChannel)
                }
                complete(true)
            }  catch (e: JSONException){
                Log.d("JSON","EXC:" + e.localizedMessage)
                complete(false)
            }
        },Response.ErrorListener{ error ->
            Log.d("ERROR", "Could not retrieve channels")
            complete(false)
        }) {
            override fun getBodyContentType(): String{
                return "application/json; charset=utf-8"
            }
            override fun getHeaders() : MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization","Bearer ${AuthService.authToken}")
                return headers
            }
        }
        App.prefs.requestQueue.add(channelsRequest)
    }
}