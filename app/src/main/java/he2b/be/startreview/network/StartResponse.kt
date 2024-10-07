package he2b.be.startreview.network

data class StartResponse(
    val data: data?
)

data class data(
    val tournaments: tournaments?,
    val tournament: Tournament?,
    val event: Event?
)

data class tournaments(
    val nodes: List<Tournament>
)

data class Tournament(
    val id: Int?,
    val name: String,
    val countryCode: String,
    val slug: String,
    val images: List<Image>?,
    val venueAddress: String?,
    val isOnline: Boolean,
    val numAttendees: Int?,
    val events: List<Event>?,
    val startAt: Long?,
    val participants: participants?
)

data class Image(
    val url: String,
    val type: String
)

data class Event(
    val id: Int,
    val name: String,
    val startAt: Long,
    val videogame: videogame,
    val phases: List<phase>?,
    val type: Int?,
    val state: String?
)

data class participants(
    val pageInfo: pageInfo,
    val nodes: List<participant>
)

data class pageInfo(
    val totalPages: Int
)

data class participant(
    val gamerTag: String,
    val events: List<Event>?,
    val user: user?
)

data class videogame(
    val name: String,
    val images: List<Image>?
)

data class phase(
    val name: String,
    val groupCount: Int?,
    val numSeeds: Int?,
    val bracketType: String
)

data class user(
    val images: List<Image>?
)