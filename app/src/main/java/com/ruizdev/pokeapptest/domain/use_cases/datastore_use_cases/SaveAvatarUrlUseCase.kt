package com.ruizdev.pokeapptest.domain.use_cases.datastore_use_cases

import com.ruizdev.pokeapptest.data.repository.Repository

class SaveAvatarUrlUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(avatarUrl: String) {
        repository.saveAvatarUrl(avatarUrl)
    }

}