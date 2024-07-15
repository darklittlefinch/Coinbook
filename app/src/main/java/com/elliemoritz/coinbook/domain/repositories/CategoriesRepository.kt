package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.ExpenseCategory

interface CategoriesRepository {
    fun getCategoriesList(): LiveData<List<ExpenseCategory>>
    fun getCategory(id: Int): ExpenseCategory
    fun addCategory(category: ExpenseCategory)
    fun editCategory(category: ExpenseCategory)
    fun removeCategory(category: ExpenseCategory)
}