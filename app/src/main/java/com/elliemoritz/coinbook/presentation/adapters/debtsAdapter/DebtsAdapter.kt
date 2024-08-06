package com.elliemoritz.coinbook.presentation.adapters.debtsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ItemDebtBinding
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
        val binding = ItemDebtBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DebtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DebtViewHolder, position: Int) {
        val debt = getItem(position)

        holder.binding.cvItemDebt.setOnClickListener {
            onDebtClickListener?.invoke(debt)
        }

        holder.binding.cvItemDebt.setOnLongClickListener {
            onDebtLongClickListener?.invoke(debt)
            true
        }

        holder.binding.tvDebtCreditor.text = debt.creditor
        holder.binding.tvDebtDate.text = formatDate(debt.startedMillis)
        holder.binding.tvDebtAmount.text = formatAmount(debt.amount, currency)

        if (debt.finished) {
            val colorResId = R.color.gray
            val color = ContextCompat.getColor(holder.binding.root.context, colorResId)
            holder.binding.tvDebtCreditor.setTextColor(color)
            holder.binding.tvDebtDate.setTextColor(color)
            holder.binding.tvDebtAmount.setTextColor(color)
        } else {
            val blackColorResId = R.color.black
            val blackColor = ContextCompat.getColor(
                holder.binding.root.context,
                blackColorResId
            )
            holder.binding.tvDebtCreditor.setTextColor(blackColor)
            holder.binding.tvDebtDate.setTextColor(blackColor)

            val redColorResId = R.color.dark_red
            val redColor = ContextCompat.getColor(
                holder.binding.root.context,
                redColorResId
            )
            holder.binding.tvDebtAmount.setTextColor(redColor)

        }
    }
}