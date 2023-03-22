package com.example.mvvm.Room

import android.content.Context
import com.example.day1.Room.DAO
import com.example.day1.Room.ProductsDao

class ConcreteLocalSource(context:Context):LocalSource {
    private val dao: DAO by lazy {
        val db:ProductsDao= ProductsDao.getInstance(context)
        db.getProductsDao()
    }


    override suspend fun insertWeathers(weathers: SearchCity) {
        dao?.insert(weathers)
    }

    override suspend fun deleteWeathers(weathers: SearchCity) {
        dao.delete(weathers)
    }

    override suspend fun getStoreWeathers(): List<SearchCity> {
        return dao?.getAll()!!
    }
}