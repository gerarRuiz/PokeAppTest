package com.ruizdev.pokeapptest.domain.use_cases.datastore_use_cases

import com.ruizdev.pokeapptest.data.repository.Repository
import kotlinx.coroutines.flow.Flow

class ReadTextColorUseCase(
    private val repository: Repository
) {

    operator fun invoke(): Flow<Int> {
        return repository.readTextColor()
    }

}