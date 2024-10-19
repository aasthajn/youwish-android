package com.app.domain.usecase

import com.app.core.utils.DataState
import com.app.domain.model.Banner
import com.app.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class GetBannerListUseCase @Inject constructor(val repository: Repository) {

    suspend fun invoke(): Flow<DataState<List<Banner>>> = repository.getBannerCards()

}
