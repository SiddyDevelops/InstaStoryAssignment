package com.siddydevelops.instastoryassignment.reels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.siddydevelops.instastoryassignment.database.ReelsDatabase
import com.siddydevelops.instastoryassignment.database.entities.ReelsItem
import com.siddydevelops.instastoryassignment.database.repositories.ReelsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReelsViewModel(application: Application): AndroidViewModel(application) {
    val allReels: LiveData<List<ReelsItem>>
    val repository: ReelsRepository

    init {
        val dao = ReelsDatabase.invoke(application)
        repository = ReelsRepository(dao)
        allReels = repository.getAllReels()
    }

    fun delete(item: ReelsItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(item)
    }

    fun update(item: ReelsItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(item)
    }

    fun insert(item: ReelsItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(item)
    }
}