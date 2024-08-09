package com.elliemoritz.coinbook.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ActivityHistoryBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.adapters.historyAdapter.HistoryAdapter
import com.elliemoritz.coinbook.presentation.states.HistoryState
import com.elliemoritz.coinbook.presentation.viewModels.HistoryViewModel
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryActivity : AppCompatActivity() {

    private val component by lazy {
        (application as CoinBookApp).component
    }

    private val binding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HistoryViewModel::class.java]
    }

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setOnClickListeners()
        setRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is HistoryState.NoData -> {
                            binding.tvNoHistoryInfo.visibility = View.VISIBLE
                        }

                        is HistoryState.HasData -> {
                            binding.tvNoHistoryInfo.visibility = View.GONE
                        }

                        is HistoryState.OperationsList -> {
                            historyAdapter.submitList(it.list)
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.rvHistory.adapter = historyAdapter
    }

    private fun setOnClickListeners() {
        setOnBackClickListener()
    }

    private fun setOnBackClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, HistoryActivity::class.java)
            return intent
        }
    }
}
