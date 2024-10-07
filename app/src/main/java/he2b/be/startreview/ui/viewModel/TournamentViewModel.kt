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

class TournamentViewModel : ViewModel() {
    var id by mutableStateOf("")
    var tournament : Tournament? by mutableStateOf(null)
    var urlBanner : String by mutableStateOf("")
    var urlProfile : String by mutableStateOf("")
    var checkedState : Boolean by mutableStateOf(false)
    var name : String by mutableStateOf("")
    var venueAddress : String by mutableStateOf("")
    var numAttendees : Int by mutableIntStateOf(0)

    enum class ImageType{
        banner, profile
    }

    fun getTournamentData(context: Context){
        val variables = mapOf(
            "id" to id
        )
        val connexion = StartConnexion(context.getString(R.string.TournamentById), variables)
        viewModelScope.launch {
            try {
                tournament =
                    StartService.startClient.getData(connexion).data?.tournaments?.nodes?.get(0)
                if (tournament?.images?.isNotEmpty() == true) {
                    val nbImages: Int? = tournament?.images?.size
                    if (nbImages != null) {
                        for (i in 0 until nbImages) {
                            val type = tournament?.images?.get(i)?.type
                            if (type == ImageType.banner.name) {
                                urlBanner = tournament?.images?.get(i)?.url ?: ""
                            } else if (type == ImageType.profile.name) {
                                urlProfile = tournament?.images?.get(i)?.url ?: ""
                            }
                        }
                    }
                }
                checkedState = Repository.getTournamentInDatabase(id.toInt())
                name = tournament?.name ?: ""
                venueAddress = tournament?.venueAddress ?: ""
                numAttendees = tournament?.numAttendees ?: 0
                Log.i("debug", "to $tournament")
            } catch (e: HttpException) {
                if (e.code() == 429) {
                    // Afficher un message d'erreur pour trop de requêtes
                    Toast.makeText(
                        context,
                        "Too many requests. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e("error", "Error while connexion: " + e.message, e)
            } catch (e: Exception) {
                Log.e("error", "Error while connexion: " + e.message, e)
            }
        }
    }

    fun addTournamentInDatabase(){
        viewModelScope.launch {
            Repository.insertTournamentInDatabase(id.toInt())
        }
    }

    fun deleteTournamentInDatabase(){
        viewModelScope.launch {
            Repository.deleteTournamentInDatabase(id.toInt())
        }
    }
}