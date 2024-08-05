package com.elliemoritz.coinbook.presentation.adapters.debtsAdapter

import androidx.recyclerview.widget.DiffUtil
import com.elliemoritz.coinbook.domain.entities.Debt

class DebtsDiffCallback : DiffUtil.ItemCallback<Debt>() {

    override fun areItemsTheSame(oldItem: Debt, newItem: Debt): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Debt, newItem: Debt): Boolean {
        return oldItem == newItem
    }
}