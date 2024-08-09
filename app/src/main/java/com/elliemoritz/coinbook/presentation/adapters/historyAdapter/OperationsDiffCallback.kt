package com.elliemoritz.coinbook.presentation.adapters.historyAdapter

import androidx.recyclerview.widget.DiffUtil
import com.elliemoritz.coinbook.domain.entities.operations.Operation

class OperationsDiffCallback : DiffUtil.ItemCallback<Operation>() {

    override fun areItemsTheSame(oldItem: Operation, newItem: Operation): Boolean {
        return oldItem.operationId == newItem.operationId
                && oldItem.operationForm == newItem.operationForm
    }

    override fun areContentsTheSame(oldItem: Operation, newItem: Operation): Boolean {
        return oldItem == newItem
    }
}