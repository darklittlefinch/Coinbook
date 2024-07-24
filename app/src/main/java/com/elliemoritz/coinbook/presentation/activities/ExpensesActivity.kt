package com.elliemoritz.coinbook.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ActivityExpensesBinding

class ExpensesActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityExpensesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        setOnBackClickListener()
        setOnAddExpenseClickListener()
        setOnAddCategoryClickListener()
    }

    private fun setOnBackClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setOnAddExpenseClickListener() {
        binding.ivAddNewExpense.setOnClickListener {
            val intent = OperationsActivity.newIntent(
                this,
                OperationsActivity.FRAGMENT_TYPE_EXPENSE,
                OperationsActivity.MODE_ADD
            )
            startActivity(intent)
        }
    }

    private fun setOnAddCategoryClickListener() {
        binding.buttonAddCategory?.setOnClickListener {
            val intent = OperationsActivity.newIntent(
                this,
                OperationsActivity.FRAGMENT_TYPE_CATEGORY,
                OperationsActivity.MODE_ADD
            )
            startActivity(intent)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, ExpensesActivity::class.java)
            return intent
        }
    }
}