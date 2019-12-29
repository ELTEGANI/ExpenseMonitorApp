package com.expensemoitor.expensemonitor.registeruser


import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.expensemoitor.expensemonitor.network.UserData
import com.expensemoitor.expensemonitor.R
import com.expensemoitor.expensemonitor.database.ExpenseMonitorDao
import com.expensemoitor.expensemonitor.database.UserExpenses
import com.expensemoitor.expensemonitor.network.ApiFactory
import com.expensemoitor.expensemonitor.utilites.*
import com.expensemoitor.expensemonitor.utilites.Converter.Companion.toBigDecimal
import com.expensemoitor.expensemonitor.utilites.MyApp.Companion.context
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.math.BigDecimal


class RegisterationUserViewModel(val database: ExpenseMonitorDao, var application: Application) :ViewModel() {


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

    private val _navigateToNextScreen = MutableLiveData<Boolean>()
    val navigateToNextScreen: LiveData<Boolean>
        get() = _navigateToNextScreen


    private val _genderSelected = MutableLiveData<Boolean>()
    val genderSelected: LiveData<Boolean>
        get() = _genderSelected


    private val _errormsg = MutableLiveData<String>()
    val errormsg : LiveData<String>
        get() = _errormsg


    fun onSelectCurrencyItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            currency = parent.selectedItem.toString()
    }





    fun registerUser(userName:String,emailAddress:String) {
        when(radiochecked.value){
            R.id.male_radiobutton->{
                geneder = "male"
            }

            R.id.female_radiobutton->{
                geneder ="female"
            }
        }

        if (geneder == null || currency.equals(context?.getString(R.string.select_currency))){
            _genderSelected.value = false
        }else{
            val weekDates = getStartAndEndOfTheWeek().split("*")
            val monthDates = getTheStartAndTheEndOfTheMonth().split("*")
            val userData = UserData(userName,emailAddress,geneder,currency,
                weekDates[0],
                weekDates[1],
                monthDates[0],
                monthDates[1]
            )
            viewModelScope.launch {
                val getUserResponse =  ApiFactory.REGISTERATION_SERVICE.registerationUser(userData)
                try {
                    try {
                        _status.value = progressStatus.LOADING
                        val userResponse = getUserResponse.await()
                        saveCurrencyForSettings(currency)
                        PrefManager.setUserRegistered(application,true)
                        PrefManager.saveAccessToken(application,userResponse.accessToken)
                                database.insertExpense(UserExpenses(
                                    todayExpenses = toBigDecimal("0"),
                                    weekExpenses  = toBigDecimal("0"),
                                    monthExpenses = toBigDecimal("0"),
                                    currency      = currency
                                ))
                        _navigateToNextScreen.value = true
                        _status.value = progressStatus.DONE
                    }catch (t:Throwable){
                        Log.d("throwable",t.toString())
                        _status.value = progressStatus.ERROR
                        _errormsg.value = t.toString()
                    }
                }catch (httpException:HttpException){
                    Log.d("httpException",httpException.message())
                }
            }
        }
    }

    fun genderAlreadySelected(){
        _genderSelected.value = true
    }

    fun onErrorMsgDisplayed(){
        _errormsg.value = null
    }

    fun onNavigationCompleted(){
        _navigateToNextScreen.value = false
    }

}