package com.elliemoritz.coinbook.domain.repositories

import com.elliemoritz.coinbook.domain.entities.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    fun getCategoriesList(): Flow<List<Category>>
    fun getCategory(id: Long): Flow<Category>
    fun getCategoryByName(name: String): Flow<Category?>
    suspend fun addCategory(category: Category): Long
    suspend fun editCategory(category: Category)
    suspend fun removeCategory(category: Category)
}