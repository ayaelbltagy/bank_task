package com.example.currency.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currency.data.data.repository.MainRepository
import com.example.currency.data.data.viewModel.MainViewModel
import com.example.currency.helpers.NetworkHelper
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {


    private var mainRepository: MainRepository = mockk()
    private lateinit var viewModel: MainViewModel
    private val networkHelper: NetworkHelper = mockk()
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = MainViewModel(mainRepository, networkHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit error object when api response error`() {
        val message = "Error from api"
    }
}