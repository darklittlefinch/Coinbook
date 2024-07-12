package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.ExpenseCategory

interface CategoriesRepository {
    fun getCategoriesList(): LiveData<List<ExpenseCategory>>
    fun getCategory(id: Int): ExpenseCategory
    fun addCategory(operation: ExpenseCategory)
    fun editCategory(operation: ExpenseCategory)
    fun removeCategory(operation: ExpenseCategory)
}