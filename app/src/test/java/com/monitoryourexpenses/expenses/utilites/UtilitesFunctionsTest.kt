package com.monitoryourexpenses.expenses.utilites

import com.monitoryourexpenses.expenses.api.DurationExpenseResponse
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class UtilitesFunctionsTest{

    @Test
    fun getSumationOfExpenseAmount_listOfAmount_totalAmounts(){

     //GIVEN a list of expense with its amount
     val amounts = listOf(
         DurationExpenseResponse(id = "1",amount = "3000000000",description = "new",expenseCategory = "test",currency = "SDG",date = "2020-1-1"),
         DurationExpenseResponse(id = "1",amount = "4000000000",description = "new",expenseCategory = "test",currency = "SDG",date = "2020-1-1"),
         DurationExpenseResponse(id = "1",amount = "5000000000",description = "new",expenseCategory = "test",currency = "SDG",date = "2020-1-1")

        )

     //WHEN call sumationOfAmount
     val result = sumationOfAmount(amounts)


     //THEN there is total amount of all expenses in BigDecimal
     assertThat(result,`is`(12000000000.toBigDecimal()))

    }


    @Test
    fun getSumationOfExpenseAmount_emptyListOfAmount_zeroAmount(){

        //GIVEN a empty list of expenses
        val amounts = emptyList<DurationExpenseResponse>()

        //WHEN call sumationOfAmount
        val result = sumationOfAmount(amounts)


        //THEN there is zero amount
        assertThat(result,`is`(BigDecimal.ZERO))

    }


    @Test
    fun getSumationOfExpenseAmount_null_zeroAmount(){

        //GIVEN a null list of expenses
        val amounts = null

        //WHEN call sumationOfAmount
        val result = sumationOfAmount(amounts)


        //THEN there is zero amount
        assertThat(result,`is`(BigDecimal.ZERO))

    }



    @Test
    fun getFormattedAmount_amount_amountWithComma(){
        //GIVEN amount
        val amount = "1000000000"

        //WHEN call expenseAmountFormatWithComma
        val result =  expenseAmountFormatter(amount)


        //THEN is a number with comma
        assertThat(result,`is`("1,000,000,000"))
    }


    @Test
    fun getFormattedAmount_nullAmount_amountWithComma(){
        //GIVEN amount
        val amount = null

        //WHEN call expenseAmountFormatWithComma
        val result =  expenseAmountFormatter(amount)


        //THEN is a number with comma
        assertThat(result,`is`("0"))
    }





}