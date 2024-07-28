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
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.FragmentAddMoneyBoxOperationBinding
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentMoneyBoxOperationState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddMoneyBoxOperationViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddMoneyBoxOperationFragment : Fragment() {

    private lateinit var onEditingListener: OnEditingListener

    private val component by lazy {
        (requireActivity().application as CoinBookApp).component
    }

    private var _binding: FragmentAddMoneyBoxOperationBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddMoneyBoxOperationViewModel::class.java]
    }

    private var mode: String = MODE_UNKNOWN
    private var id: Int = UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        if (context is OnEditingListener) {
            onEditingListener = context
        } else {
            throw RuntimeException(
                "AddMoneyBoxOperationFragment: Activity must implement OnEditingListener"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(MODE, MODE_UNKNOWN)
            id = it.getInt(ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMoneyBoxOperationBinding.inflate(
            inflater,
            container,
            false
        )
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
                        is FragmentMoneyBoxOperationState.Data -> {
                            binding.etAddMoneyBoxOperationAmount.hint = it.amount
                        }

                        is FragmentMoneyBoxOperationState.IncorrectNumber -> {
                            onEditingListener.onIncorrectNumber()
                        }

                        is FragmentMoneyBoxOperationState.EmptyFields -> {
                            onEditingListener.onEmptyFields()
                        }

                        is FragmentMoneyBoxOperationState.Finish -> {
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
                binding.tvAddMoneyBoxOperationTitle.text = requireContext().getString(
                    R.string.dialog_put_in_money_box
                )
                binding.buttonAddMoneyBoxOperation.setOnClickListener {
                    val amount = binding.buttonAddMoneyBoxOperation.text.toString()
                    viewModel.createMoneyBoxOperation(amount, Type.EXPENSE)
                }
            }

            MODE_REMOVE -> {
                binding.tvAddMoneyBoxOperationTitle.text = requireContext().getString(
                    R.string.dialog_remove_from_money_box
                )
                binding.buttonAddMoneyBoxOperation.setOnClickListener {
                    val amount = binding.buttonAddMoneyBoxOperation.text.toString()
                    viewModel.createMoneyBoxOperation(amount, Type.INCOME)
                }
            }

            MODE_EDIT -> {
                binding.tvAddMoneyBoxOperationTitle.text = requireContext().getString(
                    R.string.dialog_edit_money_box_operation
                )
                binding.buttonAddMoneyBoxOperation.setOnClickListener {
                    val amount = binding.buttonAddMoneyBoxOperation.text.toString()
                    viewModel.editMoneyBoxOperation(amount)
                }
            }

            else -> throw RuntimeException(
                "AddMoneyBoxOperationFragment: Unknown mode for AddMoneyBoxOperationFragment"
            )
        }
    }

    companion object {

        private const val MODE = "mode"
        private const val ID = "id"

        private const val MODE_ADD = "add"
        private const val MODE_REMOVE = "remove"
        private const val MODE_EDIT = "edit"
        private const val MODE_UNKNOWN = ""

        @JvmStatic
        fun newInstanceAdd() =
            AddMoneyBoxOperationFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_ADD)
                }
            }

        @JvmStatic
        fun newInstanceRemove() =
            AddMoneyBoxOperationFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_REMOVE)
                }
            }

        @JvmStatic
        fun newInstanceEdit(id: Int) =
            AddMoneyBoxOperationFragment().apply {
                arguments = Bundle().apply {
                    putInt(ID, id)
                    putString(MODE, MODE_EDIT)
                }
            }
    }
}