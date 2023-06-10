package com.ecoloops.ecoloopsapp.ui.page.home.model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ecoloops.ecoloopsapp.data.remote.response.ListCategoryResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.utils.showAlert
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListCategoryVM : ViewModel() {

    private val _categories = MutableLiveData<ListCategoryResponse>()
    val categories: LiveData<ListCategoryResponse> = _categories

    fun getCategories(){

        val apiClient = ApiConfig()
        val apiService = apiClient.createApiService()
        val call = apiService.getCategories()

        call.enqueue(object : Callback<ListCategoryResponse> {
            override fun onResponse(
                call: Call<ListCategoryResponse>,
                response: Response<ListCategoryResponse>
            ) {
                if (response.isSuccessful) {
                    val categoryResponse = response.body()
                    _categories.value = categoryResponse!!
                } else {
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody)
                    val message = jsonObject.getString("message")

                }
            }

            override fun onFailure(call: Call<ListCategoryResponse>, t: Throwable) {
                Log.e("HistoryActivity", "onFailure: ${t.message}")
            }
        })
    }

}