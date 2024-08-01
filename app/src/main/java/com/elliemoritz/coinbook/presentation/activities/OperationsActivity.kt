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
import androidx.fragment.app.Fragment
import com.elliemoritz.coinbook.R
import com.elliemoritz.coinbook.domain.entities.helpers.UNDEFINED_ID
import com.elliemoritz.coinbook.presentation.fragments.AddCategoryFragment
import com.elliemoritz.coinbook.presentation.fragments.AddDebtFragment
import com.elliemoritz.coinbook.presentation.fragments.AddExpenseFragment
import com.elliemoritz.coinbook.presentation.fragments.AddIncomeFragment
import com.elliemoritz.coinbook.presentation.fragments.AddLimitFragment
import com.elliemoritz.coinbook.presentation.fragments.AddMoneyBoxFragment
import com.elliemoritz.coinbook.presentation.fragments.AddMoneyBoxOperationFragment
import com.elliemoritz.coinbook.presentation.fragments.EditBalanceFragment
import com.elliemoritz.coinbook.presentation.util.OnEditingListener

class OperationsActivity : AppCompatActivity(), OnEditingListener {

    private val fragmentType by lazy {
        intent.getStringExtra(EXTRA_FRAGMENT_TYPE)
    }

    private val mode by lazy {
        intent.getStringExtra(EXTRA_MODE)
    }

    private val id by lazy {
        intent.getIntExtra(EXTRA_ID, UNDEFINED_ID)
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
        when (fragmentType) {
            FRAGMENT_TYPE_BALANCE -> launchEditBalanceFragment()
            FRAGMENT_TYPE_INCOME -> launchAddIncomeFragment()
            FRAGMENT_TYPE_EXPENSE -> launchAddExpenseFragment()
            FRAGMENT_TYPE_CATEGORY -> launchAddCategoryFragment()
            FRAGMENT_TYPE_MONEY_BOX -> launchAddMoneyBoxFragment()
            FRAGMENT_TYPE_MONEY_BOX_OPERATION -> launchAddMoneyBoxOperationFragment()
            FRAGMENT_TYPE_DEBT -> launchAddDebtFragment()
            FRAGMENT_TYPE_LIMIT -> launchAddLimitFragment()
            else -> Log.d(
                "OperationsActivity",
                "Mode not found or yet not implemented"
            )
        }
    }

    private fun launchEditBalanceFragment() {
        val fragment = EditBalanceFragment.newInstance()
        beginFragmentTransaction(fragment)
    }

    private fun launchAddIncomeFragment() {
        val fragment = when (mode) {
            MODE_ADD -> AddIncomeFragment.newInstanceAdd()
            MODE_EDIT -> AddIncomeFragment.newInstanceEdit(id)
            else -> throw RuntimeException(
                "OperationsActivity: Unknown mode for AddIncomeFragment"
            )
        }
        beginFragmentTransaction(fragment)
    }

    private fun launchAddExpenseFragment() {
        val fragment = when (mode) {
            MODE_ADD -> AddExpenseFragment.newInstanceAdd()
            MODE_EDIT -> AddExpenseFragment.newInstanceEdit(id)
            else -> throw RuntimeException(
                "OperationsActivity: Unknown mode for AddExpenseFragment"
            )
        }
        beginFragmentTransaction(fragment)
    }

    private fun launchAddMoneyBoxFragment() {
        val fragment = when (mode) {
            MODE_ADD -> AddMoneyBoxFragment.newInstanceAdd()
            MODE_EDIT -> AddMoneyBoxFragment.newInstanceEdit()
            else -> throw RuntimeException(
                "OperationsActivity: Unknown mode for AddMoneyBoxFragment"
            )
        }
        beginFragmentTransaction(fragment)
    }

    private fun launchAddCategoryFragment() {
        val fragment = when (mode) {
            MODE_ADD -> AddCategoryFragment.newInstanceAdd()
            MODE_EDIT -> AddCategoryFragment.newInstanceEdit(id)
            else -> throw RuntimeException(
                "OperationsActivity: Unknown mode for AddCategoryFragment"
            )
        }
        beginFragmentTransaction(fragment)
    }

    private fun launchAddMoneyBoxOperationFragment() {
        val fragment = when (mode) {
            MODE_ADD -> AddMoneyBoxOperationFragment.newInstanceAdd()
            MODE_REMOVE -> AddMoneyBoxOperationFragment.newInstanceRemove()
            MODE_EDIT -> AddMoneyBoxOperationFragment.newInstanceEdit(id)
            else -> throw RuntimeException(
                "OperationsActivity: Unknown mode for AddCategoryFragment"
            )
        }
        beginFragmentTransaction(fragment)
    }

    private fun launchAddDebtFragment() {
        val fragment = when (mode) {
            MODE_ADD -> AddDebtFragment.newInstanceAdd()
            MODE_EDIT -> AddDebtFragment.newInstanceEdit(id)
            else -> throw RuntimeException(
                "OperationsActivity: Unknown mode for AddDebtFragment"
            )
        }
        beginFragmentTransaction(fragment)
    }

    private fun launchAddLimitFragment() {
        val fragment = when (mode) {
            MODE_ADD -> AddLimitFragment.newInstanceAdd()
            MODE_EDIT -> AddLimitFragment.newInstanceEdit(id)
            else -> throw RuntimeException(
                "OperationsActivity: Unknown mode for AddLimitFragment"
            )
        }
        beginFragmentTransaction(fragment)
    }

    private fun beginFragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_operations, fragment)
            .commit()
    }

    companion object {
        private const val EXTRA_FRAGMENT_TYPE = "fragment"
        private const val EXTRA_MODE = "mode"
        private const val EXTRA_ID = "id"

        const val FRAGMENT_TYPE_BALANCE = "balance"
        const val FRAGMENT_TYPE_INCOME = "income"
        const val FRAGMENT_TYPE_EXPENSE = "expense"
        const val FRAGMENT_TYPE_MONEY_BOX = "money_box"
        const val FRAGMENT_TYPE_MONEY_BOX_OPERATION = "add_money_box"
        const val FRAGMENT_TYPE_DEBT = "debt"
        const val FRAGMENT_TYPE_LIMIT = "limit"
        const val FRAGMENT_TYPE_ALARM = "alarm"
        const val FRAGMENT_TYPE_CATEGORY = "category"

        const val MODE_ADD = "add"
        const val MODE_REMOVE = "remove"
        const val MODE_EDIT = "edit"

        fun newIntentAdd(context: Context, fragmentType: String): Intent {
            val intent = Intent(context, OperationsActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_TYPE, fragmentType)
            intent.putExtra(EXTRA_MODE, MODE_ADD)
            return intent
        }

        fun newIntentRemove(context: Context, fragmentType: String): Intent {
            val intent = Intent(context, OperationsActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_TYPE, fragmentType)
            intent.putExtra(EXTRA_MODE, MODE_REMOVE)
            return intent
        }

        fun newIntentEdit(context: Context, fragmentType: String, id: Int): Intent {
            val intent = Intent(context, OperationsActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_TYPE, fragmentType)
            intent.putExtra(EXTRA_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_ID, id)
            return intent
        }

        fun newIntentEditWithoutId(context: Context, fragmentType: String): Intent {
            val intent = Intent(context, OperationsActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_TYPE, fragmentType)
            intent.putExtra(EXTRA_MODE, MODE_EDIT)
            return intent
        }
    }

    override fun onFinished() {
        Toast.makeText(
            this,
            getString(R.string.toast_success),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    override fun onEmptyFields() {
        Toast.makeText(
            this,
            getString(R.string.toast_error_empty_fields),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onIncorrectNumber() {
        Toast.makeText(
            this,
            getString(R.string.toast_error_incorrect_number),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNoChanges() {
        Toast.makeText(
            this,
            getString(R.string.toast_error_no_changes),
            Toast.LENGTH_SHORT
        ).show()
    }
}