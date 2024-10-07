package he2b.be.startreview.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import he2b.be.startreview.R
import he2b.be.startreview.network.participant
import he2b.be.startreview.ui.viewModel.AttendeesViewModel

@Composable
fun Attendees(
    viewModel: AttendeesViewModel,
    context: Context
) {
    LaunchedEffect(Unit){
        viewModel.getAttendees(context)
    }
    val participants: List<participant>? = viewModel.participants
    if (participants != null) {
        LazyColumn {
            items(participants) { participant ->
                AttendeeCard(participant)
            }
            item{
                LaunchedEffect(true) {
                    viewModel.getMoreAttendees(context)
                }
            }
        }
    }
}

@Composable
fun AttendeeCard(participant: participant){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(2.dp),
        shape = RoundedCornerShape(5)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Text(text = "Attendee", color = MaterialTheme.colorScheme.primary)
            }
            Column {
                Text(text = "Events", color = MaterialTheme.colorScheme.primary)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            ) {
                Row {
                    Column(
                        modifier = Modifier
                            .padding(6.dp)
                    ) {
                        var url = ""
                        if (participant.user != null && participant.user.images!!.isNotEmpty()) {
                            url = participant.user.images[0].url
                        }
                        AsyncImage(
                            model = url,
                            modifier = Modifier
                                .size(width = 60.dp, height = 60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            placeholder = painterResource(R.drawable.player_foreground),
                            error = painterResource(R.drawable.player_foreground)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(6.dp)
                    ) {
                        Text(text = participant.gamerTag, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
            Column{
                Text(
                    text = participant.events?.joinToString(separator = ", ") { it.name } ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(6.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}