package com.elliemoritz.coinbook.presentation.adapters.moneyBoxAdapter

import androidx.recyclerview.widget.DiffUtil
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation

class MoneyBoxDiffCallback : DiffUtil.ItemCallback<MoneyBoxOperation>() {

    override fun areItemsTheSame(
        oldItem: MoneyBoxOperation,
        newItem: MoneyBoxOperation
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MoneyBoxOperation,
        newItem: MoneyBoxOperation
    ): Boolean {
        return oldItem == newItem
    }
}