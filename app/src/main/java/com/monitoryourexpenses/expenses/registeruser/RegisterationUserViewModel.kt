package com.monitoryourexpenses.expenses.registeruser


import android.app.Application
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.ExpenseMonitorDao
import com.monitoryourexpenses.expenses.api.ApiFactory
import com.monitoryourexpenses.expenses.api.UserData
import com.monitoryourexpenses.expenses.data.UserRepository
import com.monitoryourexpenses.expenses.utilites.MyApp.Companion.context
import com.monitoryourexpenses.expenses.utilites.PrefManager
import com.monitoryourexpenses.expenses.utilites.ProgressStatus
import com.monitoryourexpenses.expenses.utilites.saveCurrencyForSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okio.IOException
import org.threeten.bp.LocalDate
import retrofit2.HttpException


class RegisterationUserViewModel(val userRepository: UserRepository, var application: Application) :ViewModel() {

    var radiochecked = MutableLiveData<Int>()
    private var geneder = ""
    var currency = ""

    init {
        saveAllDates()
    }

    private val _status = MutableLiveData<ProgressStatus>()
    val status: LiveData<ProgressStatus>
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

    @ExperimentalCoroutinesApi
    fun registerUser(userName:String, emailAddress:String) {
        when(radiochecked.value){
            R.id.male_radio_button->{
                geneder = "male"
            }

            R.id.female_radio_button->{
                geneder ="female"
            }
        }

        if (geneder.isEmpty()){
            _genderSelected.value = false
        }else if(currency == context?.getString(R.string.select_currency)){
            _genderSelected.value = false
        }else{
            val userData = UserData(userName,emailAddress,geneder,currency)
            viewModelScope.launch {
                userRepository.registerNewUser(userData)
                    .onStart { _status.value = ProgressStatus.LOADING }
                    .onCompletion {  _status.value = ProgressStatus.DONE }
                    .catch {
                        if (it is IOException){
                            _errormsg.value =context?.getString(R.string.weak_internet_connection)
                        }else if (it is HttpException){
                            when(it.code()){
                                500->{
                            _errormsg.value = context?.getString(R.string.try_later)
                                }
                            }
                        }
                    }
                    .collect {
                        PrefManager.saveCurrency(application,currency.substring(range = 0..2))
                        saveCurrencyForSettings(currency)
                        PrefManager.setUserRegistered(application,true)
                        PrefManager.saveAccessToken(application,it.accessToken)
                        _navigateToNextScreen.value = true
                    }
            }
        }
        }

    private fun saveAllDates(){
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
}