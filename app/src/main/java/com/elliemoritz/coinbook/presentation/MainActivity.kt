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
        setIncomeListener()
        setExpensesListener()
        setMoneyBoxListener()
        setDebtsListener()
        setLimitsListener()
        setAlarmsListener()
        setHistoryListener()
        setSettingsListener()
    }

    private fun setIncomeListener() {
        binding.cvIncome.setOnClickListener {
            val intent = IncomeActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setExpensesListener() {
        binding.cvExpenses.setOnClickListener {
            val intent = ExpensesActivity.newIntent(this)
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

    private fun setLimitsListener() {
        binding.cvLimits.setOnClickListener {
            val intent = LimitsActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setAlarmsListener() {
        binding.cvAlarms.setOnClickListener {
            val intent = AlarmsActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setHistoryListener() {
        binding.cvHistory.setOnClickListener {
            val intent = HistoryActivity.newIntent(this)
            startActivity(intent)
        }
    }

    private fun setSettingsListener() {
        binding.cvSettings.setOnClickListener {
            val intent = SettingsActivity.newIntent(this)
            startActivity(intent)
        }
    }
}