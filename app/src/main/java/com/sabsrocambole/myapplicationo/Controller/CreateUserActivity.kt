package com.sabsrocambole.myapplicationo.Controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sabsrocambole.myapplicatione4.R
import com.sabsrocambole.myapplicationo.Services.AuthService
import com.sabsrocambole.myapplicationo.Utilities.BROADCAST_USER_DATA_CHANGE
import org.w3c.dom.Text
import java.util.*


class CreateUserActivity : AppCompatActivity() {

    var createSpinner = findViewById<ProgressBar>(R.id.createSpiner)
    var createEmailText = findViewById<TextView>(R.id.createEmailText)
    var createPassword = findViewById<TextView>(R.id.createPasswordText)
    val createUserNameText = findViewById<TextView>(R.id.createUserNameText)
    var createAvatarImageView = findViewById<ImageView>(R.id.createAvatarImageView)
    val createUserBtn = findViewById<Button>(R.id.createUserBtn)
    val backgroundColorBtn = findViewById<Button>(R.id.backgroundColorBtn)

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }

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
        enableSpinner(true)
        val userName = createUserNameText.toString()
        val email = createEmailText.toString()
        val password = createPassword.toString()

        if(userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){

            AuthService.registerUser(this,email,password){registerSuccess ->
                if(registerSuccess){
                    AuthService.loginUser(this,email,password){ loginSuccess ->
                        if (loginSuccess){
                            AuthService.createUser(this,userName,email,userAvatar,avatarColor){ createSuccess ->
                                if(createSuccess){

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

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
                    errorToast()
                }
            }
        }

        else{

            Toast.makeText(this,"Make sure user name, password and e-mail are all filled in Mary",
                Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }


    fun errorToast(){
        Toast.makeText(this,"Something went wrong, please try again.",
            Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean){
        if (enable){
            createSpinner.visibility = View.VISIBLE
            //quando  o spinner estiver ativado, eu vou sumir com alguns botões
            createUserBtn.isEnabled = false
            createAvatarImageView.isEnabled = false
            backgroundColorBtn.isEnabled = false
        }
        else{
            createSpinner.visibility = View.INVISIBLE
            //ativar as coisa que demos sumiço
            createUserBtn.isEnabled = true
            createAvatarImageView.isEnabled = true
            backgroundColorBtn.isEnabled = true
        }
    }


}