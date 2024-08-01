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
import com.elliemoritz.coinbook.databinding.ActivityMoneyBoxBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.adapters.moneyBoxAdapter.MoneyBoxAdapter
import com.elliemoritz.coinbook.presentation.fragments.AddMoneyBoxFragment
import com.elliemoritz.coinbook.presentation.fragments.AddMoneyBoxOperationFragment
import com.elliemoritz.coinbook.presentation.states.MoneyBoxState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.MoneyBoxViewModel
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoneyBoxActivity : AppCompatActivity(), OnEditingListener {

    private val component by lazy {
        (application as CoinBookApp).component
    }

    private val binding by lazy {
        ActivityMoneyBoxBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MoneyBoxViewModel::class.java]
    }

    private lateinit var moneyBoxAdapter: MoneyBoxAdapter

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
                        is MoneyBoxState.NoMoneyBox -> {
                            binding.ivAddToMoneyBox.visibility = View.GONE
                            binding.ivRemoveFromMoneyBox.visibility = View.GONE

                            binding.tvMoneyBoxGoalAmountInfo.text = getString(
                                R.string.no_money_box
                            )
                            binding.tvMoneyBoxGoalInfo.text = getString(
                                R.string.no_money_box_hint_create
                            )

                            setOnAddMoneyBoxClickListener()
                        }

                        is MoneyBoxState.Currency -> {
                            moneyBoxAdapter.setCurrency(it.currency)
                        }

                        is MoneyBoxState.Goal -> {
                            binding.ivAddToMoneyBox.visibility = View.VISIBLE
                            binding.ivRemoveFromMoneyBox.visibility = View.VISIBLE

                            binding.tvMoneyBoxGoalAmountInfo.text = getString(
                                R.string.info_money_box_goal_amount,
                                it.goalAmount
                            )
                            binding.tvMoneyBoxGoalInfo.text = getString(
                                R.string.info_money_box_goal,
                                it.goal
                            )

                            setOnEditMoneyBoxClickListener()
                        }

                        is MoneyBoxState.OperationsList -> {
                            moneyBoxAdapter.submitList(it.list)
                        }

                        is MoneyBoxState.TotalAmount -> {
                            binding.tvMoneyBoxAmount.text = it.amount
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        moneyBoxAdapter = MoneyBoxAdapter()
        setRvClickListener()
        setRvSwipeListener()
        binding.rvMoneyBoxOperations.adapter = moneyBoxAdapter
    }

    private fun setRvClickListener() {
        moneyBoxAdapter.onOperationClickListener = {
            if (isOnePanelModel()) {
                launchEditOperationOperationsActivity(it.id)
            } else {
                launchEditMoneyBoxOperationFragment(it.id)
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
                val item = moneyBoxAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeMoneyBoxOperation(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvMoneyBoxOperations)
    }

    private fun setOnClickListeners() {
        setOnBackClickListener()
        setOnAddClickListener()
        setOnRemoveClickListener()
    }

    private fun setOnBackClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setOnAddClickListener() {
        binding.ivAddToMoneyBox.setOnClickListener {
            if (isOnePanelModel()) {
                launchAddOperationOperationsActivity()
            } else {
                launchAddMoneyBoxOperationFragment()
            }
        }
    }

    private fun setOnRemoveClickListener() {
        binding.ivRemoveFromMoneyBox.setOnClickListener {
            if (isOnePanelModel()) {
                launchRemoveOperationOperationsActivity()
            } else {
                launchRemoveMoneyBoxOperationFragment()
            }
        }
    }

    private fun setOnAddMoneyBoxClickListener() {
        binding.ivMoneyBox.setOnClickListener {
            if (isOnePanelModel()) {
                launchAddMoneyBoxOperationsActivity()
            } else {
                launchAddMoneyBoxFragment()
            }
        }
    }

    private fun setOnEditMoneyBoxClickListener() {
        binding.ivMoneyBox.setOnClickListener {
            if (isOnePanelModel()) {
                launchEditMoneyBoxOperationsActivity()
            } else {
                launchEditMoneyBoxFragment()
            }
        }
    }

    private fun isOnePanelModel(): Boolean {
        return binding.fragmentContainerMoneyBox == null
    }

    private fun launchAddOperationOperationsActivity() {
        val intent = OperationsActivity.newIntentAdd(
            this,
            OperationsActivity.FRAGMENT_TYPE_MONEY_BOX_OPERATION
        )
        startActivity(intent)
    }

    private fun launchRemoveOperationOperationsActivity() {
        val intent = OperationsActivity.newIntentRemove(
            this,
            OperationsActivity.FRAGMENT_TYPE_MONEY_BOX_OPERATION
        )
        startActivity(intent)
    }

    private fun launchEditOperationOperationsActivity(operationId: Int) {
        val intent = OperationsActivity.newIntentEdit(
            this,
            OperationsActivity.FRAGMENT_TYPE_MONEY_BOX_OPERATION,
            operationId
        )
        startActivity(intent)
    }

    private fun launchAddMoneyBoxOperationsActivity() {
        val intent = OperationsActivity.newIntentAdd(
            this,
            OperationsActivity.FRAGMENT_TYPE_MONEY_BOX
        )
        startActivity(intent)
    }

    private fun launchEditMoneyBoxOperationsActivity() {
        val intent = OperationsActivity.newIntentEditWithoutId(
            this,
            OperationsActivity.FRAGMENT_TYPE_MONEY_BOX
        )
        startActivity(intent)
    }

    private fun launchAddMoneyBoxOperationFragment() {
        val fragment = AddMoneyBoxOperationFragment.newInstanceAdd()
        beginOperationFragmentTransaction(fragment)
    }

    private fun launchRemoveMoneyBoxOperationFragment() {
        val fragment = AddMoneyBoxOperationFragment.newInstanceRemove()
        beginOperationFragmentTransaction(fragment)
    }

    private fun launchEditMoneyBoxOperationFragment(operationId: Int) {
        val fragment = AddMoneyBoxOperationFragment.newInstanceEdit(operationId)
        beginOperationFragmentTransaction(fragment)
    }

    private fun launchAddMoneyBoxFragment() {
        val fragment = AddMoneyBoxFragment.newInstanceAdd()
        beginMoneyBoxFragmentTransaction(fragment)
    }

    private fun launchEditMoneyBoxFragment() {
        val fragment = AddMoneyBoxFragment.newInstanceEdit()
        beginMoneyBoxFragmentTransaction(fragment)
    }

    private fun beginOperationFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_money_box, fragment)
            .addToBackStack(AddMoneyBoxOperationFragment.NAME)
            .commit()
    }

    private fun beginMoneyBoxFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_money_box, fragment)
            .addToBackStack(AddMoneyBoxFragment.NAME)
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
            val intent = Intent(context, MoneyBoxActivity::class.java)
            return intent
        }
    }
}
