package he2b.be.startreview.model

import android.content.Context
import he2b.be.startreview.database.StartDatabase
import he2b.be.startreview.database.StartItem

object Repository {
    private var database : StartDatabase? = null

    fun initDatabase(context: Context){
        if (database == null) {
            database = StartDatabase.getInstance(context)
        }
    }

    suspend fun insertTournamentInDatabase(tournamentId : Int){
        database?.let { theDatabase ->
            val newTournament = StartItem(0,tournamentId)
            theDatabase.theDAO().insertTournament(newTournament)
        }
    }

    suspend fun getAllTournamentsFromDatabase() : List<StartItem> {
        database?.let { theDatabase ->
            return theDatabase.theDAO().getAllTournaments()
        }
        return listOf()
    }

    suspend fun deleteTournamentInDatabase(tournamentId : Int){
        database?.theDAO()?.delete(tournamentId)
    }

    suspend fun getTournamentInDatabase(tournamentId : Int) : Boolean{
        val item = database?.theDAO()?.getById(tournamentId)
        return item != null
    }
}