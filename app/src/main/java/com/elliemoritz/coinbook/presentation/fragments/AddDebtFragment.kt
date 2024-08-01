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
import com.elliemoritz.coinbook.databinding.FragmentAddDebtBinding
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentDebtState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddDebtViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddDebtFragment : Fragment() {

    private lateinit var onEditingListener: OnEditingListener

    private val component by lazy {
        (requireActivity().application as CoinBookApp).component
    }

    private var _binding: FragmentAddDebtBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddDebtViewModel::class.java]
    }

    private var mode: String = MODE_UNKNOWN
    private var debtId: Int = UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        if (context is OnEditingListener) {
            onEditingListener = context
        } else {
            throw RuntimeException(
                "AddDebtFragment: Activity must implement OnEditingListener"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(MODE, MODE_UNKNOWN)
            debtId = it.getInt(ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDebtBinding.inflate(inflater, container, false)
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
                        is FragmentDebtState.Data -> {
                            binding.etAddDebtAmount.setText(it.amount)
                            binding.etAddDebtCreditor.setText(it.creditor)
                        }

                        is FragmentDebtState.EmptyFields -> {
                            onEditingListener.onEmptyFields()
                        }

                        is FragmentDebtState.NoChanges -> {
                            onEditingListener.onNoChanges()
                        }

                        is FragmentDebtState.IncorrectNumber -> {
                            onEditingListener.onIncorrectNumber()
                        }

                        is FragmentDebtState.Finish -> {
                            onEditingListener.onFinished()
                        }
                    }
                }
            }
        }
    }

    private fun setMode() {
        when (mode) {
            MODE_ADD -> {
                binding.buttonAddDebt.setOnClickListener {
                    val amount = binding.etAddDebtAmount.text.toString()
                    val creditor = binding.etAddDebtCreditor.text.toString()
                    viewModel.createDebt(amount, creditor)
                }
            }

            MODE_EDIT -> {
                viewModel.setData(debtId)
                binding.buttonAddDebt.setOnClickListener {
                    val amount = binding.etAddDebtAmount.text.toString()
                    val creditor = binding.etAddDebtCreditor.text.toString()
                    viewModel.editDebt(amount, creditor, debtId)
                }
            }

            else -> throw RuntimeException(
                "AddDebtFragment: Unknown mode for AddDebtFragment"
            )
        }
    }

    companion object {

        private const val MODE = "mode"
        private const val ID = "id"

        private const val MODE_ADD = "add"
        private const val MODE_EDIT = "edit"
        private const val MODE_UNKNOWN = ""

        @JvmStatic
        fun newInstanceAdd() =
            AddDebtFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_ADD)
                }
            }

        @JvmStatic
        fun newInstanceEdit(id: Int) =
            AddDebtFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID, id)
                    putString(MODE, MODE_EDIT)
                }
            }
    }
}