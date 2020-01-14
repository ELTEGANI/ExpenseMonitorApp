package com.expensemoitor.expensemonitor.registeruser


import android.app.Application
import android.content.SharedPreferences
import android.os.Build
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
import org.threeten.bp.LocalDate
import retrofit2.HttpException


class RegisterationUserViewModel(val database: ExpenseMonitorDao, var application: Application) :ViewModel() {


    var radiochecked = MutableLiveData<Int>()
    var geneder = ""
    var currency = ""

    lateinit var sharedPreferences: SharedPreferences

    init {
        //save dates for the first time so it can be updated later
        saveAllDates()
         //Encrypted SharedPreference wait for google to release version work below 23
         //initEncryptedSharedPreferences()
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
            val userData = UserData(userName,emailAddress,geneder,currency)
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
                        _errormsg.value = context?.getString(R.string.weak_internet_connection)
                    }
                }catch (httpException:HttpException){
                    Log.d("httpException",httpException.message())
                }
            }
        }
    }

    fun saveAllDates(){
        viewModelScope.launch {
            PrefManager.saveCurrentDate(application,LocalDate.now().toString())
            PrefManager.saveStartOfTheWeek(application,LocalDate.now().toString())
            PrefManager.saveStartOfTheMonth(application,LocalDate.now().toString())
            PrefManager.saveEndOfTheWeek(application,LocalDate.now().plusDays(7).toString())
            PrefManager.saveEndOfTheMonth(application,LocalDate.now().plusMonths(1).toString())
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

//    private fun initEncryptedSharedPreferences() {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//            sharedPreferences = EncryptedSharedPreferences.create(
//                "APP_PREF",
//                masterKeyAlias,
//                application,
//                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//            )
//        }
//    }

}