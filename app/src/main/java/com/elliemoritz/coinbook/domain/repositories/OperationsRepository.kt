package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.entities.operations.Operation

interface OperationsRepository {
    fun getOperationsList(): LiveData<List<Operation>>
    fun getOperation(id: Int): Operation
    fun addOperation(operation: Operation)
    fun editOperation(operation: Operation)
    fun removeOperation(operation: Operation)

    fun getIncomeList(): LiveData<List<Income>>
    fun getExpensesList(): LiveData<List<Expense>>
    fun getMoneyBoxOperationsList(): LiveData<List<MoneyBoxOperation>>
    fun getDebtOperationsList(): LiveData<List<DebtOperation>>
}
