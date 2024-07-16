package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Category

interface CategoriesRepository {
    fun getCategoriesList(): LiveData<List<Category>>
    suspend fun getCategory(id: Int): Category
    suspend fun addCategory(category: Category)
    suspend fun editCategory(category: Category)
    suspend fun removeCategory(category: Category)
}