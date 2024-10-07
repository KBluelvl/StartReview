package he2b.be.startreview.model

import android.icu.text.SimpleDateFormat
import java.sql.Date
import java.sql.Timestamp
import java.util.Locale

object TimeConverter {
    fun longToDate(date: Long?): String {
        if(date == null){
            return ""
        }
        val timestamp = Timestamp(date * 1000L)
        val format = SimpleDateFormat("MMM d' 'yyyy", Locale.ENGLISH)
        return format.format(Date(timestamp.time))
    }
}