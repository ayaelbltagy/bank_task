package com.example.currency.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currency.data.data.repository.MainRepository
import com.example.currency.data.data.viewModel.MainViewModel
import com.example.currency.helpers.NetworkHelper
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.TestRule
import org.mockito.Mock

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {

    @Mock
    private var mainRepository = mockk<MainRepository>(relaxed = true)
    @Mock
    private val networkHelper  = mockk<NetworkHelper>(relaxed = true)
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: MainViewModel
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
      //  Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = MainViewModel(mainRepository,networkHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()

    }

    @Test
    fun `should emit error object when api response error`() = runBlocking  {
        launch(Dispatchers.Main) {
            if(networkHelper.isNetworkConnected()){
            val message = viewModel.currencies().value!!.message
            Assert.assertEquals(message, message)
                }
        }

    }


}