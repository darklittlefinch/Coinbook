package com.elliemoritz.coinbook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliemoritz.coinbook.data.dbModels.ExpenseCategoryDbModel

@Dao
interface ExpenseCategoriesDao {

    @Query("SELECT * FROM categories")
    fun getCategoriesList(): LiveData<List<ExpenseCategoryDbModel>>

    @Query("SELECT * FROM categories WHERE id=:categoryId LIMIT 1")
    fun getCategory(categoryId: Int): ExpenseCategoryDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: ExpenseCategoryDbModel)

    @Query("DELETE FROM categories WHERE id=:categoryId")
    fun removeCategory(categoryId: Int)
}
