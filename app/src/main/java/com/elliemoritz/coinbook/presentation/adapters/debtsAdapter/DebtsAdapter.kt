package com.elliemoritz.coinbook.presentation.adapters.debtsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.elliemoritz.coinbook.databinding.ItemDebtBinding
import com.elliemoritz.coinbook.domain.entities.Debt
import com.elliemoritz.coinbook.presentation.util.formatAmount
import com.elliemoritz.coinbook.presentation.util.formatDate

class DebtsAdapter : ListAdapter<Debt, DebtViewHolder>(DebtsDiffCallback()) {

    var onDebtClickListener: ((Debt) -> Unit)? = null
    val onDebtLongClickListener: ((Debt) -> Unit)? = null

    private lateinit var currency: String

    fun setCurrency(newCurrency: String) {
        currency = newCurrency
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtViewHolder {
        val binding = ItemDebtBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DebtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DebtViewHolder, position: Int) {
        val debt = getItem(position)

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