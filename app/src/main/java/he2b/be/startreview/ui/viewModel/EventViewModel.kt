package he2b.be.startreview.ui.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import he2b.be.startreview.R
import he2b.be.startreview.model.EventTypeConverter
import he2b.be.startreview.model.TimeConverter.longToDate
import he2b.be.startreview.network.StartConnexion
import kotlinx.coroutines.launch
import he2b.be.startreview.network.Event
import he2b.be.startreview.network.StartService
import he2b.be.startreview.network.phase
import retrofit2.HttpException

class EventViewModel : ViewModel() {
    var id by mutableStateOf("")
    var event : Event? by mutableStateOf(null)
    var urlImage : String? by mutableStateOf("")
    var type by mutableStateOf("")
    var phases : List<phase>? by mutableStateOf(null)
    var state : String? by mutableStateOf("")
    var startAt : String by mutableStateOf("")
    var name : String by mutableStateOf("")
    var videogameName : String by mutableStateOf("")


    fun getEventData(context: Context) {
        val variables = mapOf(
            "eventId" to id
        )
        val connexion = StartConnexion(context.getString(R.string.getEvent), variables)
        viewModelScope.launch {
            try {
                event = StartService.startClient.getData(connexion).data?.event
                urlImage = event?.videogame?.images?.get(0)?.url
                type = event?.type?.let { EventTypeConverter.getEvent(it) } ?: ""
                phases = event?.phases
                state = event?.state
                startAt = longToDate(event?.startAt)
                name = event?.name ?: ""
                videogameName = event?.videogame?.name ?: ""
                Log.i("debug", "$event")
            } catch (e: HttpException) {
                if (e.code() == 429) {
                    // Afficher un message d'erreur pour trop de requêtes
                    Toast.makeText(context, "Too many requests. Please try again later.", Toast.LENGTH_SHORT).show()
                }
                Log.e("error", "Error while connexion: "+e.message, e)
            } catch (e: Exception) {
                Log.e("error", "Error while connexion: "+e.message, e)
            }
        }
    }
}