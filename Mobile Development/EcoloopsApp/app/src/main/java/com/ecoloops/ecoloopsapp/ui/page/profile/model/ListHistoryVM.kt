package com.ecoloops.ecoloopsapp.ui.page.profile.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ecoloops.ecoloopsapp.data.remote.response.ListHistoryResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListHistoryVM : ViewModel() {

    private val _histories = MutableLiveData<ListHistoryResponse>()
    val histories: LiveData<ListHistoryResponse> = _histories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getHistories(token: String?){
        _isLoading.value = true

        val apiClient = ApiConfig()
        val apiService = apiClient.createApiService()
        val call = apiService.getHistories("Bearer $token")

        call.enqueue(object : Callback<ListHistoryResponse> {
            override fun onResponse(
                call: Call<ListHistoryResponse>,
                response: Response<ListHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    val historyResponse = response.body()
                    _histories.value = historyResponse!!
                    Log.d("HistoryActivity", "onResponses: ${historyResponse?.message}")

                } else {
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody)
                    val message = jsonObject.getString("message")

                }

                _isLoading.value = false

            }

            override fun onFailure(call: Call<ListHistoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("HistoryActivity", "onFailure: ${t.message}")
            }

        })



    }

}