package com.expensemoitor.expensemonitor.registeruser


import android.app.Application
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.expensemoitor.expensemonitor.network.UserData
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.utilites.*
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException




class RegisterationUserViewModel(var application: Application) :ViewModel() {

    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    var radiochecked = MutableLiveData<Int>()
    var geneder = ""
    var currency = ""


    init {
        //save currentDate for the first time so it can be updated later
        PrefManager.saveCurrentDate(application, getCurrentDate())
    }


    private val _status = MutableLiveData<progressStatus>()
    val status: LiveData<progressStatus>
        get() = _status

    private val _responseMsg = MutableLiveData<String>()
    val responseMsg: LiveData<String>
        get() = _responseMsg


    private val _genderSelected = MutableLiveData<Boolean>()
    val genderSelected: LiveData<Boolean>
        get() = _genderSelected


    private val _errormsg = MutableLiveData<String>()
    val errormsg : LiveData<String>
        get() = _errormsg


    fun onSelectCurrencyItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            currency = parent.selectedItem.toString()
    }

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

        if (geneder == null || currency.equals("Select Your Currency")){
            _genderSelected.value = false
        }else{
            PrefManager.saveCurrency(application,currency)
            Log.d("userCurrency",PrefManager.getCurrency(application).toString())
            name?.let { email?.let { it1 -> registerUser(it, it1,geneder) } }
        }

    }


    private var viewModelJob = Job()
    private val coroutineJob = CoroutineScope(viewModelJob + Dispatchers.Main)


    private fun registerUser(userName:String,userEmail:String,gender:String) {
        val weekDates = getStartAndEndOfTheWeek().split("*")
        val monthDates = getTheStartAndTheEndOfTheMonth().split("*")
        coroutineJob.launch {
            val userData = UserData(userName,userEmail,gender,
                weekDates[0],
                weekDates[1],
                monthDates[0],
                monthDates[1]
                )
            val getUserResponse =  ApiFactory.REGISTERATION_SERVICE.registerationUser(userData)
           try {
               try {
                   _status.value = progressStatus.LOADING
                   val userResponse = getUserResponse.await()
                   PrefManager.saveAccessTokenAndCurrentExpense(context,userResponse.accesstoken,userResponse.userCurrentExpense.toInt(),userResponse.weekExpense.toInt(),userResponse.monthExpense.toInt())
                   PrefManager.setUserRegistered(context,true)
                   _responseMsg.value = "Registeration Done Successfully"
                   _status.value = progressStatus.DONE
               }catch (t:Throwable){
                   _status.value = progressStatus.ERROR
                   if(t is IOException){
                       _errormsg.value = "Poor Internet Connections"
                   }
               }
           }catch (httpException:HttpException){
               Log.d("httpException",httpException.toString())
           }

        }
    }

    fun genderAlreadySelected(){
        _genderSelected.value = true
    }

    fun onErrorMsgDisplayed(){
        _errormsg.value = null
    }

    fun onResponseMsgDisplayed(){
        _responseMsg.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}