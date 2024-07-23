package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    fun getCategoriesList(): Flow<List<Category>>
    fun getCategory(id: Int): Flow<Category>
    suspend fun addCategory(category: Category)
    suspend fun editCategory(category: Category)
    suspend fun removeCategory(category: Category)

    suspend fun refreshCategoriesData()
}