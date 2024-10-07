    package he2b.be.startreview.ui

    import android.content.Context
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.LocationOn
    import androidx.compose.material.icons.filled.Person
    import androidx.compose.material.icons.filled.Star
    import androidx.compose.material.icons.outlined.Star
    import androidx.compose.material3.Card
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import coil.compose.AsyncImage
    import he2b.be.startreview.model.EventTypeConverter
    import he2b.be.startreview.model.TimeConverter
    import he2b.be.startreview.network.Event
    import he2b.be.startreview.ui.viewModel.TournamentViewModel

    @Composable
    fun TournamentScreen(
        viewModel: TournamentViewModel,
        onAttendeesClicked : (String) -> Unit,
        onClickedEvent : (Int) -> Unit,
        context: Context
    ) {
        LaunchedEffect(Unit) {
            viewModel.getTournamentData(context)
        }

        if (viewModel.tournament != null) {
            val tournament = viewModel.tournament!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ){
                Row {
                    AsyncImage(
                        model = viewModel.urlBanner,
                        contentDescription = null,
                    )
                }
                Row {
                    AsyncImage(
                        modifier = Modifier
                            .size(width = 80.dp, height = 80.dp),
                        model = viewModel.urlProfile,
                        contentDescription = null,
                    )
                    Column {
                        Row{
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                            ) {
                                Text(
                                    text = viewModel.name,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(
                                onClick = {
                                    viewModel.checkedState = !viewModel.checkedState
                                    if (viewModel.checkedState) {
                                        viewModel.addTournamentInDatabase()
                                    } else {
                                        viewModel.deleteTournamentInDatabase()
                                    }
                                }
                            ) {
                                if (viewModel.checkedState) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Checked",
                                        tint = Color.Red
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.Star,
                                        contentDescription = "Unchecked",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                        Row {
                            Icon(Icons.Filled.LocationOn, contentDescription = null)
                            val isOnline = tournament.isOnline
                            val venueAddress = if (isOnline) {
                                "Online"
                            } else {
                                viewModel.venueAddress
                            }
                            Text(text = venueAddress)
                        }

                    }
                }
                Card(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .clickable { onAttendeesClicked(tournament.slug) }
                ) {
                    Row{
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 8.dp)
                        )
                        Text(
                            text = "Attendees ${viewModel.numAttendees}"
                        )
                    }
                }
                Text(
                    text = "Event"
                )
                val events : List<Event> = tournament.events ?: listOf()
                if (events.isEmpty()) {
                    Text(
                        text = "No events available",
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Column {
                        events.forEach { event ->
                            EventCard(event = event, onClickedEvent = onClickedEvent)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun EventCard(event: Event, onClickedEvent: (Int) -> Unit) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { onClickedEvent(event.id) }
        ) {
            val date = TimeConverter.longToDate(event.startAt)
            val type = EventTypeConverter.getEvent(event.type)
            Text(
                text = date
            )
            Text(
                text = event.name
            )
            Text(
                text = "$type - " + event.videogame.name
            )
        }
    }
