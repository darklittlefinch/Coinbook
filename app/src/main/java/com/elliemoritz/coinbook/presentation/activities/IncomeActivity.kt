package com.elliemoritz.coinbook.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ActivityIncomeBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.adapters.incomeAdapter.IncomeAdapter
import com.elliemoritz.coinbook.presentation.fragments.AddIncomeFragment
import com.elliemoritz.coinbook.presentation.states.IncomeState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.IncomeViewModel
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class IncomeActivity : AppCompatActivity(), OnEditingListener {

    private val component by lazy {
        (application as CoinBookApp).component
    }

    private val binding by lazy {
        ActivityIncomeBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[IncomeViewModel::class.java]
    }

    private lateinit var incomeAdapter: IncomeAdapter

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
                        is IncomeState.NoData -> {
                            binding.tvNoIncomeInfo.visibility = View.VISIBLE
                            binding.tvIncomeAmount.text = it.amount
                        }

                        is IncomeState.Currency -> {
                            incomeAdapter.setCurrency(it.currency)
                        }

                        is IncomeState.IncomeList -> {
                            binding.tvNoIncomeInfo.visibility = View.GONE
                            incomeAdapter.submitList(it.list)
                        }

                        is IncomeState.Amount -> {
                            binding.tvIncomeAmount.text = it.amount
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        incomeAdapter = IncomeAdapter()
        setRvClickListener()
        setRvSwipeListener()
        binding.rvIncome.adapter = incomeAdapter
    }

    private fun setRvClickListener() {
        incomeAdapter.onIncomeClickListener = {
            if (isOnePanelModel()) {
                launchEditOperationsActivity(it.id)
            } else {
                launchEditIncomeFragment(it.id)
            }
        }
    }

    private fun setRvSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = incomeAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeIncome(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvIncome)
    }

    private fun setOnClickListeners() {
        setOnBackClickListener()
        setOnAddClickListener()
    }

    private fun setOnBackClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setOnAddClickListener() {
        binding.ivAddNewIncome.setOnClickListener {

            if (isOnePanelModel()) {
                launchAddOperationsActivity()
            } else {
                launchAddIncomeFragment()
            }
        }
    }

    private fun isOnePanelModel(): Boolean {
        return binding.fragmentContainerIncome == null
    }

    private fun launchAddOperationsActivity() {
        val intent = OperationsActivity.newIntentAdd(
            this,
            OperationsActivity.FRAGMENT_TYPE_INCOME
        )
        startActivity(intent)
    }

    private fun launchEditOperationsActivity(incomeId: Int) {
        val intent = OperationsActivity.newIntentEdit(
            this,
            OperationsActivity.FRAGMENT_TYPE_INCOME,
            incomeId
        )
        startActivity(intent)
    }

    private fun launchAddIncomeFragment() {
        val fragment = AddIncomeFragment.newInstanceAdd()
        beginFragmentTransaction(fragment)
    }

    private fun launchEditIncomeFragment(incomeId: Int) {
        val fragment = AddIncomeFragment.newInstanceEdit(incomeId)
        beginFragmentTransaction(fragment)
    }

    private fun beginFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_income, fragment)
            .addToBackStack(AddIncomeFragment.NAME)
            .commit()
    }

    override fun onFinished() {
        Toast.makeText(
            this,
            getString(R.string.toast_success),
            Toast.LENGTH_SHORT
        ).show()

        onBackPressedDispatcher.onBackPressed()
    }

    override fun onEmptyFields() {
        Toast.makeText(
            this,
            getString(R.string.toast_error_empty_fields),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onIncorrectNumber() {
        Toast.makeText(
            this,
            getString(R.string.toast_error_incorrect_number),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNoChanges() {
        Toast.makeText(
            this,
            getString(R.string.toast_error_no_changes),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, IncomeActivity::class.java)
            return intent
        }
    }
}
