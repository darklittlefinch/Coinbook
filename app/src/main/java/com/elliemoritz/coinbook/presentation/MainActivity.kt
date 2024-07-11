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

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        setOnBalanceClickListener()
        setOnIncomeClickListener()
        setOnPlusClickListener()
        setOnExpensesClickListener()
        setOnMinusClickListener()
        setOnMoneyBoxClickListener()
        setOnDebtsClickListener()
        setOnLimitsClickListener()
        setOnAlarmsClickListener()
        setOnHistoryClickListener()
        setOnSettingsClickListener()
    }

    private fun setOnBalanceClickListener() {
        binding.cvBalance.setOnClickListener {
            val intent = OperationsActivity.newIntent(this, OperationsActivity.MODE_BALANCE)
            startActivity(intent)
        }
    }

    private fun setOnIncomeClickListener() {
        binding.cvIncome.setOnClickListener {
            val intent = IncomeActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setOnPlusClickListener() {
        binding.cvPlus.setOnClickListener {
            val intent = OperationsActivity.newIntent(this, OperationsActivity.MODE_INCOME)
            startActivity(intent)
        }
    }

    private fun setOnExpensesClickListener() {
        binding.cvExpenses.setOnClickListener {
            val intent = ExpensesActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setOnMinusClickListener() {
        binding.cvMinus.setOnClickListener {
            val intent = OperationsActivity.newIntent(this, OperationsActivity.MODE_EXPENSE)
            startActivity(intent)
        }
    }

    private fun setOnMoneyBoxClickListener() {
        binding.cvMoneyBox.setOnClickListener {
            val intent = MoneyBoxActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setOnDebtsClickListener() {
        binding.cvDebts.setOnClickListener {
            val intent = DebtsActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setOnLimitsClickListener() {
        binding.cvLimits.setOnClickListener {
            val intent = LimitsActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setOnAlarmsClickListener() {
        binding.cvAlarms.setOnClickListener {
            val intent = AlarmsActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setOnHistoryClickListener() {
        binding.cvHistory.setOnClickListener {
            val intent = HistoryActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setOnSettingsClickListener() {
        binding.cvSettings.setOnClickListener {
            val intent = SettingsActivity.newIntent(this)
            startActivity(intent)
        }
    }
}