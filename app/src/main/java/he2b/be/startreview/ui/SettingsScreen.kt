package he2b.be.startreview.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import he2b.be.startreview.R

@Composable
fun SettingsScreen(
    darkModeState: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
){
    ToggleButton(darkModeState,onDarkModeChange)
}

@Composable
fun ToggleButton(darkModeState: Boolean, onDarkModeChange: (Boolean) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.display_title),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = stringResource(R.string.darkTheme_title),
            fontSize = 17.sp
        )
        Switch(
            checked = darkModeState,
            onCheckedChange = {
                onDarkModeChange(it)
            },
        )
    }

}