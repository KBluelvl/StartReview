package he2b.be.startreview.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import he2b.be.startreview.R
import he2b.be.startreview.network.Tournament
import he2b.be.startreview.ui.viewModel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onFavoriteClicked: (Int) -> Unit,
    context: Context
){
    LaunchedEffect(Unit){
        viewModel.getFavoritesTournament(context)
    }
    val tournaments : List<Tournament> = viewModel.tournamentList
    Column {
        Row{
            Text(
                text = stringResource(R.string.tournament_title),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        LazyColumn(contentPadding = PaddingValues()) {
            items(tournaments) { tournament ->
                tournament.images?.get(0)?.let { image ->
                    tournament.id?.let { id ->
                        ImageCardWithCloseButton(
                            url = image.url,
                            onCloseClicked = {
                                viewModel.deleteTournamentInDatabase(id)
                                viewModel.getFavoritesTournament(context)
                            },
                            { onFavoriteClicked(id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCardWithCloseButton(
    url: String,
    onCloseClicked: () -> Unit,
    onFavoriteClicked: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.4f)
                .clickable { onFavoriteClicked() }
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    CloseButton(onCloseClicked)
                }
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(bottom = 8.dp),
                    placeholder = painterResource(R.drawable.placeholder_foreground),
                    error = painterResource(R.drawable.placeholder_foreground)
                )
            }
        }
    }
}

@Composable
fun CloseButton(onCloseClicked: () -> Unit) {
    IconButton(
        onClick = onCloseClicked,
        modifier = Modifier.size(32.dp)
    ) {
        Icon(
            Icons.Rounded.Close,
            contentDescription = "Close",
            tint = Color.Black,
        )
    }
}

