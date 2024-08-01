package com.elliemoritz.coinbook.presentation.adapters.expensesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elliemoritz.coinbook.databinding.ItemExpenseBinding
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.presentation.util.formatAmountWithSign
import com.elliemoritz.coinbook.presentation.util.formatDate
import com.elliemoritz.coinbook.presentation.util.formatTime

class ExpensesAdapter : ListAdapter<Expense, ExpenseViewHolder>(ExpensesDiffCallback()) {

    var onIncomeClickListener: ((Expense) -> Unit)? = null

    private lateinit var currency: String

    fun setCurrency(newCurrency: String) {
        currency = newCurrency
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)

        with(holder.binding) {
            tvExpenseCategory.text = expense.expCategoryName
            tvExpenseDate.text = formatDate(expense.dateTimeMillis)
            tvExpenseTime.text = formatTime(expense.dateTimeMillis)
            tvExpenseAmount.text = formatAmountWithSign(expense.amount, currency, expense.type)

            cvItemExpense.setOnClickListener {
                onIncomeClickListener?.invoke(expense)
            }
        }
    }
}