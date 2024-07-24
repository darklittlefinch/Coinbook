package com.elliemoritz.coinbook.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elliemoritz.coinbook.databinding.FragmentAddIncomeBinding
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentIncomeState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddIncomeViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddIncomeFragment : Fragment() {

    private lateinit var onEditingListener: OnEditingListener

    private val component by lazy {
        (requireActivity().application as CoinBookApp).component
    }

    private var _binding: FragmentAddIncomeBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddIncomeViewModel::class.java]
    }

    private var mode = MODE_UNKNOWN
    private var incomeId = UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        if (context is OnEditingListener) {
            onEditingListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(MODE, MODE_UNKNOWN)
            incomeId = it.getInt(INCOME_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()

        setMode()
    }

    private fun setMode() {
        when (mode) {

            MODE_ADD -> {
                binding.buttonAddIncome.setOnClickListener {
                    val amount = binding.etAddIncomeAmount.text.toString()
                    val source = binding.etAddIncomeSource.text.toString()
                    viewModel.addIncome(amount, source)
                }
            }

            MODE_EDIT -> {
                viewModel.setData(id)
                binding.buttonAddIncome.setOnClickListener {
                    val amount = binding.etAddIncomeAmount.text.toString()
                    val source = binding.etAddIncomeSource.text.toString()
                    viewModel.editIncome(id, amount, source)
                }
            }

            else -> {
                throw RuntimeException("Unknown mode for AddIncomeFragment")
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is FragmentIncomeState.Data -> {
                            binding.etAddIncomeAmount.hint = it.amount
                            binding.etAddIncomeSource.hint = it.source
                        }

                        is FragmentIncomeState.Error -> {
                            onEditingListener.onError()
                        }

                        is FragmentIncomeState.Finish -> {
                            onEditingListener.onEditingFinished()
                        }
                    }
                }
            }
        }
    }

    companion object {

        private const val MODE = "mode"
        private const val INCOME_ID = "id"

        private const val MODE_ADD = "add"
        private const val MODE_EDIT = "edit"
        private const val MODE_UNKNOWN = ""

        @JvmStatic
        fun newInstanceAdd() =
            AddIncomeFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_ADD)
                }
            }

        @JvmStatic
        fun newInstanceEdit(incomeId: Int) =
            AddIncomeFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_EDIT)
                    putInt(INCOME_ID, incomeId)
                }
            }
    }
}