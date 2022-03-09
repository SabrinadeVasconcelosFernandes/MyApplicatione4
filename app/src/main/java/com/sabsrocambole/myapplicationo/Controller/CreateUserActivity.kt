package com.sabsrocambole.myapplicationo.Controller

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sabsrocambole.myapplicatione4.R
import com.sabsrocambole.myapplicationo.Services.AuthService
import org.w3c.dom.Text
import java.util.*


class CreateUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }
    var createEmailText = findViewById<TextView>(R.id.createEmailText)
    var createPassword = findViewById<TextView>(R.id.createPasswordText)
    val createUserNameText = findViewById<TextView>(R.id.createUserNameText)
    var createAvatarImageView = findViewById<ImageView>(R.id.createAvatarImageView)
    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    fun generateUserAvatar(view: View){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0){
            userAvatar = "Light$avatar"
        }
        else{
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar,"drawable",packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View){
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble()/255
        val savedG = g.toDouble()/255
        val savedB = b.toDouble()/255

        avatarColor = "[$savedR,$savedG,$savedB,1]"
    }

    fun createUserClicked(view: View){
        val userName = createUserNameText.toString()
        val email = createEmailText.toString()
        val password = createPassword.toString()
        AuthService.registerUser(this,email,password){registerSuccess ->
            if(registerSuccess){
                AuthService.loginUser(this,email,password){ loginSuccess ->
                    if (loginSuccess){
                        AuthService.createUser(this,userName,email,userAvatar,avatarColor){
                            createSuccess -> if(createSuccess){
                                    finish()
                            }
                        }
                    }

                }
            }
        }
    }



}