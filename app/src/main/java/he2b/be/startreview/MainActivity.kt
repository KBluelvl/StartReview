    package he2b.be.startreview

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Modifier
    import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
    import androidx.lifecycle.ViewModelProvider
    import he2b.be.startreview.datastore.DataStoreManager
    import he2b.be.startreview.datastore.DataStoreViewModel
    import he2b.be.startreview.model.Repository
    import he2b.be.startreview.ui.theme.StartReviewTheme


    class MainActivity : ComponentActivity() {
        private lateinit var dataStoreViewModel: DataStoreViewModel
        private lateinit var dataStoreManager: DataStoreManager
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            installSplashScreen()
            dataStoreViewModel = ViewModelProvider(this)[DataStoreViewModel::class.java]
            dataStoreManager = DataStoreManager(context = this)

            dataStoreViewModel.getTheme.observe(this@MainActivity) { isDarkMode ->
                Repository.initDatabase(applicationContext)
                setContent {
                    var darkMode by remember { mutableStateOf(isDarkMode) }
                    StartReviewTheme(darkTheme = darkMode) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            MainApp(darkModeState = darkMode,
                                onDarkModeChange = {
                                    darkMode = it
                                    dataStoreViewModel.setTheme(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
