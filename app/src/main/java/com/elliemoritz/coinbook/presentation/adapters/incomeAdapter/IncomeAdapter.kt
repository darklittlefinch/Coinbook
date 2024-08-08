package com.elliemoritz.coinbook.presentation.adapters.incomeAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elliemoritz.coinbook.databinding.ItemIncomeBinding
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.presentation.util.formatAmountWithSign
import com.elliemoritz.coinbook.presentation.util.formatDate
import com.elliemoritz.coinbook.presentation.util.formatTime

class IncomeAdapter : ListAdapter<Income, IncomeViewHolder>(IncomeDiffCallback()) {

    var onIncomeClickListener: ((Income) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val binding = ItemIncomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IncomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val income = getItem(position)

        with(holder.binding) {
            tvIncomeSource.text = income.source
            tvIncomeDate.text = formatDate(income.dateTimeMillis)
            tvIncomeTime.text = formatTime(income.dateTimeMillis)
            tvIncomeAmount.text = formatAmountWithSign(
                income.amount,
                income.currency,
                Type.INCOME
            )

            cvItemIncome.setOnClickListener {
                onIncomeClickListener?.invoke(income)
            }
        }
    }
}
