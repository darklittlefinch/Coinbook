package com.elliemoritz.coinbook.presentation.adapters.historyAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ItemHistoryBinding
import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import com.elliemoritz.coinbook.presentation.util.formatAmountWithSign
import com.elliemoritz.coinbook.presentation.util.formatDate
import com.elliemoritz.coinbook.presentation.util.formatTime

class HistoryAdapter : ListAdapter<Operation, OperationViewHolder>(OperationsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OperationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operation = getItem(position)

        with(holder.binding) {
            tvHistoryAmount.text = formatAmountWithSign(
                operation.amount,
                operation.currency,
                operation.type
            )
            tvHistoryDate.text = formatDate(operation.dateTimeMillis)
            tvHistoryTime.text = formatTime(operation.dateTimeMillis)

            val colorResId = when (operation.type) {
                Type.INCOME -> R.color.dark_green
                Type.EXPENSE -> R.color.dark_red
            }
            val color = ContextCompat.getColor(root.context, colorResId)
            tvHistoryAmount.setTextColor(color)

            tvHistoryInfo.text = when (operation.operationForm) {
                OperationForm.MONEY_BOX -> {
                    when (operation.type) {

                        Type.INCOME -> root.context.getString(
                            R.string.operation_info_money_box_income
                        )

                        Type.EXPENSE -> root.context.getString(
                            R.string.operation_info_money_box_expense
                        )
                    }
                }

                OperationForm.DEBT -> {
                    when (operation.type) {

                        Type.INCOME -> root.context.getString(
                            R.string.operation_info_debt_income
                        )

                        Type.EXPENSE -> root.context.getString(
                            R.string.operation_info_debt_expense
                        )
                    }
                }

                else -> {
                    operation.info
                }
            }
        }
    }
}
