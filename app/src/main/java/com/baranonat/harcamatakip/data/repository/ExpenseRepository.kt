package com.baranonat.harcamatakip.data.repository

import com.baranonat.harcamatakip.data.local.Expense
import com.baranonat.harcamatakip.data.local.ExpenseDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExpenseRepository @Inject constructor(

    private val expenseDao: ExpenseDao

){

    fun getAllExpenses(): Flow<List<Expense>> = expenseDao.getAllExpenses()

    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }


}
