package com.elliemoritz.coinbook.presentation.adapters.limitsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ItemLimitBinding
import com.elliemoritz.coinbook.domain.entities.Limit
import com.elliemoritz.coinbook.presentation.util.calculatePercent
import com.elliemoritz.coinbook.presentation.util.formatAmount

class LimitsAdapter : ListAdapter<Limit, LimitViewHolder>(LimitsDiffCallback()) {

    var onLimitClickListener: ((Limit) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LimitViewHolder {
        val binding = ItemLimitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LimitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LimitViewHolder, position: Int) {
        val limit = getItem(position)

        with(holder.binding) {
            tvLimitCategory.text = limit.categoryName

            val realAmount = formatAmount(limit.realAmount, limit.currency)
            val limitAmount = formatAmount(limit.limitAmount, limit.currency)

            tvSpentMoneyAmount.text = root.context.getString(
                R.string.amount_limit,
                realAmount,
                limitAmount
            )

            val percent = calculatePercent(limit.realAmount, limit.limitAmount)
            progressBarLimit.progress = if (percent <= MAX_PROGRESS_VALUE) {
                percent
            } else {
                MAX_PROGRESS_VALUE
            }

            cvItemLimit.setOnClickListener {
                onLimitClickListener?.invoke(limit)
            }
        }
    }

    companion object {
        private const val MAX_PROGRESS_VALUE = 100
    }
}
