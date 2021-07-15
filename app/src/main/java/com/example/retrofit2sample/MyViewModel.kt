package com.example.retrofit2sample

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.Path





class MyViewModel : ViewModel() {

    class User(var name: String, var mail: String, var tel: String) {
        fun to_s(): String = "${name} [${mail}, ${tel}]"
    }

    interface RetrofitApi {
        @GET("/mydata.json")
        fun getUsers(): Call<MutableList<User>>

        @GET("/mydata/{id}.json")
        fun getUser(@Path("id") id: Int): Call<User>?
    }

    val fireDatum: MutableLiveData<User> by lazy {

    }

    val fireData: MutableLiveData<MutableList<User>> by lazy {
        MutableLiveData<MutableList<User>>()
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://tuyano-dummy-data.firebaseio.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: RetrofitApi = retrofit.create<RetrofitApi>(RetrofitApi::class.java)

    fun getUserByRetrofit(id: Int = 0) {
        val call = api.getUser(id) as Call<User>
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                fireDatum.value = response.body()
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.d("ERR", t?.message)
            }

        })
    }

    fun getUsersByRetrofit() {
        val call = api.getUsers() as Call<MutableList<User>>
        call.enqueue(object : Callback<MutableList<User>> {
            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                fireData.value = response.body()
            }

            override fun onFailure(call: Call<MutableList<User>>, t: Throwable?) {
                Log.d("ERR", t?.message)
            }
        })
    }

    fun getFireDatumByText(data: User?): String {
        if (data == null) {
            return "no-data."
        }
        return data.to_s()
    }

    fun getFireDataByText(data: MutableList<User>?): String {
        if (data == null || data.size == 0)  {
            return  "no-data."
        }
        var res = ""
        for (item in data.iterator()) {
            res += "${item.to_s()}\n"
        }
        return res
    }
}

class MyViewModelFactory():ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (model.isAssingnableForm(MyViewModel::class.java)) {
            return MyViewModel() as T
        }
        throw IllegalArgumentException("CANNOT_GET_VIEWMODEL")
    }

}