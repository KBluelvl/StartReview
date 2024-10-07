package he2b.be.startreview.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StartDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTournament(item: StartItem)

    @Query("SELECT * FROM StartTournaments")
    suspend fun getAllTournaments(): List<StartItem>

    @Query("DELETE FROM StartTournaments WHERE tournamentId = :tournamentId")
    suspend fun delete(tournamentId: Int)

    @Query("SELECT * FROM StartTournaments WHERE tournamentId = :tournamentId")
    suspend fun getById(tournamentId: Int) : StartItem
}