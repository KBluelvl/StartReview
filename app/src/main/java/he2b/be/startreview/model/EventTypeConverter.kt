package he2b.be.startreview.model

object EventTypeConverter {
    enum class EventType{
        Singles, Doubles, Teams
    }

    fun getEvent(type: Int?): String {
        return when (type) {
            1 -> EventType.Singles.name
            2 -> EventType.Doubles.name
            5 -> EventType.Teams.name
            else -> ""
        }
    }
}