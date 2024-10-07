package he2b.be.startreview.ui.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import he2b.be.startreview.R
import he2b.be.startreview.network.StartConnexion
import he2b.be.startreview.network.StartService
import he2b.be.startreview.network.Tournament
import retrofit2.HttpException

class TournamentSearchViewModel : ViewModel() {
    enum class State {
        STARTING, PROGRESS, ERROR
    }

    var name by mutableStateOf("")
    var tournamentList : List<Tournament> by mutableStateOf(listOf())
    var state : State by mutableStateOf(State.STARTING)

    fun searchTournaments(context: Context) {
        val variables = mapOf(
            "name" to name
        )
        val request = StartConnexion(context.getString(R.string.TournamentsByName),variables)
        viewModelScope.launch {
            try {
                tournamentList = StartService.startClient.getData(request).data?.tournaments!!.nodes
                state = if(tournamentList.isNotEmpty()){
                    State.PROGRESS
                } else{
                    State.ERROR
                }
            } catch (e: HttpException) {
                if (e.code() == 429) {
                    // Afficher un message d'erreur pour trop de requêtes
                    Toast.makeText(context, "Too many requests. Please try again later.", Toast.LENGTH_SHORT).show()
                }
                Log.e("error", "Error while connexion: "+e.message, e)
            } catch (e: Exception) {
                state = State.ERROR
                Log.e("error", "Error while connexion: "+e.message, e)
            }
        }
    }
}