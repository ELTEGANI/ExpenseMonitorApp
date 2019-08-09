package com.expensemoitor.expensemonitor.registeruser


import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.expensemoitor.expensemonitor.network.ExpenseMonitorApi
import com.expensemoitor.expensemonitor.network.UserData
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.utilites.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException




class RegisterationUserViewModel(application: Application) :ViewModel() {

    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    var radiochecked = MutableLiveData<Int>()
    var prefManager:PrefManager = PrefManager(application)
    var geneder = ""


    private val _displayMsg = MutableLiveData<Boolean>()
    val displayMsg: LiveData<Boolean>
        get() = _displayMsg


    private val _genderSelected = MutableLiveData<Boolean>()
    val genderSelected: LiveData<Boolean>
        get() = _genderSelected


    private val _navigateToMyExpenseFragment = MutableLiveData<Boolean>()
    val navigateToMyExpenseFragment : LiveData<Boolean>
        get() = _navigateToMyExpenseFragment



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
            _genderSelected.value = false
        }

    }


    private var viewModelJob = Job()
    private val coroutineJob = CoroutineScope(viewModelJob + Dispatchers.Main)


    private fun registerUser(userName:String,userEmail:String,gender:String) {
        coroutineJob.launch {
            val userData = UserData(userName,userEmail,gender)
            val getUserResponse =  ExpenseMonitorApi.retrofitService.registerationUser(userData)
            try {
                val userResponse = getUserResponse.await()
                 prefManager.saveAccessTokenAndCurrentExpense(userResponse.userCurrentExpense, userResponse.accesstoken)
                _navigateToMyExpenseFragment.value = true
            }catch (t:Throwable){
                _navigateToMyExpenseFragment.value = false
                if(t is IOException){
                    _displayMsg.value = true
                }
            }
        }
    }

    fun genderAlreadySelected(){
        _genderSelected.value = true
    }

    fun internetIsAvailable(){
        _displayMsg.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}