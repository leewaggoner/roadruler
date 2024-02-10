package com.wreckingballsoftware.roadruler.di

import com.wreckingballsoftware.roadruler.data.repos.ActivityTransitionRepo
import com.wreckingballsoftware.roadruler.ui.mainscreen.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainScreenViewModel(
            handle = get(),
            context = get(),
            activityTransitionRepo = get(),
        )
    }

    single {
        ActivityTransitionRepo(
            context = get()
        )
    }
}