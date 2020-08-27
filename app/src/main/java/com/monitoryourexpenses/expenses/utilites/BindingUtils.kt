package com.monitoryourexpenses.expenses.utilites

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.google.android.material.elevation.ElevationOverlayProvider
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.database.Expenses



@SuppressLint("SetTextI18n")
@BindingAdapter("expenseAmount")
fun TextView.setExpenseAmount(expenses:Expenses?){
    expenses?.let {
         text = PrefManager.getCurrency(context)+" "+ expenseAmountFormatter(expenses.amount.toString())
     }
}

@BindingAdapter("expenseCategory")
fun TextView.setExpensecategory(expenses:Expenses?){
    expenses?.let {
        text = expenses.expenseCategory
    }
}

@BindingAdapter("expenseDescription")
fun TextView.setExpenseDescription(expenses:Expenses?){
    expenses?.let {
        text = expenses.description
    }
}



@BindingAdapter("expenseDate")
fun TextView.setExpenseDate(expenses:Expenses?){
    expenses?.let {
        text = expenses.date
    }
}



@BindingAdapter(
    "marginLeftSystemWindowInsets",
    "marginTopSystemWindowInsets",
    "marginRightSystemWindowInsets",
    "marginBottomSystemWindowInsets",
    requireAll = false
)
fun View.applySystemWindowInsetsMargin(
    previousApplyLeft: Boolean,
    previousApplyTop: Boolean,
    previousApplyRight: Boolean,
    previousApplyBottom: Boolean,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    if (previousApplyLeft == applyLeft &&
        previousApplyTop == applyTop &&
        previousApplyRight == applyRight &&
        previousApplyBottom == applyBottom
    ) {
        return
    }

    doOnApplyWindowInsets { view, insets, _, margin ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin = margin.left + left
            topMargin = margin.top + top
            rightMargin = margin.right + right
            bottomMargin = margin.bottom + bottom
        }
    }
}

fun View.doOnApplyWindowInsets(
    block: (View, WindowInsets, InitialPadding, InitialMargin) -> Unit
) {
    // Create a snapshot of the view's padding & margin states
    val initialPadding = recordInitialPaddingForView(this)
    val initialMargin = recordInitialMarginForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding & margin states
    setOnApplyWindowInsetsListener { v, insets ->
        block(v, insets, initialPadding, initialMargin)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

class InitialMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

private fun recordInitialMarginForView(view: View): InitialMargin {
    val lp = view.layoutParams as? ViewGroup.MarginLayoutParams
        ?: throw IllegalArgumentException("Invalid view layout params")
    return InitialMargin(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}



@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    requireAll = false
)
fun View.applySystemWindowInsetsPadding(
    previousApplyLeft: Boolean,
    previousApplyTop: Boolean,
    previousApplyRight: Boolean,
    previousApplyBottom: Boolean,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    if (previousApplyLeft == applyLeft &&
        previousApplyTop == applyTop &&
        previousApplyRight == applyRight &&
        previousApplyBottom == applyBottom
    ) {
        return
    }

    doOnApplyWindowInsets { view, insets, padding, _ ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        view.setPadding(
            padding.left + left,
            padding.top + top,
            padding.right + right,
            padding.bottom + bottom
        )
    }
}

@BindingAdapter("layoutFullscreen")
fun View.bindLayoutFullscreen(previousFullscreen: Boolean, fullscreen: Boolean) {
    if (previousFullscreen != fullscreen && fullscreen) {
        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}

@BindingAdapter(
    "popupElevationOverlay"
)
fun Spinner.bindPopupElevationOverlay(popupElevationOverlay: Float) {
    setPopupBackgroundDrawable(
        ColorDrawable(
            ElevationOverlayProvider(context)
                .compositeOverlayWithThemeSurfaceColorIfNeeded(popupElevationOverlay)
        )
    )
}

@BindingAdapter("expenseCategoryImage")
fun ImageView.setCategoryImage(category: Categories?) {
    category?.let {
        setImageResource(when (category.CategoryName) {
            context.getString(R.string.Anniversary) -> R.drawable.ic_anniversary
            context.getString(R.string.Adultsclothing) -> R.drawable.ic_receipt
            context.getString(R.string.Alimonyandchildsupport) -> R.drawable.ic_human_female_boy
            context.getString(R.string.Babysitter) -> R.drawable.ic_baby_face_outline
            context.getString(R.string.beef)-> R.drawable.ic_food_drumstick
            context.getString(R.string.Books) -> R.drawable.ic_book_shelf
            context.getString(R.string.Bigpurchases) -> R.drawable.ic_basket_plus
            context.getString(R.string.Birthday) -> R.drawable.ic_cake_layered
            context.getString(R.string.boosh) -> R.drawable.ic_food_variant
            context.getString(R.string.fastfood) -> R.drawable.ic_food
            context.getString(R.string.breakfast) -> R.drawable.ic_food_fork_drink
            context.getString(R.string.Cable) -> R.drawable.ic_cake_layered
            context.getString(R.string.Cafes) -> R.drawable.ic_coffee
            context.getString(R.string.CarLeasing) -> R.drawable.ic_car_hatchback
            context.getString(R.string.Carpayment) -> R.drawable.ic_car_outline
            context.getString(R.string.Electricity) -> R.drawable.ic_flash
            context.getString(R.string.Invoices) -> R.drawable.ic_receipt
            else -> R.drawable.new_category
        })
    }
}

@BindingAdapter("CategoryNameString")
fun TextView.setCategoryNameString(category: Categories?) {
    category?.let {
        text = category.CategoryName
    }
}

