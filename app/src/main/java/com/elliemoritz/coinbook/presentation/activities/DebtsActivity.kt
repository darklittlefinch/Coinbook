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
import com.elliemoritz.coinbook.databinding.ActivityDebtsBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.adapters.debtsAdapter.DebtsAdapter
import com.elliemoritz.coinbook.presentation.fragments.AddDebtFragment
import com.elliemoritz.coinbook.presentation.states.DebtsState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.DebtsViewModel
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class DebtsActivity : AppCompatActivity(), OnEditingListener {

    private val component by lazy {
        (application as CoinBookApp).component
    }

    private val binding by lazy {
        ActivityDebtsBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DebtsViewModel::class.java]
    }

    private lateinit var debtsAdapter: DebtsAdapter

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
        observeViewModel()
        setRecyclerView()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is DebtsState.Amount -> {
                            binding.tvDebtsAmount.text = it.amount
                        }

                        is DebtsState.Currency -> {
                            debtsAdapter.setCurrency(it.currency)
                        }

                        is DebtsState.DebtsList -> {
                            binding.tvDebtsInfo.visibility = View.GONE
                            debtsAdapter.submitList(it.list)
                        }

                        is DebtsState.NoData -> {
                            binding.tvDebtsAmount.text = it.amount
                            binding.tvDebtsInfo.visibility = View.VISIBLE
                        }

                        is DebtsState.NotEnoughMoney -> {
                            Toast.makeText(
                                this@DebtsActivity,
                                getString(R.string.toast_error_not_enough_money),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        debtsAdapter = DebtsAdapter()
        setRvClickListener()
        setRvLongClickListener()
        setRvSwipeListener()
        binding.rvDebts.adapter = debtsAdapter
    }

    private fun setRvClickListener() {
        debtsAdapter.onDebtClickListener = {
            if (isOnePanelMode()) {
                launchEditOperationActivity(it.id)
            } else {
                launchEditDebtFragment(it.id)
            }
        }
    }

    private fun setRvLongClickListener() {
        debtsAdapter.onDebtLongClickListener = {
            viewModel.changeFinishedStatus(it)
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
                val item = debtsAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeDebt(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvDebts)
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
        binding.ivAddNewDebt.setOnClickListener {
            if (isOnePanelMode()) {
                launchAddOperationActivity()
            } else {
                launchAddDebtFragment()
            }
        }
    }

    private fun isOnePanelMode(): Boolean {
        return binding.fragmentContainerDebts == null
    }

    private fun launchAddOperationActivity() {
        val intent = OperationsActivity.newIntentAdd(
            this,
            OperationsActivity.FRAGMENT_TYPE_DEBT
        )
        startActivity(intent)
    }

    private fun launchEditOperationActivity(id: Int) {
        val intent = OperationsActivity.newIntentEdit(
            this,
            OperationsActivity.FRAGMENT_TYPE_DEBT,
            id
        )
        startActivity(intent)
    }

    private fun launchAddDebtFragment() {
        val fragment = AddDebtFragment.newInstanceAdd()
        beginFragmentTransaction(fragment)
    }

    private fun launchEditDebtFragment(id: Int) {
        val fragment = AddDebtFragment.newInstanceEdit(id)
        beginFragmentTransaction(fragment)
    }

    private fun beginFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_debts, fragment)
            .addToBackStack(AddDebtFragment.NAME)
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
            val intent = Intent(context, DebtsActivity::class.java)
            return intent
        }
    }
}