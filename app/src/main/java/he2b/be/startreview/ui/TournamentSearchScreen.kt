    package he2b.be.startreview.ui

    import android.content.Context
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.text.KeyboardActions
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.DateRange
    import androidx.compose.material.icons.filled.LocationOn
    import androidx.compose.material.icons.filled.Search
    import androidx.compose.material3.Card
    import androidx.compose.material3.Icon
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.input.ImeAction
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.unit.dp
    import coil.compose.AsyncImage
    import he2b.be.startreview.R
    import he2b.be.startreview.model.TimeConverter
    import he2b.be.startreview.network.Tournament
    import he2b.be.startreview.ui.viewModel.TournamentSearchViewModel
    import he2b.be.startreview.ui.viewModel.TournamentSearchViewModel.State

    @Composable
    fun TournamentSearchScreen(
        viewModel: TournamentSearchViewModel,
        onCardClicked: (Int) -> Unit,
        context: Context
    ){
        Column {
            OutlinedTextField (
                modifier = Modifier
                    .padding(50.dp,20.dp),
                leadingIcon = { Icon(Icons.Filled.Search,"Search") },
                shape = RoundedCornerShape(12.dp),
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("Search any tournament") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if(viewModel.name.isNotBlank()) {
                            viewModel.searchTournaments(context)
                        }
                    }
                )
            )
            TournamentList(viewModel.tournamentList,onCardClicked, viewModel.state)
        }
    }

    @Composable
    fun TournamentList(
        tournaments: List<Tournament>?,
        onCardClicked:(Int) -> Unit,
        state: State
    ) {
        if (tournaments != null) {
            if (tournaments.isNotEmpty() && state == State.PROGRESS) {
                LazyColumn(contentPadding = PaddingValues()) {
                    items(tournaments) { tournament ->
                        TournamentItem(tournament, onCardClicked)
                    }
                }
            }else if (state == State.ERROR){
                Text(
                    text = stringResource(id = R.string.no_result),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(60.dp,0.dp)
                )
            }
        }
    }

    @Composable
    fun TournamentItem(tournament: Tournament, onCardClicked: (Int) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(2.dp),
            shape = RoundedCornerShape(10)
        ) {
            Row(
                modifier = Modifier
                    .clickable { tournament.id?.let { onCardClicked(it) } }
                    .fillMaxWidth()
            ) {
                // change the format Long to Date in string Mmm dd yyyy
                val formattedDate = TimeConverter.longToDate(tournament.startAt)
                Column {
                    var url = ""
                    if (tournament.images?.isNotEmpty() == true) {
                        url = tournament.images[0].url
                    }
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 120.dp, height = 120.dp),
                        placeholder = painterResource(R.drawable.placeholder_foreground),
                        error = painterResource(R.drawable.placeholder_foreground)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(start= 8.dp)
                ) {
                    Row {
                        Text(
                            tournament.name,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row {
                        Icon(Icons.Filled.DateRange, contentDescription = null)
                        Text(
                            formattedDate
                        )
                    }
                    Row {
                        Icon(Icons.Filled.LocationOn, contentDescription = null)
                        val isOnline = tournament.isOnline
                        val venueAddress = if (isOnline) {
                            "Online"
                        } else {
                            tournament.venueAddress
                        }
                        if (venueAddress != null) {
                            Text(
                                text = venueAddress
                            )
                        }
                    }
                }
            }
        }
    }