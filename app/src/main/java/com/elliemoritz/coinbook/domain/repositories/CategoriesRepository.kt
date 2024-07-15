package com.elliemoritz.coinbook.domain.repositories

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Category

interface CategoriesRepository {
    fun getCategoriesList(): LiveData<List<Category>>
    fun getCategory(id: Int): Category
    fun addCategory(category: Category)
    fun editCategory(category: Category)
    fun removeCategory(category: Category)
}