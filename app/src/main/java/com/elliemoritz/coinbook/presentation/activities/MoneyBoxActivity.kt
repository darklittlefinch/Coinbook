package com.elliemoritz.coinbook.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.databinding.ActivityMoneyBoxBinding

class MoneyBoxActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMoneyBoxBinding.inflate(layoutInflater)
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
        setOnAddClickListener()
        setOnRemoveClickListener()
        setOnAddMoneyBoxClickListener()
    }

    private fun setOnBackClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun setOnAddClickListener() {
        binding.ivAddToMoneyBox.setOnClickListener {
            val intent = OperationsActivity.newIntent(
                this,
                OperationsActivity.FRAGMENT_TYPE_MONEY_BOX_OPERATION,
                OperationsActivity.MODE_ADD
            )
            startActivity(intent)
        }
    }

    private fun setOnRemoveClickListener() {
        binding.ivRemoveFromMoneyBox.setOnClickListener {
            val intent = OperationsActivity.newIntent(
                this,
                OperationsActivity.FRAGMENT_TYPE_MONEY_BOX_OPERATION,
                OperationsActivity.MODE_REMOVE
            )
            startActivity(intent)
        }
    }

    private fun setOnEditMoneyBoxClickListener() {
        binding.ivMoneyBox.setOnClickListener {
            val intent = OperationsActivity.newIntent(
                this,
                OperationsActivity.FRAGMENT_TYPE_MONEY_BOX,
                OperationsActivity.MODE_EDIT
            )
            startActivity(intent)
        }
    }

    private fun setOnAddMoneyBoxClickListener() {
        binding.ivMoneyBox.setOnClickListener {
            val intent = OperationsActivity.newIntent(
                this,
                OperationsActivity.FRAGMENT_TYPE_MONEY_BOX,
                OperationsActivity.MODE_ADD
            )
            startActivity(intent)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, MoneyBoxActivity::class.java)
            return intent
        }
    }
}