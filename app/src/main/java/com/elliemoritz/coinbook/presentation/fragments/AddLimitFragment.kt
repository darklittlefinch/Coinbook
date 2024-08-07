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
import com.elliemoritz.coinbook.databinding.FragmentAddLimitBinding
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentLimitState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddLimitViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddLimitFragment : Fragment() {

    private lateinit var onEditingListener: OnEditingListener

    private val component by lazy {
        (requireActivity().application as CoinBookApp).component
    }

    private var _binding: FragmentAddLimitBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddLimitViewModel::class.java]
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
                "AddLimitFragment: Activity must implement OnEditingListener"
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
        _binding = FragmentAddLimitBinding.inflate(inflater, container, false)
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
                        is FragmentLimitState.Amount -> {
                            binding.etAddLimitAmount.setText(it.amount)
                        }

                        is FragmentLimitState.Categories -> {
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                it.categories
                            )
                            binding.spinnerAddLimit.adapter = adapter
                        }

                        is FragmentLimitState.CategoryPosition -> {
                            binding.spinnerAddLimit.setSelection(it.categoryPosition)
                        }

                        is FragmentLimitState.EmptyFields -> {
                            onEditingListener.onEmptyFields()
                        }

                        is FragmentLimitState.NoChanges -> {
                            onEditingListener.onNoChanges()
                        }


                        is FragmentLimitState.IncorrectNumber -> {
                            onEditingListener.onIncorrectNumber()
                        }

                        is FragmentLimitState.Finish -> {
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
                binding.buttonAddLimit.setOnClickListener {
                    val amount = binding.etAddLimitAmount.text.toString()
                    val categoryName = binding.spinnerAddLimit.selectedItem.toString()
                    viewModel.createLimit(amount, categoryName)
                }
            }

            MODE_EDIT -> {
                viewModel.setData(id)
                binding.buttonAddLimit.setOnClickListener {
                    val amount = binding.etAddLimitAmount.text.toString()
                    val categoryName = binding.spinnerAddLimit.selectedItem.toString()
                    viewModel.editLimit(amount, categoryName, id)
                }
            }

            else -> throw RuntimeException(
                "AddLimitFragment: Unknown mode for AddLimitFragment"
            )
        }
    }

    companion object {

        const val NAME = "AddLimitFragment"

        private const val MODE = "mode"
        private const val ID = "id"

        private const val MODE_ADD = "add"
        private const val MODE_EDIT = "edit"
        private const val MODE_UNKNOWN = ""

        @JvmStatic
        fun newInstanceAdd() =
            AddLimitFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_ADD)
                }
            }

        @JvmStatic
        fun newInstanceEdit(id: Int) =
            AddLimitFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_EDIT)
                    putInt(ID, id)
                }
            }
    }
}