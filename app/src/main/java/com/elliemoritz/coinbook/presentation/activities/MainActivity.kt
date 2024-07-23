package com.elliemoritz.coinbook.presentation.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ActivityMainBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.MainState
import com.elliemoritz.coinbook.presentation.viewModels.MainViewModel
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import kotlinx.coroutines.launch
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
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    binding.mainProgressBar?.visibility = View.GONE
                    when (it) {
                        is MainState.Loading -> binding.mainProgressBar?.visibility = View.VISIBLE
                        is MainState.Balance -> binding.tvBalanceNumber.text = it.amount
                        is MainState.Income -> binding.tvIncomeNumber.text = it.amount
                        is MainState.Expenses -> binding.tvExpensesNumber.text = it.amount
                        is MainState.MoneyBox -> {
                            binding.tvMoneyBoxAmount.text = it.amount
                            val backgroundColor = getMoneyBoxColor(it.wasStarted)
                            binding.cvMoneyBox.background.setTint(backgroundColor)
                        }

                        is MainState.Debts -> {
                            binding.tvDebtsAmount.text = it.amount
                            val backgroundColor = getDebtsColor(it.userHasDebts)
                            binding.cvDebts.background.setTint(backgroundColor)
                        }

                        is MainState.Limits -> {
                            val backgroundColor = getLimitsColor(it.userHasLimits)
                            binding.cvLimits.background.setTint(backgroundColor)
                        }

                        is MainState.Alarms -> {
                            val backgroundColor = getAlarmsColor(it.userHasAlarms)
                            binding.cvAlarms.background.setTint(backgroundColor)
                        }
                    }
                }
            }
        }
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

    // SETTINGS COLORS

    private fun getMoneyBoxColor(hasMoneyBox: Boolean): Int {
        val colorResId = if (hasMoneyBox) {
            R.color.yellow
        } else {
            R.color.creamy
        }
        return ContextCompat.getColor(this, colorResId)
    }

    private fun getDebtsColor(hasDebts: Boolean): Int {
        val colorResId = if (hasDebts) {
            R.color.orange
        } else {
            R.color.creamy
        }
        return ContextCompat.getColor(this, colorResId)
    }

    private fun getLimitsColor(hasLimits: Boolean): Int {
        val colorResId = if (hasLimits) {
            R.color.orange
        } else {
            R.color.creamy
        }
        return ContextCompat.getColor(this, colorResId)
    }

    private fun getAlarmsColor(hasAlarms: Boolean): Int {
        val colorResId = if (hasAlarms) {
            R.color.purple
        } else {
            R.color.creamy
        }
        return ContextCompat.getColor(this, colorResId)
    }

    // CLICK LISTENERS

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