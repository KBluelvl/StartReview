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
import he2b.be.startreview.network.StartConnexion
import he2b.be.startreview.network.StartService
import he2b.be.startreview.network.participant
import kotlinx.coroutines.launch
import retrofit2.HttpException


class AttendeesViewModel : ViewModel() {
    var participants : List<participant>? by mutableStateOf(null)
    var slug by mutableStateOf("")
    var totalPages by mutableIntStateOf(0)
    var page by mutableIntStateOf(1)

    fun getAttendees(context: Context) {
        page = 1
        val variables = mapOf(
            "slug" to slug,
            "page" to page
        )
        val connexion = StartConnexion(context.getString(R.string.GetAttendees), variables)
        viewModelScope.launch {
            try {
                val data = StartService.startClient.getData(connexion).data
                if (data != null) {
                    participants = data.tournament?.participants?.nodes
                    totalPages = data.tournament?.participants?.pageInfo?.totalPages ?: 0
                }
                Log.i("debug", "at $participants")
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

    fun getMoreAttendees(context: Context) {
        page += 1
        if(page <= totalPages) {
            val variables = mapOf(
                "slug" to slug,
                "page" to page
            )
            val connexion = StartConnexion(context.getString(R.string.GetAttendees), variables)
            viewModelScope.launch {
                try {
                    val data = StartService.startClient.getData(connexion).data
                    if (data != null) {
                        data.tournament?.participants?.nodes?.let { newParticipants ->
                            addParticipants(newParticipants)
                        }
                        totalPages = data.tournament?.participants?.pageInfo?.totalPages ?: 0
                    }
                    Log.i("debug", "at $participants")
                } catch (e: HttpException) {
                    if (e.code() == 429) {
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
    }

    private fun addParticipants(newParticipants: List<participant>) {
        val currentList = participants.orEmpty().toMutableList()
        currentList.addAll(newParticipants)
        participants = currentList
    }
}