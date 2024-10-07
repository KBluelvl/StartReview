package he2b.be.startreview.model

object BracketTypeConverter {
    enum class BracketType{
        SINGLE_ELIMINATION,
        DOUBLE_ELIMINATION,
        ROUND_ROBIN,
        SWISS,
        EXHIBITION,
        CUSTOM_SCHEDULE,
        MATCHMAKING,
        ELIMINATION_ROUNDS,
        RACE,
        CIRCUIT;
        val acronym: String
            get() {
                return name.split('_').joinToString("") { it.first().toString() }
            }
    }

    fun getBracketAcronym(bracketType: String?): String {
        if(bracketType == null){
            return ""
        }
        return try {
            BracketType.valueOf(bracketType).acronym
        } catch (e: IllegalArgumentException) {
            ""
        }
    }

}