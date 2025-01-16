package com.app.watchtime.di

import com.app.watchtime.data.network.ApiService
import com.app.watchtime.data.network.RetrofitInstance
import com.app.watchtime.data.repository.HomeRepository
import com.app.watchtime.data.repository.HomeRepositoryImpl
import com.app.watchtime.data.repository.TitleDetailsRepository
import com.app.watchtime.data.repository.TitleDetailsRepositoryImpl
import com.app.watchtime.ui.viewmodel.home.HomeViewModel
import com.app.watchtime.ui.viewmodel.title.TitleViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<ApiService> { RetrofitInstance.api }

    single<HomeRepository> { HomeRepositoryImpl(get()) }
    single<TitleDetailsRepository> { TitleDetailsRepositoryImpl(get()) }

    viewModel<HomeViewModel> { HomeViewModel(get()) }
    viewModel<TitleViewModel> { TitleViewModel(get(), get()) }
}