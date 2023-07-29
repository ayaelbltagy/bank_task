package com.example.currency.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.repository.MainRepository
import com.example.currency.data.data.viewModel.MainViewModel
import com.example.currency.helpers.NetworkHelper
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.rules.TestRule
import org.mockito.Mock

class ViewModelTest {

    @Mock
    private var mainRepository = mockk<MainRepository>(relaxed = true)

    @Mock
    private val networkHelper = mockk<NetworkHelper>(relaxed = true)
    private lateinit var viewModel: MainViewModel


    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = MainViewModel(mainRepository, networkHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

    }

    @Test
    fun `should emit success when network is connected`() {
        if (networkHelper.isNetworkConnected()) {
            lateinit var response: AllCurrenciesResponse
            coVerify {
                response = viewModel.currencies().value!!.data!!
            }
            Assert.assertEquals("true", response.success.toString())

        }
    }


}


