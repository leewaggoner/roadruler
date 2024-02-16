package com.wreckingballsoftware.roadruler.ui.mainscreen.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenState(
    val transition: String = ""
) : Parcelable
