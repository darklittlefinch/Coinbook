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
import com.elliemoritz.coinbook.databinding.FragmentEditBalanceBinding
import com.elliemoritz.coinbook.presentation.CoinBookApp
import com.elliemoritz.coinbook.presentation.states.fragmentsStates.FragmentBalanceState
import com.elliemoritz.coinbook.presentation.util.OnEditingListener
import com.elliemoritz.coinbook.presentation.viewModels.ViewModelFactory
import com.elliemoritz.coinbook.presentation.viewModels.fragmentsViewModels.EditBalanceViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditBalanceFragment : Fragment() {

    private lateinit var onEditingListener: OnEditingListener

    private val component by lazy {
        (requireActivity().application as CoinBookApp).component
    }

    private var _binding: FragmentEditBalanceBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[EditBalanceViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)

        if (context is OnEditingListener) {
            onEditingListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()

        binding.buttonEditBalance.setOnClickListener {
            val newBalance = binding.etEditBalanceAmount.text.toString()
            viewModel.editBalance(newBalance)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is FragmentBalanceState.Data -> {
                            binding.etEditBalanceAmount.hint = it.amount
                        }
                        is FragmentBalanceState.Error -> {
                            onEditingListener.onError()
                        }
                        is FragmentBalanceState.Finish -> {
                            onEditingListener.onEditingFinished()
                        }
                    }
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = EditBalanceFragment()
    }
}