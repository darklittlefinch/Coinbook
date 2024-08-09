package com.elliemoritz.coinbook.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elliemoritz.coinbook.databinding.FragmentAddExpenseBinding
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentExpenseState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.util.OnNotEnoughMoneyListener
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddExpenseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddExpenseFragment : Fragment() {

    private lateinit var onEditingListener: OnEditingListener
    private lateinit var onNotEnoughMoneyListener: OnNotEnoughMoneyListener

    private val component by lazy {
        (requireActivity().application as CoinBookApp).component
    }

    private var _binding: FragmentAddExpenseBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddExpenseViewModel::class.java]
    }

    private var mode: String = MODE_UNKNOWN
    private var id: Long = UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        if (context is OnEditingListener) {
            onEditingListener = context
        } else {
            throw RuntimeException(
                "AddExpenseFragment: Activity must implement OnEditingListener"
            )
        }

        if (context is OnNotEnoughMoneyListener) {
            onNotEnoughMoneyListener = context
        } else {
            throw RuntimeException(
                "AddExpenseFragment: Activity must implement OnNotEnoughMoneyListener"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(MODE, MODE_UNKNOWN)
            id = it.getLong(EXPENSE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setMode()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is FragmentExpenseState.Categories -> {
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                it.categories
                            )
                            binding.spinnerAddExpenses.adapter = adapter
                        }

                        is FragmentExpenseState.Amount -> {
                            binding.etAddExpenseAmount.setText(it.amount)
                        }

                        is FragmentExpenseState.CategoryPosition -> {
                            binding.spinnerAddExpenses.setSelection(it.position)
                        }

                        is FragmentExpenseState.EmptyFields -> {
                            onEditingListener.onEmptyFields()
                        }

                        FragmentExpenseState.NoChanges -> {
                            onEditingListener.onNoChanges()
                        }

                        is FragmentExpenseState.IncorrectNumber -> {
                            onEditingListener.onIncorrectNumber()
                        }

                        is FragmentExpenseState.Finish -> {
                            onEditingListener.onFinished()
                        }

                        is FragmentExpenseState.NotEnoughMoney -> {
                            onNotEnoughMoneyListener.onNotEnoughMoney()
                        }
                    }
                }
            }
        }
    }

    private fun setMode() {
        when (mode) {
            MODE_ADD -> {
                binding.buttonAddExpense.setOnClickListener {
                    val amount = binding.etAddExpenseAmount.text.toString()
                    val categoryName = binding.spinnerAddExpenses.selectedItem.toString()
                    viewModel.createExpense(amount, categoryName)
                }
            }

            MODE_EDIT -> {
                viewModel.setData(id)
                binding.buttonAddExpense.setOnClickListener {
                    val amount = binding.etAddExpenseAmount.text.toString()
                    val categoryName = binding.spinnerAddExpenses.selectedItem.toString()
                    viewModel.editExpense(amount, categoryName, id)
                }
            }

            else -> throw RuntimeException(
                "AddExpenseFragment: Unknown mode for AddExpenseFragment"
            )
        }
    }

    companion object {

        const val NAME = "AddExpenseFragment"

        private const val MODE = "mode"
        private const val EXPENSE_ID = "id"

        private const val MODE_ADD = "add"
        private const val MODE_EDIT = "edit"
        private const val MODE_UNKNOWN = ""

        @JvmStatic
        fun newInstanceAdd() =
            AddExpenseFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_ADD)
                }
            }

        @JvmStatic
        fun newInstanceEdit(id: Long) =
            AddExpenseFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXPENSE_ID, id)
                    putString(MODE, MODE_EDIT)
                }
            }
    }
}