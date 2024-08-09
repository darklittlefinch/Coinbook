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
import com.elliemoritz.coinbook.databinding.FragmentAddMoneyBoxBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentMoneyBoxState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddMoneyBoxViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddMoneyBoxFragment : Fragment() {

    private lateinit var onEditingListener: OnEditingListener

    private val component by lazy {
        (requireActivity().application as CoinBookApp).component
    }

    private var _binding: FragmentAddMoneyBoxBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddMoneyBoxViewModel::class.java]
    }

    private var mode: String = MODE_UNKNOWN

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        if (context is OnEditingListener) {
            onEditingListener = context
        } else {
            throw RuntimeException(
                "AddMoneyBoxFragment: Activity must implement OnEditingListener"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(MODE, MODE_UNKNOWN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMoneyBoxBinding.inflate(inflater, container, false)
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
                        is FragmentMoneyBoxState.Data -> {
                            binding.etAddMoneyBoxAmount.setText(it.goalAmount)
                            binding.etAddMoneyBoxGoal.setText(it.goal)
                        }

                        is FragmentMoneyBoxState.EmptyFields -> {
                            onEditingListener.onEmptyFields()
                        }

                        is FragmentMoneyBoxState.NoChanges -> {
                            onEditingListener.onNoChanges()
                        }

                        is FragmentMoneyBoxState.IncorrectNumber -> {
                            onEditingListener.onIncorrectNumber()
                        }

                        is FragmentMoneyBoxState.Finish -> {
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
                binding.buttonAddMoneyBox.setOnClickListener {
                    val goalAmount = binding.etAddMoneyBoxAmount.text.toString()
                    val goal = binding.etAddMoneyBoxGoal.text.toString()
                    viewModel.createMoneyBox(goalAmount, goal)
                }
            }

            MODE_EDIT -> {
                viewModel.setData()
                binding.buttonAddMoneyBox.setOnClickListener {
                    val goalAmount = binding.etAddMoneyBoxAmount.text.toString()
                    val goal = binding.etAddMoneyBoxGoal.text.toString()
                    viewModel.editMoneyBox(goalAmount, goal)
                }
            }

            else -> throw RuntimeException(
                "AddMoneyBoxFragment: Unknown mode for AddMoneyBoxFragment"
            )
        }
    }

    companion object {

        const val NAME = "AddMoneyBoxFragment"

        private const val MODE = "mode"
        private const val MODE_ADD = "add"
        private const val MODE_EDIT = "edit"
        private const val MODE_UNKNOWN = ""

        @JvmStatic
        fun newInstanceAdd() =
            AddMoneyBoxFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_ADD)
                }
            }

        @JvmStatic
        fun newInstanceEdit() =
            AddMoneyBoxFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_EDIT)
                }
            }
    }
}