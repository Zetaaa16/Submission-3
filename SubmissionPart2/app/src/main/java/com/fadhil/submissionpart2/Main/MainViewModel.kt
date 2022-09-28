package com.fadhil.submissionpart2.Main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadhil.submissionpart2.API.ApiConfig
import com.fadhil.submissionpart2.Database.FavoriteUser
import com.fadhil.submissionpart2.Database.FavoriteUserDao
import com.fadhil.submissionpart2.Database.UserDatabase
import com.fadhil.submissionpart2.dll.DetailResponse
import com.fadhil.submissionpart2.User.User
import com.fadhil.submissionpart2.User.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


import retrofit2.Call
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val listUsers = MutableLiveData<ArrayList<User>>()
    val UserDetail = MutableLiveData<DetailResponse>()

    private var userDao: FavoriteUserDao?
    private var userDB: UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.favoriteUserDao()
    }

    fun setSearch(username: String){

        ApiConfig.getApiService()
            .getUser(username)
            .enqueue(object : retrofit2.Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if(response.isSuccessful){
                        listUsers.postValue(response.body()?.items)


                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun setDetail(username: String){

        ApiConfig.getApiService()
            .getUserDetails(username)
            .enqueue(object : retrofit2.Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if(response.isSuccessful){
                        UserDetail.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }
    fun getUserDetail(): LiveData<DetailResponse> {
        return UserDetail
    }

    fun get(): LiveData<ArrayList<User>>{
        return listUsers
    }

    fun addToFavorite(username: String,id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(username,id,avatarUrl)
            userDao?.addToFavorite(user)
        }
    }

     fun checkUser(id: Int) = userDao?.checUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }




}



