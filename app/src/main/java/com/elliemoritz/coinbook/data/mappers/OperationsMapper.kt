package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.OperationDbModel
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_DEBT
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_EXPENSE
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_INCOME
import com.elliemoritz.coinbook.data.util.OPERATION_FORM_MONEY_BOX_OPERATION
import com.elliemoritz.coinbook.data.util.TYPE_EXPENSE
import com.elliemoritz.coinbook.data.util.TYPE_INCOME
import com.elliemoritz.coinbook.data.util.formatTime
import com.elliemoritz.coinbook.data.util.parseTime
import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.DebtOperation
import com.elliemoritz.coinbook.domain.entities.operations.Expense
import com.elliemoritz.coinbook.domain.entities.operations.Income
import com.elliemoritz.coinbook.domain.entities.operations.MoneyBoxOperation
import com.elliemoritz.coinbook.domain.entities.operations.Operation

class OperationsMapper {

    private fun defineOperationForm(operationForm: OperationForm): String {
        return when (operationForm) {
            OperationForm.INCOME -> OPERATION_FORM_INCOME
            OperationForm.EXPENSE -> OPERATION_FORM_EXPENSE
            OperationForm.MONEY_BOX -> OPERATION_FORM_MONEY_BOX_OPERATION
            OperationForm.DEBT -> OPERATION_FORM_DEBT
        }
    }

    private fun defineDbModelType(type: Type): String {
        return when (type) {
            Type.INCOME -> TYPE_INCOME
            Type.EXPENSE -> TYPE_EXPENSE
        }
    }

    private fun defineEntityType(string: String): Type {
        return when (string) {
            TYPE_INCOME -> Type.INCOME
            TYPE_EXPENSE -> Type.EXPENSE
            else -> {
                throw RuntimeException("Unknown type")
            }
        }
    }

    // INCOME

    private fun mapDbModelToIncome(dbModel: OperationDbModel) = Income(
        incId = dbModel.id,
        incDate = parseTime(dbModel.date),
        incAmount = dbModel.amount,
        incSource = dbModel.info
    )

    fun mapListDbModelToListIncome(list: List<OperationDbModel>) = list.map {
        mapDbModelToIncome(it)
    }

    // EXPENSE

    fun mapDbModelToExpense(dbModel: OperationDbModel) = Expense(
        expId = dbModel.id,
        expDate = parseTime(dbModel.date),
        expAmount = dbModel.amount,
        expCategoryName = dbModel.info
    )

    fun mapListDbModelToListExpenses(list: List<OperationDbModel>) = list.map {
        mapDbModelToExpense(it)
    }

    // MONEY BOX OPERATION

    private fun mapDbModelToMoneyBoxOperation(dbModel: OperationDbModel) = MoneyBoxOperation(
        mbId = dbModel.id,
        mbDate = parseTime(dbModel.date),
        mbAmount = dbModel.amount,
        mbType = defineEntityType(dbModel.type)
    )

    fun mapListDbModelToListMoneyBoxOperations(list: List<OperationDbModel>) = list.map {
        mapDbModelToMoneyBoxOperation(it)
    }

    // DEBT OPERATION

    private fun mapDbModelToDebtOperation(dbModel: OperationDbModel) = DebtOperation(
        debtId = dbModel.id,
        debtDate = parseTime(dbModel.date),
        debtAmount = dbModel.amount,
        debtType = defineEntityType(dbModel.type),
        debtCreditor = dbModel.info
    )

    fun mapListDbModelToListDebtOperations(list: List<OperationDbModel>) = list.map {
        mapDbModelToDebtOperation(it)
    }

    // ALL OPERATIONS

    fun mapOperationToDbModel(income: Operation) = OperationDbModel(
        id = income.id,
        operationForm = defineOperationForm(income.operationForm),
        type = defineDbModelType(income.type),
        date = formatTime(income.date),
        amount = income.amount,
        info = income.info
    )

    fun mapDbModelToOperation(dbModel: OperationDbModel): Operation {
        return when (dbModel.operationForm) {
            OPERATION_FORM_INCOME -> mapDbModelToIncome(dbModel)
            OPERATION_FORM_EXPENSE -> mapDbModelToExpense(dbModel)
            OPERATION_FORM_MONEY_BOX_OPERATION -> mapDbModelToMoneyBoxOperation(dbModel)
            OPERATION_FORM_DEBT -> mapDbModelToDebtOperation(dbModel)
            else -> {
                throw RuntimeException("Unknown operation form")
            }
        }
    }

    fun mapListDbModelToListOperations(list: List<OperationDbModel>) = list.map {
        mapDbModelToOperation(it)
    }
}
