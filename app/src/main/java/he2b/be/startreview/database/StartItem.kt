package he2b.be.startreview.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StartTournaments")
data class StartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tournamentId: Int
)
