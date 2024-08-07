package com.elliemoritz.coinbook.data.mappers

import com.elliemoritz.coinbook.data.dbModels.operations.DebtOperationDbModel
import com.elliemoritz.coinbook.data.dbModels.operations.ExpenseDbModel
import com.elliemoritz.coinbook.data.dbModels.operations.IncomeDbModel
import com.elliemoritz.coinbook.data.dbModels.operations.MoneyBoxOperationDbModel
import com.elliemoritz.coinbook.data.util.defineEntityType
import com.elliemoritz.coinbook.domain.entities.helpers.OperationForm
import com.elliemoritz.coinbook.domain.entities.helpers.Type
import com.elliemoritz.coinbook.domain.entities.operations.Operation
import javax.inject.Inject

class OperationsMapper @Inject constructor() {

    fun mapListsOperationsToOneList(
        incomeList: List<IncomeDbModel>,
        expensesList: List<ExpenseDbModel>,
        moneyBoxOperationsList: List<MoneyBoxOperationDbModel>,
        debtsOperationsList: List<DebtOperationDbModel>
    ): List<Operation> {
        return mapListIncomeDbModelToListOperations(incomeList) +
                mapListExpensesDbModelToListOperations(expensesList) +
                mapListMoneyBoxOperationsDbModelToListOperations(moneyBoxOperationsList) +
                mapListDebtOperationDbModelToListOperations(debtsOperationsList)
    }

    private fun mapListIncomeDbModelToListOperations(list: List<IncomeDbModel>) = list
        .map { mapIncomeDbModelToOperation(it) }

    private fun mapListExpensesDbModelToListOperations(list: List<ExpenseDbModel>) = list
        .map { mapExpenseDbModelToOperation(it) }

    private fun mapListMoneyBoxOperationsDbModelToListOperations(
        list: List<MoneyBoxOperationDbModel>
    ) = list
        .map { mapMoneyBoxOperationDbModelToOperation(it) }

    private fun mapListDebtOperationDbModelToListOperations(
        list: List<DebtOperationDbModel>
    ) = list
        .map { mapDebtOperationDbModelToOperation(it) }

    private fun mapIncomeDbModelToOperation(dbModel: IncomeDbModel) = Operation(
        operationForm = OperationForm.INCOME,
        type = Type.INCOME,
        amount = dbModel.amount,
        dateTimeMillis = dbModel.dateTimeMillis,
        operationId = dbModel.id,
        info = dbModel.source
    )

    private fun mapExpenseDbModelToOperation(dbModel: ExpenseDbModel) = Operation(
        operationForm = OperationForm.EXPENSE,
        type = Type.EXPENSE,
        amount = dbModel.amount,
        dateTimeMillis = dbModel.dateTimeMillis,
        operationId = dbModel.id,
        info = dbModel.categoryName
    )

    private fun mapMoneyBoxOperationDbModelToOperation(
        dbModel: MoneyBoxOperationDbModel
    ) = Operation(
        operationForm = OperationForm.MONEY_BOX,
        type = defineEntityType(dbModel.type),
        amount = dbModel.amount,
        dateTimeMillis = dbModel.dateTimeMillis,
        operationId = dbModel.id
    )

    private fun mapDebtOperationDbModelToOperation(dbModel: DebtOperationDbModel) = Operation(
        operationForm = OperationForm.DEBT,
        type = defineEntityType(dbModel.type),
        amount = dbModel.amount,
        dateTimeMillis = dbModel.dateTimeMillis,
        operationId = dbModel.id
    )
}
