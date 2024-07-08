package com.elliemoritz.coinbook.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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

        setListeners()
    }

    private fun setListeners() {
        setExpensesListener()
        setIncomeListener()
        setMoneyBoxListener()
        setDebtsListener()
    }

    private fun setIncomeListener() {
        binding.cvIncome.setOnClickListener {
            val intent = OperationsActivity.newIntent(
                this,
                OperationsActivity.INCOME_MODE
            )
            startActivity(intent)
        }
    }

    private fun setExpensesListener() {
        binding.cvExpenses.setOnClickListener {
            val intent = OperationsActivity.newIntent(
                this,
                OperationsActivity.EXPENSES_MODE
            )
            startActivity(intent)
        }
    }

    private fun setMoneyBoxListener() {
        binding.cvMoneyBox.setOnClickListener {
            val intent = MoneyBoxActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setDebtsListener() {
        binding.cvDebts.setOnClickListener {
            val intent = DebtsActivity.newIntent(this)
            startActivity(intent)
        }
    }
}