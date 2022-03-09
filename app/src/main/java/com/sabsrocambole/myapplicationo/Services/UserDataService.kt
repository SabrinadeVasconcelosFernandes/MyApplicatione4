package com.sabsrocambole.myapplicationo.Services

import android.graphics.Color
import java.util.*

object UserDataService {

    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout(){
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        AuthService.authToken = ""
        AuthService.userEmail = ""
        AuthService.isLoggedIn = false
    }

    fun returnsAvatarColor(components: String) : Int{
        //número que recebemos do sistema representando a cor. Precisamos transformar esse número em RGB
        //exemplo de numero --> [0.1333333333333333, 0.7568627450980392, 0.7215686274509804, 1]

        //vamos tirar os parentesis e as virgulas
        //0.1333333333333333 0.7568627450980392 0.7215686274509804 1
        val strippedColor = components
            .replace("[","")
            .replace("]","")
            .replace(",","")

        //vamos usar o scanner para pegar os tres valores e deixar o ultimo
        //vamos tirar o número do final, que representa o alfa, não sendo nenhum RGB, não precisamos
        //0.1333333333333333 0.7568627450980392 0.7215686274509804

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()){
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }
        return Color.rgb(r,g,b)
    }
}