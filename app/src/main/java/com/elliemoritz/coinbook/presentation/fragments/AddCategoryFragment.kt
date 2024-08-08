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
import com.elliemoritz.coinbook.databinding.FragmentAddCategoryBinding
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentCategoryState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.AddCategoryViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddCategoryFragment : Fragment() {

    private lateinit var onEditingListener: OnEditingListener

    private val component by lazy {
        (requireActivity().application as CoinBookApp).component
    }

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddCategoryViewModel::class.java]
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
                "AddCategoryFragment: Activity must implement OnEditingListener"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getString(MODE, MODE_UNKNOWN)
            id = it.getLong(CATEGORY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
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
                        is FragmentCategoryState.Name -> {
                            binding.etAddCategoryName.setText(it.name)
                        }

                        is FragmentCategoryState.Limit -> {
                            binding.etAddCategoryLimit.setText(it.limit)
                        }

                        is FragmentCategoryState.EmptyFields -> {
                            onEditingListener.onEmptyFields()
                        }

                        is FragmentCategoryState.NoChanges -> {
                            onEditingListener.onNoChanges()
                        }

                        is FragmentCategoryState.IncorrectNumber -> {
                            onEditingListener.onIncorrectNumber()
                        }

                        is FragmentCategoryState.Finish -> {
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
                binding.buttonAddCategory.setOnClickListener {
                    val name = binding.etAddCategoryName.text.toString()
                    val limit = binding.etAddCategoryLimit.text.toString()
                    viewModel.createCategory(name, limit)
                }
            }

            MODE_EDIT -> {
                viewModel.setData(id)
                binding.buttonAddCategory.setOnClickListener {
                    val name = binding.etAddCategoryName.text.toString()
                    val limit = binding.etAddCategoryLimit.text.toString()
                    viewModel.editCategory(name, limit, id)
                }
            }

            else -> throw RuntimeException(
                "AddCategoryFragment: Unknown mode for AddCategoryFragment"
            )
        }
    }

    companion object {

        private const val MODE = "mode"
        private const val CATEGORY_ID = "id"

        private const val MODE_ADD = "add"
        private const val MODE_EDIT = "edit"
        private const val MODE_UNKNOWN = ""

        @JvmStatic
        fun newInstanceAdd() =
            AddCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_ADD)
                }
            }

        @JvmStatic
        fun newInstanceEdit(id: Long) =
            AddCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(MODE, MODE_EDIT)
                    putLong(CATEGORY_ID, id)
                }
            }
    }
}