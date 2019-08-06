package com.expensemoitor.expensemonitor.registeruser


import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.expensemoitor.expensemonitor.Network.ExpenseMonitorApi
import com.expensemoitor.expensemonitor.Network.ExpenseMonitorApiService
import com.expensemoitor.expensemonitor.Network.UserData
import com.expensemoitor.expensemonitor.Network.UserResponse
import com.expensemoitor.expensemonitor.R
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class RegisterationUserViewModel :ViewModel() {

    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    var radiochecked = MutableLiveData<Int>()
    var geneder = ""

    private val _genderSelected = MutableLiveData<String>()
    val genderSelected: LiveData<String>
        get() = _genderSelected


    fun onNext() {
        val name = name.value
        val email = email.value

        when(radiochecked.value){
            R.id.male_radiobutton->{
                geneder = "male"
            }

            R.id.female_radiobutton->{
                geneder ="female"
            }
        }

        if (geneder.isNotEmpty()){
             registerUser(name!!, email!!,geneder)
        }else{
            _genderSelected.value = "please specify your gender"
        }

    }


    private fun registerUser(userName:String,userEmail:String,gender:String){
        val userData = UserData(userName,userEmail,gender)
        ExpenseMonitorApi.retrofitService.registerationUser(userData).enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
               Log.d("onFailure",t.toString())
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("onResponse",response.body()?.accesstoken+" "+response.body()?.userCurrentExpense)
            }

        })
    }

}