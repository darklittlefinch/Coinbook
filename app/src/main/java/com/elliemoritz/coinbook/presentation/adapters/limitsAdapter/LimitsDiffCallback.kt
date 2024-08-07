package com.elliemoritz.coinbook.presentation.adapters.limitsAdapter

import androidx.recyclerview.widget.DiffUtil
import com.elliemoritz.coinbook.domain.entities.Limit

class LimitsDiffCallback : DiffUtil.ItemCallback<Limit>() {

    override fun areItemsTheSame(oldItem: Limit, newItem: Limit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Limit, newItem: Limit): Boolean {
        return oldItem == newItem
    }
}