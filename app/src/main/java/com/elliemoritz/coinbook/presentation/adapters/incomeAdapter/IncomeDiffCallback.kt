package com.elliemoritz.coinbook.presentation.adapters.incomeAdapter

import androidx.recyclerview.widget.DiffUtil
import com.elliemoritz.coinbook.domain.entities.operations.Income

class IncomeDiffCallback: DiffUtil.ItemCallback<Income>() {

    override fun areItemsTheSame(oldItem: Income, newItem: Income): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Income, newItem: Income): Boolean {
        return oldItem == newItem
    }
}