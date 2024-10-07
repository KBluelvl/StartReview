package he2b.be.startreview.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreManager(context: Context) {
    // Create an extension property for Context to initialize DataStore<Preferences>
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
    private val dataStore = context.dataStore

    companion object {
        // Define a key for storing dark mode preference
        val darkModeKey = booleanPreferencesKey("DARK_MODE_KEY")
    }

    /**
     * Set the theme preference.
     * @param isDarkMode Boolean indicating if dark mode should be enabled.
     */
    suspend fun setTheme(isDarkMode: Boolean) {
        dataStore.edit { pref ->
            pref[darkModeKey] = isDarkMode
        }
    }

    /**
     * Get the current theme preference as a Flow.
     * @return Flow<Boolean> emitting the current theme preference.
     */
    fun getTheme(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val uiMode = preferences[darkModeKey] ?: false
                // Return the uiMode value
                uiMode
            }
    }
}