package com.elliemoritz.coinbook.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ActivitySettingsBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.SettingsState
import com.elliemoritz.coinbook.presentation.viewModels.SettingsViewModel
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
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

        val currencies = resources.getStringArray(R.array.currencies)
        viewModel.setCurrencyPosition(currencies)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is SettingsState.Balance -> binding.etBalance.hint = it.value
                        is SettingsState.CurrencyPosition -> {
                            binding.spinnerCurrency.setSelection(it.value)
                        }

                        is SettingsState.Notifications -> {
                            binding.toggleSettingsNotifications.isChecked = it.enabled
                        }

                        is SettingsState.NotificationsSounds -> {
                            binding.toggleSettingsNotificationsSound.isChecked = it.enabled
                        }

                        is SettingsState.Finish -> finish()
                    }
                }
            }
        }
    }

    private fun setOnClickListeners() {
        setOnBackClickListener()
        setOnDoneClickListener()
    }

    private fun setOnBackClickListener() {
        binding.ivBack.setOnClickListener {
            viewModel.closeSettings()
        }
    }

    private fun setOnDoneClickListener() {
        binding.buttonDone.setOnClickListener {
            viewModel.setNewSettings(
                binding.etBalance.text.toString(),
                binding.spinnerCurrency.selectedItem.toString(),
                binding.toggleSettingsNotifications.isChecked,
                binding.toggleSettingsNotificationsSound.isChecked
            )
            Toast.makeText(
                this,
                R.string.toast_settings_saved,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, SettingsActivity::class.java)
            return intent
        }
    }
}