package he2b.be.startreview.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import he2b.be.startreview.R
import he2b.be.startreview.model.BracketTypeConverter
import he2b.be.startreview.ui.viewModel.EventViewModel

@Composable
fun EventScreen(
    viewModel: EventViewModel,
    context: Context
){
    LaunchedEffect(Unit){
        viewModel.getEventData(context)
    }
    if (viewModel.event != null) {
        val modifier = Modifier
            .padding(2.dp)
        Column {
            Row {
                Text(
                    text = stringResource(id = R.string.summary_title)
                )
            }
            Row {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row {
                        Column {
                            AsyncImage(
                                model = viewModel.urlImage,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(width = 100.dp, height = 120.dp)
                            )
                        }
                        Column {
                            Row {
                                Text(text = viewModel.startAt)
                            }
                            Row {
                                Text(text = viewModel.name)
                            }
                            Row {
                                Text(
                                    text = "${viewModel.type} - " + viewModel.videogameName,
                                    color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
            Row {
                Text(stringResource(R.string.brackets_title))
            }
            Row {
                Column {
                    if (viewModel.phases != null) {
                        for (i in 0 until viewModel.phases!!.size) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Row{
                                    Text(
                                        text = viewModel.phases!![i].name,
                                        modifier = modifier
                                    )
                                    viewModel.state?.let {state ->
                                        Text(
                                            text = state,
                                            modifier = modifier
                                        )
                                    }
                                }
                                Row{
                                    Column {
                                        Text(
                                            text = "${viewModel.phases!![i].groupCount}",
                                            modifier
                                        )
                                        Text(
                                            text = stringResource(id = R.string.pools_title),
                                            modifier,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = BracketTypeConverter.getBracketAcronym(viewModel.phases!![i].bracketType),
                                            modifier
                                        )
                                        Text(
                                            text = stringResource(id = R.string.type_title),
                                            modifier,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "${viewModel.phases!![i].numSeeds}",
                                            modifier
                                        )
                                        Text(
                                            text = stringResource(id = R.string.entrants_title),
                                            modifier,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    if (i < viewModel.phases!!.size-1){
                                        Column {
                                            Text(
                                                text = stringResource(
                                                    id = R.string.progression_value,
                                                    viewModel.phases!![i+1].name
                                                ),
                                                modifier
                                            )
                                            Text(
                                                text = stringResource(id = R.string.progression_title),
                                                modifier,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}