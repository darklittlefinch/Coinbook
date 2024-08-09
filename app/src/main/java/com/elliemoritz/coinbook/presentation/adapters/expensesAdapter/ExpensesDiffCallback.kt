package com.elliemoritz.coinbook.presentation.adapters.expensesAdapter

import androidx.recyclerview.widget.DiffUtil
import com.elliemoritz.coinbook.domain.entities.operations.Expense

class ExpensesDiffCallback : DiffUtil.ItemCallback<Expense>() {

    override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
        return oldItem == newItem
    }
}