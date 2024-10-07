package he2b.be.startreview.datastore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataStoreViewModel(applicaton: Application) : AndroidViewModel(applicaton) {

    private val dataStore = DataStoreManager(applicaton)

    val getTheme = dataStore.getTheme().asLiveData(Dispatchers.IO)

    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            dataStore.setTheme(isDarkMode)
        }
    }
}