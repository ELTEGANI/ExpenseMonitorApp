package com.expensemoitor.expensemonitor.createnewexpense


import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CreateNewExpenseFragmentViewModel() : ViewModel() {



    val amount = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val currentDate = ObservableField<String>()

    private val _validationMsg = MutableLiveData<Boolean>()
    val validationMsg: LiveData<Boolean>
        get() = _validationMsg


    fun createExpense(){
        val expenseAmount = amount.value
        val expenseDescription = description.value

        if(expenseAmount == null || expenseDescription == null){
            _validationMsg.value = true
        }else{
           Log.d("currentDate","\n"+currentDate.get().toString())
        }
    }

    fun onNoEmptyFields(){
        _validationMsg.value = false
    }




    override fun onCleared() {
        super.onCleared()
    }


}