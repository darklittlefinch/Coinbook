package com.elliemoritz.coinbook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.CategoryDbModel

@Dao
interface ExpenseCategoriesDao {

    @Query("SELECT * FROM categories")
    fun getCategoriesList(): LiveData<List<CategoryDbModel>>

    @Query("SELECT * FROM categories WHERE id=:categoryId LIMIT 1")
    suspend fun getCategory(categoryId: Int): CategoryDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: CategoryDbModel)

    @Query("DELETE FROM categories WHERE id=:categoryId")
    suspend fun removeCategory(categoryId: Int)
}
