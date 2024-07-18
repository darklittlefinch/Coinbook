package com.elliemoritz.coinbook.presentation.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ActivityMainBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.viewModels.MainViewModel
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as CoinBookApp).component
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

        component.inject(this)

        observeViewModel()
        setOnClickListeners()
        viewModel.setValues()
    }

    private fun observeViewModel() {
        observeBalanceAmount()
        observeIncomeAmount()
        observeExpensesAmount()
        observeHasMoneyBox()
        observeMoneyBoxAmount()
        observeHasDebts()
        observeDebtsAmount()
        observeHasLimits()
        observeHasAlarms()
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

    private fun observeBalanceAmount() {
        viewModel.balanceAmount.observe(this) {
            binding.tvBalanceNumber.text = it
        }
    }

    private fun observeIncomeAmount() {
        viewModel.incomeAmount.observe(this) {
            binding.tvIncomeNumber.text = it
        }
    }

    private fun observeExpensesAmount() {
        viewModel.expensesAmount.observe(this) {
            binding.tvExpensesNumber.text = it
        }
    }

    private fun observeHasMoneyBox() {
        viewModel.hasMoneyBox.observe(this) {
            val colorResId = if (it) {
                R.color.yellow
            } else {
                R.color.creamy
            }
            val color = ContextCompat.getColor(this, colorResId)
            binding.cvMoneyBox.background.setTint(color)
        }
    }

    private fun observeMoneyBoxAmount() {
        viewModel.moneyBoxAmount.observe(this) {
            binding.tvMoneyBoxAmount.text = it
        }
    }

    private fun observeHasDebts() {
        viewModel.hasDebts.observe(this) {
            val colorResId = if (it) {
                R.color.orange
            } else {
                R.color.creamy
            }
            val color = ContextCompat.getColor(this, colorResId)
            binding.cvDebts.background.setTint(color)
        }
    }

    private fun observeDebtsAmount() {
        viewModel.debtsAmount.observe(this) {
            binding.tvDebtsAmount.text = it
        }
    }

    private fun observeHasLimits() {
        viewModel.hasLimits.observe(this) {
            val colorResId = if (it) {
                R.color.orange
            } else {
                R.color.creamy
            }
            val color = ContextCompat.getColor(this, colorResId)
            binding.cvLimits.background.setTint(color)
        }
    }

    private fun observeHasAlarms() {
        viewModel.hasAlarms.observe(this) {
            val colorResId = if (it) {
                R.color.purple
            } else {
                R.color.creamy
            }
            val color = ContextCompat.getColor(this, colorResId)
            binding.cvLimits.background.setTint(color)
        }
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