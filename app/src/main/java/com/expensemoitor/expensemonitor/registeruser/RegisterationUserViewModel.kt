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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
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


    private var viewModelJob = Job()
    private val coroutineJob = CoroutineScope(viewModelJob + Dispatchers.Main)


    private fun registerUser(userName:String,userEmail:String,gender:String){

        coroutineJob.launch {
            val userData = UserData(userName,userEmail,gender)
            var getUserResponse =  ExpenseMonitorApi.retrofitService.registerationUser(userData)
            try {
                var userResponse = getUserResponse.await()
                Log.d("onResponse",userResponse?.accesstoken+" "+userResponse.userCurrentExpense)

            }catch (t:Throwable){
                Log.d("onFailure",t.toString())
            }

        }


    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}