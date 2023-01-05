package com.ruizdev.pokeapptest.domain.use_cases.datastore_use_cases

import com.ruizdev.pokeapptest.data.repository.Repository
import kotlinx.coroutines.flow.Flow

class ReadAvatarUrlUseCase(
    private val repository: Repository
) {

    operator fun invoke(): Flow<String> {
        return repository.readAvatarUrl()
    }

}