package com.example.currency.viewModel

import androidx.lifecycle.Observer
import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.LatestRateResponse
import com.example.currency.data.data.repository.MainRepository
import com.example.currency.data.data.viewModel.MainViewModel
import com.example.currency.helpers.NetworkHelper
import com.example.currency.helpers.Resource
import com.example.currency.helpers.Status
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class ViewModelTest {
    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private var mainRepository = mockk<MainRepository>(relaxed = true)

    @Mock
    private val networkHelper = mockk<NetworkHelper>(relaxed = true)

     private lateinit var viewModel :MainViewModel



    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.resetMain()

    }


    @Test
    fun `should emit success when network is connected`() {
        if (networkHelper.isNetworkConnected()) {
            coVerify {
               var response = viewModel.currencies().value!!.data!!
                assertEquals("true", response.success.toString())
            }
        }
    }

    @Test
    fun `should emit null  in currencies when network is not connected`() {
        viewModel = MainViewModel(mainRepository, networkHelper)

        if (networkHelper.isNetworkConnected()) {
                var response = viewModel.currencies().value!!.data!!

                assertEquals("false", Mockito.verify(response).success.toString())

        }
    }

    @Test
    fun `should emit null in rates when network is not connected`() {
         if (networkHelper.isNetworkConnected()) {
            var response = viewModel.latestRates("USD").value!!.status!!
            Assert.assertEquals(Status.SUCCESS, response)
        }
    }

    @Test
    fun `should emit value in rates when network is connected`() {
        if (networkHelper.isNetworkConnected()) {
            var response = viewModel.latestRates("USD").value!!.data!!
            Assert.assertEquals("2023-07-30", response.date)
        }
    }



}


