package com.elliemoritz.coinbook.presentation.adapters.debtsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elliemoritz.coinbook.databinding.ItemDebtBinding
import com.elliemoritz.coinbook.databinding.ItemDebtFinishedBinding
import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.presentation.util.formatAmount
import com.elliemoritz.coinbook.presentation.util.formatDate

class DebtsAdapter : ListAdapter<Debt, DebtViewHolder>(DebtsDiffCallback()) {

    var onDebtClickListener: ((Debt) -> Unit)? = null
    var onDebtLongClickListener: ((Debt) -> Unit)? = null

    private lateinit var currency: String

    fun setCurrency(newCurrency: String) {
        currency = newCurrency
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtViewHolder {
        val binding = when (viewType) {
            VIEW_TYPE_ACTIVE -> {
                ItemDebtBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            VIEW_TYPE_FINISHED -> {
                ItemDebtFinishedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            }

            else -> throw RuntimeException("DebtsAdapter: Unknown view type \"$viewType\"")
        }
        return DebtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DebtViewHolder, position: Int) {
        val debt = getItem(position)

        when (holder.binding) {
            is ItemDebtBinding -> {
                with(holder.binding) {
                    tvDebtCreditor.text = debt.creditor
                    tvDebtDate.text = formatDate(debt.startedMillis)
                    tvDebtAmount.text = formatAmount(debt.amount, currency)

                    cvItemDebt.setOnClickListener {
                        onDebtClickListener?.invoke(debt)
                    }

                    cvItemDebt.setOnLongClickListener {
                        onDebtLongClickListener?.invoke(debt)
                        true
                    }
                }
            }

            is ItemDebtFinishedBinding -> {
                with(holder.binding) {
                    tvDebtCreditor.text = debt.creditor
                    tvDebtDate.text = formatDate(debt.startedMillis)
                    tvDebtAmount.text = formatAmount(debt.amount, currency)

                    cvItemDebt.setOnClickListener {
                        onDebtClickListener?.invoke(debt)
                    }

                    cvItemDebt.setOnLongClickListener {
                        onDebtLongClickListener?.invoke(debt)
                        true
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.finished) {
            VIEW_TYPE_FINISHED
        } else {
            VIEW_TYPE_ACTIVE
        }
    }

    companion object {
        private const val VIEW_TYPE_ACTIVE = 100
        private const val VIEW_TYPE_FINISHED = 101
    }
}
