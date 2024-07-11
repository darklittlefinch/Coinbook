package com.elliemoritz.coinbook.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elliemoritz.coinbook.R

class OperationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_operations)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        private const val EXTRA_MODE = "mode"
        const val MODE_BALANCE = "balance"
        const val MODE_INCOME = "balance"
        const val MODE_EXPENSE = "balance"
        const val MODE_MONEY_BOX = "balance"
        const val MODE_MONEY_BOX_OPERATION = "balance"
        const val MODE_DEBT = "balance"
        const val MODE_LIMIT = "balance"
        const val MODE_ALARM = "balance"
        const val MODE_CATEGORY = "balance"

        fun newIntent(context: Context, mode: String): Intent {
            val intent = Intent(context, OperationsActivity::class.java)
            intent.putExtra(EXTRA_MODE, mode)
            return intent
        }
    }
}