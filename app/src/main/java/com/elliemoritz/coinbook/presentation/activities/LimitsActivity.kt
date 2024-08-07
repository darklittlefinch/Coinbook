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
import com.elliemoritz.coinbook.databinding.ActivityLimitsBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.adapters.limitsAdapter.LimitsAdapter
import com.elliemoritz.coinbook.presentation.fragments.AddCategoryFragment
import com.elliemoritz.coinbook.presentation.fragments.AddLimitFragment
import com.elliemoritz.coinbook.presentation.states.LimitsState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.LimitsViewModel
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class LimitsActivity : AppCompatActivity(), OnEditingListener {

    private val component by lazy {
        (application as CoinBookApp).component
    }

    private val binding by lazy {
        ActivityLimitsBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LimitsViewModel::class.java]
    }

    private lateinit var limitsAdapter: LimitsAdapter

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
                        is LimitsState.NoData -> {
                            binding.tvLimitsInfo.visibility = View.VISIBLE
                        }

                        LimitsState.HasData -> {
                            binding.tvLimitsInfo.visibility = View.GONE
                        }

                        is LimitsState.Currency -> {
                            limitsAdapter.setCurrency(it.currency)
                        }

                        is LimitsState.LimitsList -> {
                            limitsAdapter.submitList(it.list)
                        }

                        is LimitsState.NoCategoriesError -> {
                            Toast.makeText(
                                this@LimitsActivity,
                                getString(R.string.toast_create_category),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is LimitsState.PermitAddLimit -> {
                            if (isOnePanelModel()) {
                                launchAddOperationsActivity()
                            } else {
                                launchAddLimitFragment()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        limitsAdapter = LimitsAdapter()
        setRvClickListener()
        setRvSwipeListener()
        binding.rvLimits.adapter = limitsAdapter
    }

    private fun setRvClickListener() {
        limitsAdapter.onLimitClickListener = {
            if (isOnePanelModel()) {
                launchEditOperationsActivity(it.id)
            } else {
                launchEditLimitFragment(it.id)
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
                val item = limitsAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeLimit(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvLimits)
    }

    private fun setOnClickListeners() {
        setOnBackClickListener()
        setOnAddClickListener()
        setOnAddCategoryClickListener()
    }

    private fun setOnBackClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setOnAddClickListener() {
        binding.ivAddNewLimit.setOnClickListener {
            viewModel.checkCategories()
        }
    }

    private fun setOnAddCategoryClickListener() {
        binding.buttonLimitsAddCategory.setOnClickListener {
            if (isOnePanelModel()) {
                launchAddCategoryOperationsActivity()
            } else {
                launchAddCategoryFragment()
            }
        }
    }

    private fun launchAddOperationsActivity() {
        val intent = OperationsActivity.newIntentAdd(
            this,
            OperationsActivity.FRAGMENT_TYPE_LIMIT
        )
        startActivity(intent)
    }

    private fun launchEditOperationsActivity(id: Int) {
        val intent = OperationsActivity.newIntentEdit(
            this,
            OperationsActivity.FRAGMENT_TYPE_LIMIT,
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

    private fun launchAddLimitFragment() {
        val fragment = AddLimitFragment.newInstanceAdd()
        beginFragmentTransaction(fragment)
    }

    private fun launchEditLimitFragment(id: Int) {
        val fragment = AddLimitFragment.newInstanceEdit(id)
        beginFragmentTransaction(fragment)
    }

    private fun launchAddCategoryFragment() {
        val fragment = AddCategoryFragment.newInstanceAdd()
        beginFragmentTransaction(fragment)
    }

    private fun isOnePanelModel(): Boolean {
        return binding.fragmentContainerLimits == null
    }

    private fun beginFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_limits, fragment)
            .addToBackStack(AddLimitFragment.NAME)
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
            val intent = Intent(context, LimitsActivity::class.java)
            return intent
        }
    }
}