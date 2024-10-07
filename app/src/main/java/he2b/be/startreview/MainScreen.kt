package he2b.be.startreview

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import he2b.be.startreview.network.NetworkManager.isWifiConnected
import he2b.be.startreview.ui.Attendees
import he2b.be.startreview.ui.EventScreen
import he2b.be.startreview.ui.TournamentSearchScreen
import he2b.be.startreview.ui.HomeScreen
import he2b.be.startreview.ui.PlayerSearchScreen
import he2b.be.startreview.ui.SettingsScreen
import he2b.be.startreview.ui.TournamentScreen
import he2b.be.startreview.ui.theme.HomeIconColor
import he2b.be.startreview.ui.theme.PlayerIconColor
import he2b.be.startreview.ui.theme.SettingsIconColor
import he2b.be.startreview.ui.theme.TournamentIconColor
import he2b.be.startreview.ui.viewModel.TournamentSearchViewModel
import he2b.be.startreview.ui.viewModel.AttendeesViewModel
import he2b.be.startreview.ui.viewModel.EventViewModel
import he2b.be.startreview.ui.viewModel.HomeViewModel
import he2b.be.startreview.ui.viewModel.TournamentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val tournamentSearchViewModel = TournamentSearchViewModel()
private var tournamentViewModel = TournamentViewModel()
private val attendeesViewModel = AttendeesViewModel()
private val eventViewModel = EventViewModel()
private val homeViewModel = HomeViewModel()
var showToast by  mutableStateOf(false)
var trigger by  mutableStateOf(Unit)

enum class AppScreen {
    Home,
    TournamentSearch,
    Tournament,
    Attendees,
    Event,
    PlayerSearch,
    Player,
    Settings
}

@Composable
fun MainApp(
    darkModeState: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?:AppScreen.Home.name
    )
    val context = LocalContext.current
    CheckWifiAndShowToast(context)

    if(showToast){
        Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show()
        showToast = false
    }
    Scaffold(
        topBar = {
            if((navController.currentBackStackEntryAsState().value?.destination?.route.toString() == AppScreen.Tournament.name ) ||
                (navController.currentBackStackEntryAsState().value?.destination?.route.toString() == AppScreen.Attendees.name) ||
                (navController.currentBackStackEntryAsState().value?.destination?.route.toString() == AppScreen.Event.name)
                ) {
                TopBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        },
        bottomBar = {
            if(navController.currentBackStackEntryAsState().value?.destination?.route.toString() != AppScreen.Tournament.name &&
                (navController.currentBackStackEntryAsState().value?.destination?.route.toString() != AppScreen.Attendees.name) &&
                (navController.currentBackStackEntryAsState().value?.destination?.route.toString() != AppScreen.Event.name)
                ) {
                BottomNavigationBar(navController)
            }
        }
    )
    { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = AppScreen.Home.name) {
                CheckWifiAndShowToast(context)
                HomeScreen(
                    homeViewModel,
                    onFavoriteClicked = { id ->
                        tournamentViewModel.urlProfile = ""
                        tournamentViewModel.urlBanner = ""
                        tournamentViewModel.id = id.toString()
                        navController.navigate(AppScreen.Tournament.name)
                    },
                    context
                )
            }
            composable(route = AppScreen.TournamentSearch.name) {
                CheckWifiAndShowToast(context)
                TournamentSearchScreen(
                    tournamentSearchViewModel,
                    onCardClicked = { id ->
                        tournamentViewModel.urlProfile = ""
                        tournamentViewModel.urlBanner = ""
                        tournamentViewModel.id = id.toString()
                        navController.navigate(AppScreen.Tournament.name)
                    },
                    context
                )
            }
            composable(route = AppScreen.Tournament.name) {
                CheckWifiAndShowToast(context)
                TournamentScreen(
                    tournamentViewModel,
                    onAttendeesClicked = {
                        attendeesViewModel.slug = tournamentViewModel.tournament?.slug.toString()
                        navController.navigate(AppScreen.Attendees.name)
                    },
                    onClickedEvent = { id ->
                        eventViewModel.id = id.toString()
                        navController.navigate(AppScreen.Event.name)
                    },
                    context
                )
            }
            composable(route = AppScreen.Attendees.name) {
                CheckWifiAndShowToast(context)
                Attendees(attendeesViewModel,context)
            }
            composable(route = AppScreen.Event.name) {
                CheckWifiAndShowToast(context)
                EventScreen(
                    eventViewModel,
                    context
                )
            }
            composable(route = AppScreen.PlayerSearch.name) {
                CheckWifiAndShowToast(context)
                PlayerSearchScreen()
            }
            composable(route = AppScreen.Settings.name) {
                CheckWifiAndShowToast(context)
                SettingsScreen(
                    darkModeState,
                    onDarkModeChange
                )
            }
        }
    }
}


@Composable
fun TopBar(
    currentScreen:AppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
){
    if (canNavigateBack) {
        TopAppBar(
            title = { Text(currentScreen.name) },
            modifier = Modifier,
            navigationIcon = {
                IconButton(onClick = { navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController){
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    NavigationBar{
            NavigationBarItem(
                icon = { Icon(Icons.Rounded.Home, contentDescription = AppScreen.Home.name) },
                label = { Text(
                    text = AppScreen.Home.name
                ) },
                onClick =  { selectedItem = 0
                    navController.navigate(AppScreen.Home.name) },
                selected = selectedItem == 0,
                colors = NavigationBarItemDefaults.colors(selectedIconColor = HomeIconColor)
            )
            NavigationBarItem(
                icon = { Icon(painterResource(id = R.drawable.gamepad_solid), AppScreen.TournamentSearch.name) },
                label = { Text(
                    text = "Tournament"
                ) },
                onClick = { selectedItem = 1
                     navController.navigate(AppScreen.TournamentSearch.name)
                },
                selected = selectedItem == 1,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TournamentIconColor
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Rounded.Person, AppScreen.PlayerSearch.name) },
                label = { Text(text = AppScreen.Player.name) },
                onClick = { selectedItem = 2
                    navController.navigate(AppScreen.PlayerSearch.name)
                          },
                selected = selectedItem == 2,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PlayerIconColor
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Rounded.Menu, "Settings") },
                label = { Text(text = "Settings") },
                onClick = { selectedItem = 3
                    navController.navigate(AppScreen.Settings.name)
                          },
                selected = selectedItem == 3,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SettingsIconColor
                )
            )
    }
}

@Composable
fun CheckWifiAndShowToast(context : Context) {
    LaunchedEffect(Unit) {
        val isConnected = withContext(Dispatchers.IO) {
            isWifiConnected(context)
        }
        showToast = !isConnected
    }
    trigger = Unit
}
