package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.wreckingballsoftware.roadruler.data.repos.DriveRepo
import com.wreckingballsoftware.roadruler.data.services.ActionTransition
import com.wreckingballsoftware.roadruler.domain.models.UIDrive
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenEvent
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenNavigation
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MainScreenViewModelTest {
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var handleMock: SavedStateHandle
    private lateinit var actionTransitionMock: ActionTransition
    private lateinit var driveRepoMock: DriveRepo
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        val valuesMap = mapOf(
            "savedUIState" to mutableSetOf(MainScreenState())
        )
        handleMock = SavedStateHandle(valuesMap)
        actionTransitionMock = mockk()
        every { actionTransitionMock.startTracking(any(), any()) } returns Unit
        every { actionTransitionMock.transition } returns MutableStateFlow("")
        driveRepoMock = mockk()
        every { driveRepoMock.setDriveStartedCallback(any()) } returns Unit
        every { driveRepoMock.setDriveFinishedCallback(any()) } returns Unit
        every { driveRepoMock.getCurrentDistance() } returns MutableStateFlow("0.0")
        every { driveRepoMock.getDrives() } returns MutableStateFlow(listOf())

        viewModel = MainScreenViewModel(
            handleMock,
            actionTransitionMock,
            driveRepoMock,
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `eventHandler should update state correctly`() = runTest {
        val drives = listOf(
            UIDrive(
                driveId = 1,
                driveName = "My really, really short drive to get breakfast",
                driveDateTime = "2021-10-01 12:00",
                driveDistance = "15.3km"
            ),
            UIDrive(
                driveId = 2,
                driveName = "Drive 2",
                driveDateTime = "2021-10-02 12:00",
                driveDistance = "20.3km"
            ),
        )
        val transition = "Transition"
        val driveId = 123L
        val distance = "100"
        val finalDistance = "Final: $distance"
        val eventPopulateDrives = MainScreenEvent.PopulateDrives(drives)
        val eventNewTransition = MainScreenEvent.NewTransition(transition)
        val eventNewDriveStarted = MainScreenEvent.NewDriveStarted(driveId)
        val eventNewDriveDistance = MainScreenEvent.NewDriveDistance(distance)
        val eventFinalDriveDistance = MainScreenEvent.FinalDriveDistance(distance)
        val eventDriveSelected = MainScreenEvent.DriveSelected(driveId)

        viewModel.eventHandler(eventPopulateDrives)
        assert(viewModel.state.drives == drives)

        viewModel.eventHandler(eventNewTransition)
        assert(viewModel.state.transition == transition)

        viewModel.eventHandler(eventNewDriveStarted)
        assert(viewModel.state.driveId == driveId)

        viewModel.eventHandler(eventNewDriveDistance)
        assert(viewModel.state.currentDistance == distance)

        viewModel.eventHandler(eventFinalDriveDistance)
        assert(viewModel.state.currentDistance == finalDistance)

        var navValue: MainScreenNavigation?
        viewModel.navigation.test {
            viewModel.eventHandler(eventDriveSelected)
            navValue = awaitItem()
            assert(navValue == MainScreenNavigation.DisplayDrive(driveId))
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}