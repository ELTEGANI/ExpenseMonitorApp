package com.monitoryourexpenses.expenses.adapters

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class NoPredictiveAnimationsGridLayoutManager(context: Context?, spanCount: Int) :
    GridLayoutManager(context, spanCount) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}
