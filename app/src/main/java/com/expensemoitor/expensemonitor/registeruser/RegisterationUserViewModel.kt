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
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.expensemoitor.expensemonitor.utilites.progressStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException




class RegisterationUserViewModel(var application: Application) :ViewModel() {

    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    var radiochecked = MutableLiveData<Int>()
    var geneder = ""
    var currency = ""


    private val _status = MutableLiveData<progressStatus>()
    val status: LiveData<progressStatus>
        get() = _status

    private val _displayMsg = MutableLiveData<Boolean>()
    val displayMsg: LiveData<Boolean>
        get() = _displayMsg


    private val _genderSelected = MutableLiveData<Boolean>()
    val genderSelected: LiveData<Boolean>
        get() = _genderSelected


    private val _navigateToMyExpenseFragment = MutableLiveData<Boolean>()
    val navigateToMyExpenseFragment : LiveData<Boolean>
        get() = _navigateToMyExpenseFragment


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

        if (geneder == null || currency.equals("Select Currency")){
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
        coroutineJob.launch {
            val userData = UserData(userName,userEmail,gender)
            val getUserResponse =  ApiFactory.REGISTERATION_SERVICE.registerationUser(userData)
            try {
                _status.value = progressStatus.LOADING
                val userResponse = getUserResponse.await()
                 PrefManager.saveAccessTokenAndCurrentExpense(application,userResponse.accesstoken,userResponse.userCurrentExpense.toInt(),userResponse.weekExpense.toInt(),userResponse.monthExpense.toInt())
                 Log.d("accessToken",PrefManager.getAccessToken(application)+"\n"+PrefManager.getUserExpenses(application))
                _navigateToMyExpenseFragment.value = true
                _status.value = progressStatus.DONE
            }catch (t:Throwable){
                _status.value = progressStatus.ERROR
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