package com.elliemoritz.coinbook.domain.useCases.alarmsUseCases

import com.elliemoritz.coinbook.domain.repositories.AlarmsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlarmsCountUseCase @Inject constructor(
    private val alarmsRepository: AlarmsRepository
) {
    operator fun invoke(): Flow<Int> {
        return alarmsRepository.getAlarmsCount()
    }
}