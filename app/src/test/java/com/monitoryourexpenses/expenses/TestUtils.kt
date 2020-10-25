package com.monitoryourexpenses.expenses

import androidx.lifecycle.LiveData
import org.junit.Assert


fun assertSnackbarMessage(snackbarLiveData: LiveData<Event<Int>>, messageId: Int) {
    val value: Event<Int> = snackbarLiveData.getOrAwaitValue()
    Assert.assertEquals(value.getContentIfNotHandled(), messageId)
}