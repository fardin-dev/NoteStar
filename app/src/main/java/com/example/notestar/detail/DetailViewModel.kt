package com.example.notestar.detail

import androidx.lifecycle.ViewModel
import com.example.notestar.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: StorageRepository
) : ViewModel() {

}
