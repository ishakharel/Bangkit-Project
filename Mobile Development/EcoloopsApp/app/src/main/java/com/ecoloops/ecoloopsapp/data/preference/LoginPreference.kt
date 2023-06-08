package com.ecoloops.ecoloopsapp.data.preference

import android.content.Context
import com.ecoloops.ecoloopsapp.data.model.UserModel

class LoginPreference(context: Context) {
    private val preferences = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE)

    fun setUser(id: String?, name: String?, email: String?, image: String?, points: Int?,  token: String?) {
        val editor = preferences.edit()
        editor.putString("id", id)
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("image", image)
        editor.putInt("points", points!!)
        editor.putString("token", token)
        editor.apply()
    }

    fun setImgUrl(image: String?) {
        val editor = preferences.edit()
        editor.putString("image", image)
        editor.apply()
    }

    fun getUser(): UserModel {
        val model = UserModel()
        model.id = preferences.getString("id", "")
        model.name = preferences.getString("name", "")
        model.email = preferences.getString("email", "")
        model.image = preferences.getString("image", "")
        model.points = preferences.getInt("points", 0)
        model.token = preferences.getString("token", "")
        return model
    }

    fun clearUser() : Boolean{
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
        return true
    }
}