package com.expensemoitor.expensemonitor.createnewexpense


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expensemoitor.expensemonitor.utilites.displayCurrentDate


class CreateNewExpenseFragmentViewModel() : ViewModel() {



    val amount = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val currentDate = MutableLiveData<String>()


    init {
        currentDate.value = displayCurrentDate()
    }
    private val _validationMsg = MutableLiveData<Boolean>()
    val validationMsg: LiveData<Boolean>
        get() = _validationMsg




    fun createExpense(){
        val expenseAmount = amount.value
        val expenseDescription = description.value
        val expenseDate = currentDate.value

        if(expenseAmount == null || expenseDescription == null){
            _validationMsg.value = true
        }else{
           Log.d("currentDate","\n"+expenseAmount+"\n"+expenseDescription+"\n"+expenseDate)
        }
    }

    fun onNoEmptyFields(){
        _validationMsg.value = false
    }





    override fun onCleared() {
        super.onCleared()
    }


}