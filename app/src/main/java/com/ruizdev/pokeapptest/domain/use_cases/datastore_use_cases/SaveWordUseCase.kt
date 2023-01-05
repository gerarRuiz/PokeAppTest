package com.ruizdev.pokeapptest.domain.use_cases.datastore_use_cases

import com.ruizdev.pokeapptest.data.repository.Repository

class SaveWordUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(word: String) {
        repository.saveWord(word)
    }

}