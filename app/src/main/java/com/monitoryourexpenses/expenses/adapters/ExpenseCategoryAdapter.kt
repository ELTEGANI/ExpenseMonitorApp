package com.monitoryourexpenses.expenses.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.monitoryourexpenses.expenses.R
import com.monitoryourexpenses.expenses.database.Categories
import com.monitoryourexpenses.expenses.databinding.CategoriesItemExpenseBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlinx.android.synthetic.main.categories_item_expense.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

class ExpenseCategoryAdapter @Inject constructor() : ListAdapter<Categories, ExpenseCategoryAdapter.ViewHolder>(
    ExpenseCategoryResponseDiffCallback()
) {

    var categoryListener: CategoryListener? = null

    fun setOnClickListener(categoryListener: CategoryListener) {
        this.categoryListener = categoryListener
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    private var unFilteredList: List<Categories>? = null
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    fun addList(categories: List<Categories>?) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                unFilteredList = categories
                submitList(categories)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tracker?.let {
            categoryListener?.let { it1 ->
                holder.bind(getItem(position), it.isSelected(position.toLong()),
                    it1
                )
            }
        }

        if (tracker?.isSelected(position.toLong())!!) {
                holder.itemView.cardView.setBackgroundResource(R.drawable.item_category_background)
            } else {
                holder.itemView.cardView.setBackgroundResource(0)
        }
    }

    class ViewHolder private constructor(private val categoriesItemExpenseBinding: CategoriesItemExpenseBinding) :
        RecyclerView.ViewHolder(categoriesItemExpenseBinding.root) {
        fun bind(
            categories: Categories,
            isActivated: Boolean = false,
            expenseCategoryListener: CategoryListener
        ) {
            categoriesItemExpenseBinding.categories = categories
            itemView.isActivated = isActivated
            categoriesItemExpenseBinding.clickListener = expenseCategoryListener
            categoriesItemExpenseBinding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val categoriesItemExpenseBinding = CategoriesItemExpenseBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(categoriesItemExpenseBinding)
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<Categories>()
        if (!query?.trim().isNullOrEmpty()) {
            unFilteredList?.filter {
                it.CategoryName?.toLowerCase(Locale.getDefault())!!.contains("$query")
            }?.let {
               list.addAll(it)
            }
        } else {
            unFilteredList?.let {
                list.addAll(it)
            }
        }
        submitList(list)
    }

    override fun getItemId(position: Int): Long = position.toLong()
}

class ExpenseCategoryResponseDiffCallback : DiffUtil.ItemCallback<Categories>() {
    override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean {
        return oldItem == newItem
    }
}


class CategoryListener (val onClickListener: (categories: Categories) -> Unit) {
    fun onClick(categories: Categories) = onClickListener(categories)
}
