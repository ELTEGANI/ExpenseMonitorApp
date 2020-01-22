package com.expensemoitor.expensemonitor.utilites

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.expensemoitor.expensemonitor.adapters.DurationsExpenseAdapter
import com.expensemoitor.expensemonitor.network.DurationExpenseResponse
import com.google.android.material.elevation.ElevationOverlayProvider


@BindingAdapter("progressStatus")
fun bindingStatus(progressBar: ProgressBar,status: ProgressStatus?){
    when(status){
        ProgressStatus.LOADING->{
           progressBar.visibility = View.VISIBLE
        }
        ProgressStatus.DONE->{
            progressBar.visibility = View.GONE
        }
        ProgressStatus.ERROR->{
            progressBar.visibility = View.GONE
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,data:List<DurationExpenseResponse>?){
    val adapter = recyclerView.adapter as DurationsExpenseAdapter
    adapter.submitList(data)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("expenseAmount")
fun TextView.setExpenseAmount(durationExpenseResponse:DurationExpenseResponse?){
    durationExpenseResponse?.let {
         text = PrefManager.getCurrency(context)+" "+ expenseAmountFormatter(durationExpenseResponse.amount)
     }
}

@BindingAdapter("expenseCategory")
fun TextView.setExpensecategory(durationExpenseResponse:DurationExpenseResponse?){
    durationExpenseResponse?.let {
        text = durationExpenseResponse.expenseCategory
    }
}

@BindingAdapter("expenseDescription")
fun TextView.setExpenseDescription(durationExpenseResponse:DurationExpenseResponse?){
    durationExpenseResponse?.let {
        text = durationExpenseResponse.description
    }
}



@BindingAdapter("expenseDate")
fun TextView.setExpenseDate(durationExpenseResponse:DurationExpenseResponse?){
    durationExpenseResponse?.let {
        text = durationExpenseResponse.date
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
