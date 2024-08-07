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
import com.elliemoritz.coinbook.databinding.ActivityExpensesBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.adapters.expensesAdapter.ExpensesAdapter
import com.elliemoritz.coinbook.presentation.fragments.AddCategoryFragment
import com.elliemoritz.coinbook.presentation.fragments.AddExpenseFragment
import com.elliemoritz.coinbook.presentation.states.ExpenseState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.util.OnNotEnoughMoneyListener
import com.elliemoritz.coinbook.presentation.viewModels.ExpenseViewModel
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpensesActivity : AppCompatActivity(),
    OnEditingListener,
    OnNotEnoughMoneyListener {

    private val component by lazy {
        (application as CoinBookApp).component
    }

    private val binding by lazy {
        ActivityExpensesBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ExpenseViewModel::class.java]
    }

    private lateinit var expensesAdapter: ExpensesAdapter

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
                        is ExpenseState.NoData -> {
                            binding.tvExpensesInfo.visibility = View.VISIBLE
                            binding.tvExpensesInfo.text = getString(R.string.no_expenses)
                        }

                        is ExpenseState.HasData -> {
                            binding.tvExpensesInfo.visibility = View.GONE
                        }

                        is ExpenseState.Currency -> {
                            expensesAdapter.setCurrency(it.currency)
                        }

                        is ExpenseState.ExpensesList -> {
                            expensesAdapter.submitList(it.list)
                        }

                        is ExpenseState.Amount -> {
                            binding.tvExpensesAmount.text = it.amount
                        }

                        is ExpenseState.NoCategoriesError -> {
                            Toast.makeText(
                                this@ExpensesActivity,
                                getString(R.string.toast_create_category),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ExpenseState.PermitAddExpense -> {
                            if (isOnePanelModel()) {
                                launchAddOperationsActivity()
                            } else {
                                launchAddExpenseFragment()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        expensesAdapter = ExpensesAdapter()
        setRvClickListener()
        setRvSwipeListener()
        binding.rvExpenses.adapter = expensesAdapter
    }

    private fun setRvClickListener() {
        expensesAdapter.onIncomeClickListener = {
            if (isOnePanelModel()) {
                launchEditOperationsActivity(it.id)
            } else {
                launchEditExpenseFragment(it.id)
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
                val item = expensesAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeExpense(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvExpenses)
    }

    private fun setOnClickListeners() {
        setOnBackClickListener()
        setOnAddExpenseClickListener()
        setOnAddCategoryClickListener()
    }

    private fun setOnBackClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setOnAddExpenseClickListener() {
        binding.ivAddNewExpense.setOnClickListener {
            viewModel.checkCategories()
        }
    }

    private fun setOnAddCategoryClickListener() {
        binding.buttonExpensesAddCategory.setOnClickListener {
            if (isOnePanelModel()) {
                launchAddCategoryOperationsActivity()
            } else {
                launchAddCategoryFragment()
            }
        }
    }

    private fun isOnePanelModel(): Boolean {
        return binding.fragmentContainerExpenses == null
    }

    private fun launchAddOperationsActivity() {
        val intent = OperationsActivity.newIntentAdd(
            this,
            OperationsActivity.FRAGMENT_TYPE_EXPENSE
        )
        startActivity(intent)
    }

    private fun launchEditOperationsActivity(id: Int) {
        val intent = OperationsActivity.newIntentEdit(
            this,
            OperationsActivity.FRAGMENT_TYPE_EXPENSE,
            id
        )
        startActivity(intent)
    }

    private fun launchAddCategoryOperationsActivity() {
        val intent = OperationsActivity.newIntentAdd(
            this,
            OperationsActivity.FRAGMENT_TYPE_CATEGORY
        )
        startActivity(intent)
    }

    private fun launchAddExpenseFragment() {
        val fragment = AddExpenseFragment.newInstanceAdd()
        beginFragmentTransaction(fragment)
    }

    private fun launchEditExpenseFragment(id: Int) {
        val fragment = AddExpenseFragment.newInstanceEdit(id)
        beginFragmentTransaction(fragment)
    }

    private fun launchAddCategoryFragment() {
        val fragment = AddCategoryFragment.newInstanceAdd()
        beginFragmentTransaction(fragment)
    }

    private fun beginFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_expenses, fragment)
            .addToBackStack(AddExpenseFragment.NAME)
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

    override fun onNotEnoughMoney() {
        Toast.makeText(
            this,
            getString(R.string.toast_error_not_enough_money),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, ExpensesActivity::class.java)
            return intent
        }
    }
}