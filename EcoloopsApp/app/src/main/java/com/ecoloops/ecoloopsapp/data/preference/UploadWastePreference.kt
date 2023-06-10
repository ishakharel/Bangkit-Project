package com.ecoloops.ecoloopsapp.data.preference

import android.content.Context
import com.ecoloops.ecoloopsapp.data.model.UploadWasteModel
import com.ecoloops.ecoloopsapp.data.model.UserModel

class UploadWastePreference(context: Context) {
    private val preferences = context.getSharedPreferences("upload_pref", Context.MODE_PRIVATE)

    fun setUploadWaste(id: String?, name: String?, category: String?, description_recycle: String?, date: String?, points: Int?, image: String?) {
        val editor = preferences.edit()
        editor.putString("id", id)
        editor.putString("name", name)
        editor.putString("category", category)
        editor.putString("description_recycle", description_recycle)
        editor.putString("date", date)
        editor.putInt("points", points!!)
        editor.putString("image", image)
        editor.apply()
    }

    fun getUploadWaste(): UploadWasteModel {
        val model = UploadWasteModel()
        model.id = preferences.getString("id", "")
        model.name = preferences.getString("name", "")
        model.category = preferences.getString("category", "")
        model.description_recycle = preferences.getString("description_recycle", "")
        model.date = preferences.getString("date", "")
        model.points = preferences.getInt("points", 0)
        model.image = preferences.getString("image", "")
        return model
    }
}