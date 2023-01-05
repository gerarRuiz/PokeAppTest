package com.ruizdev.pokeapptest.domain.use_cases.datastore_use_cases

import com.ruizdev.pokeapptest.data.repository.Repository

class SaveSessionUUIDUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(sessionUUID: String) {
        repository.saveSessionUUID(sessionUUID)
    }

}