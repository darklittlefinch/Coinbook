package com.elliemoritz.coinbook.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ActivityOperationsBinding

class OperationsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityOperationsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setMode()
    }

    private fun setMode() {
        val mode = intent.getStringExtra(EXTRA_MODE)
        val colorResId: Int
        when (mode) {
            EXPENSES_MODE -> {
                binding.tvOperationTitle.setText(R.string.expenses_title)
                colorResId = R.color.red
            }

            INCOME_MODE -> {
                binding.tvOperationTitle.setText(R.string.income_title)
                colorResId = R.color.green
            }

            MONEY_BOX_MODE -> {
                binding.tvOperationTitle.setText(R.string.money_box_title)
                colorResId = R.color.yellow
            }

            DEBTS_MODE -> {
                binding.tvOperationTitle.setText(R.string.debts_title)
                colorResId = R.color.orange
            }

            else -> {
                throw RuntimeException("Unknown mode")
            }
        }

        val color = ContextCompat.getColor(this, colorResId)
        binding.tvOperationTitle.setBackgroundColor(color)
    }

    companion object {
        private const val EXTRA_MODE = "mode"
        const val EXPENSES_MODE = "expenses"
        const val INCOME_MODE = "income"
        const val MONEY_BOX_MODE = "money_box"
        const val DEBTS_MODE = "debts"

        fun newIntent(context: Context, mode: String): Intent {
            val intent = Intent(context, OperationsActivity::class.java)
            intent.putExtra(EXTRA_MODE, mode)
            return intent
        }
    }
}