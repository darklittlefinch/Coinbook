package com.elliemoritz.coinbook.presentation.adapters.moneyBoxAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ItemMoneyBoxBinding
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.presentation.util.formatAmountWithSign
import com.elliemoritz.coinbook.presentation.util.formatDate
import com.elliemoritz.coinbook.presentation.util.formatTime

class MoneyBoxAdapter : ListAdapter<MoneyBoxOperation, MoneyBoxViewHolder>(MoneyBoxDiffCallback()) {

    var onOperationClickListener: ((MoneyBoxOperation) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyBoxViewHolder {
        val binding = ItemMoneyBoxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MoneyBoxViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoneyBoxViewHolder, position: Int) {
        val moneyBoxOperation = getItem(position)

        with(holder.binding) {

            val oppositeType = when (moneyBoxOperation.type) {
                Type.INCOME -> Type.EXPENSE
                Type.EXPENSE -> Type.INCOME
            }

            tvMoneyBoxAmount.text = formatAmountWithSign(
                moneyBoxOperation.amount,
                moneyBoxOperation.currency,
                oppositeType
            )

            tvMoneyBoxDate.text = formatDate(moneyBoxOperation.dateTimeMillis)
            tvMoneyBoxTime.text = formatTime(moneyBoxOperation.dateTimeMillis)

            val colorResId = when (oppositeType) {
                Type.INCOME -> R.color.dark_green
                Type.EXPENSE -> R.color.dark_red
            }

            val color = ContextCompat.getColor(holder.binding.root.context, colorResId)
            tvMoneyBoxAmount.setTextColor(color)

            cvItemMoneyBox.setOnClickListener {
                onOperationClickListener?.invoke(moneyBoxOperation)
            }
        }
    }
}
