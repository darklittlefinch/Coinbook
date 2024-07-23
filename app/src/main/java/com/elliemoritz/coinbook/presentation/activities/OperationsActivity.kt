package com.elliemoritz.coinbook.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.presentation.fragments.EditBalanceFragment
import com.elliemoritz.coinbook.presentation.util.OnEditingListener

class OperationsActivity : AppCompatActivity(), OnEditingListener {

    private val mode by lazy {
        intent.getStringExtra(EXTRA_MODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_operations)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container_operations))
        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        launchFragment()
    }

    private fun launchFragment() {
        when (mode) {
            MODE_BALANCE -> launchEditBalanceFragment()
            else -> Log.d(
                "OperationsActivity",
                "Mode not found or yet not implemented"
            )
        }
    }

    private fun launchEditBalanceFragment() {
        val fragment = EditBalanceFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_operations, fragment)
            .commit()
    }

    companion object {
        private const val EXTRA_MODE = "mode"
        const val MODE_BALANCE = "balance"
        const val MODE_INCOME = "income"
        const val MODE_EXPENSE = "expense"
        const val MODE_MONEY_BOX = "money_box"
        const val MODE_ADD_MONEY_BOX = "add_money_box"
        const val MODE_REMOVE_MONEY_BOX = "remove_money_box"
        const val MODE_DEBT = "debt"
        const val MODE_LIMIT = "limit"
        const val MODE_ALARM = "alarm"
        const val MODE_CATEGORY = "category"

        fun newIntent(context: Context, mode: String): Intent {
            val intent = Intent(context, OperationsActivity::class.java)
            intent.putExtra(EXTRA_MODE, mode)
            return intent
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(
            this,
            getString(R.string.toast_success),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    override fun onError() {
        Toast.makeText(
            this,
            getString(R.string.toast_error),
            Toast.LENGTH_SHORT
        ).show()
    }
}