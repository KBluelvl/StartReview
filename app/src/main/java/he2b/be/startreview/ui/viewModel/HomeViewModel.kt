package he2b.be.startreview.ui.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import he2b.be.startreview.R
import he2b.be.startreview.model.Repository
import he2b.be.startreview.network.StartConnexion
import he2b.be.startreview.network.StartService
import he2b.be.startreview.network.Tournament
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel : ViewModel(){
    var tournamentList : List<Tournament> by mutableStateOf(listOf())

    fun getFavoritesTournament(context: Context){
        viewModelScope.launch {
            try {
                val items = Repository.getAllTournamentsFromDatabase()
                val ids = items.map { it.tournamentId }
                val variables = mapOf(
                    "ids" to ids
                )
                val request = StartConnexion(context.getString(R.string.TournamentsByIds),variables)
                tournamentList = StartService.startClient.getData(request).data?.tournaments!!.nodes
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

    fun deleteTournamentInDatabase(id: Int){
        viewModelScope.launch {
            Repository.deleteTournamentInDatabase(id)
        }
    }
}