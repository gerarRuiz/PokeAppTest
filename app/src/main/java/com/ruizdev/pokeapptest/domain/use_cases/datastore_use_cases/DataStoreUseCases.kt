package com.ruizdev.pokeapptest.domain.use_cases.datastore_use_cases

data class DataStoreUseCases(
    val saveAvatarUrlUseCase: SaveAvatarUrlUseCase,
    val readAvatarUrlUseCase: ReadAvatarUrlUseCase,
    val saveWordUseCase: SaveWordUseCase,
    val readWordUseCase: ReadWordUseCase,
    val saveBackgroundColorUseCase: SaveBackgroundColorUseCase,
    val readBackgroundColorUseCase: ReadBackgroundColorUseCase,
    val saveTextColorUseCase: SaveTextColorUseCase,
    val readTextColorUseCase: ReadTextColorUseCase,
    val saveSessionUUIDUseCase: SaveSessionUUIDUseCase,
    val readSessionUUIDUseCase: ReadSessionUUIDUseCase
)