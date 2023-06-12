package com.ecoloops.ecoloopsapp.ui.page.reward.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ecoloops.ecoloopsapp.data.remote.response.ListMerchResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMerchVM : ViewModel() {

    private val _merch = MutableLiveData<ListMerchResponse>()
    val merch: LiveData<ListMerchResponse> = _merch

    fun getListMerch(token: String?){
        val apiClient = ApiConfig()
        val apiService = apiClient.createApiService()
        val call = apiService.getListMerch("Bearer $token")

        call.enqueue(object : Callback<ListMerchResponse> {
            override fun onResponse(
                call: Call<ListMerchResponse>,
                response: Response<ListMerchResponse>
            ) {
                if (response.isSuccessful) {
                    val historyResponse = response.body()
                    _merch.value = historyResponse!!
                    Log.d("MerchActivity", "onResponses: ${historyResponse?.message}")

                } else {
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody)
                    val message = jsonObject.getString("message")

                }

            }

            override fun onFailure(call: Call<ListMerchResponse>, t: Throwable) {
                Log.e("MerchActivity", "onFailure: ${t.message}")
            }

        })



    }

}