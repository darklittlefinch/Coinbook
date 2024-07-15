package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.OperationDbModel
import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.entities.operations.Operation

class OperationsMapper {

    // INCOME
    fun mapIncomeToDbModel(income: Income) = OperationDbModel(
        id = income.id,
        operationForm = income.operationForm,
        type = income.type,
        date = income.date,
        amount = income.amount,
        info = income.info
    )

    fun mapDbModelToIncome(dbModel: OperationDbModel) = Income(
        incId = dbModel.id,
        incDate = dbModel.date,
        incAmount = dbModel.amount,
        incSource = dbModel.info
    )

    fun mapListDbModelToListIncome(list: List<OperationDbModel>) = list.map {
        mapDbModelToIncome(it)
    }

    // EXPENSE
    fun mapExpenseToDbModel(expense: Expense) = OperationDbModel(
        id = expense.id,
        operationForm = expense.operationForm,
        type = expense.type,
        date = expense.date,
        amount = expense.amount,
        info = expense.info
    )

    fun mapDbModelToExpense(dbModel: OperationDbModel) = Expense(
        expId = dbModel.id,
        expDate = dbModel.date,
        expAmount = dbModel.amount,
        expCategoryName = dbModel.info
    )

    fun mapListDbModelToListExpenses(list: List<OperationDbModel>) = list.map {
        mapDbModelToExpense(it)
    }

    // MONEY BOX OPERATION
    fun mapMoneyBoxOperationToDbModel(moneyBoxOperation: MoneyBoxOperation) = OperationDbModel(
        id = moneyBoxOperation.id,
        operationForm = moneyBoxOperation.operationForm,
        type = moneyBoxOperation.type,
        date = moneyBoxOperation.date,
        amount = moneyBoxOperation.amount,
        info = moneyBoxOperation.info
    )

    fun mapDbModelToMoneyBoxOperation(dbModel: OperationDbModel) = MoneyBoxOperation(
        mbId = dbModel.id,
        mbDate = dbModel.date,
        mbAmount = dbModel.amount,
        mbType = dbModel.type
    )

    fun mapListDbModelToListMoneyBoxOperations(list: List<OperationDbModel>) = list.map {
        mapDbModelToMoneyBoxOperation(it)
    }

    // DEBT OPERATION
    fun mapDebtOperationToDbModel(debtOperation: DebtOperation) = OperationDbModel(
        id = debtOperation.id,
        operationForm = debtOperation.operationForm,
        type = debtOperation.type,
        date = debtOperation.date,
        amount = debtOperation.amount,
        info = debtOperation.info
    )

    fun mapDbModelToDebtOperation(dbModel: OperationDbModel) = DebtOperation(
        debtId = dbModel.id,
        debtDate = dbModel.date,
        debtAmount = dbModel.amount,
        debtType = dbModel.type,
        debtCreditor = dbModel.info
    )

    fun mapListDbModelToListDebtOperations(list: List<OperationDbModel>) = list.map {
        mapDbModelToMoneyBoxOperation(it)
    }

    // ALL OPERATIONS
    fun mapOperationToDbModel(income: Operation) = OperationDbModel(
        id = income.id,
        operationForm = income.operationForm,
        type = income.type,
        date = income.date,
        amount = income.amount,
        info = income.info
    )

    fun mapDbModelToOperation(dbModel: OperationDbModel): Operation {
        return when (dbModel.operationForm) {
            OperationForm.INCOME -> mapDbModelToIncome(dbModel)
            OperationForm.EXPENSE -> mapDbModelToExpense(dbModel)
            OperationForm.MONEY_BOX -> mapDbModelToMoneyBoxOperation(dbModel)
            OperationForm.DEBT -> mapDbModelToDebtOperation(dbModel)
        }
    }

    fun mapListDbModelToListOperations(list: List<OperationDbModel>) = list.map {
        mapDbModelToOperation(it)
    }
}
