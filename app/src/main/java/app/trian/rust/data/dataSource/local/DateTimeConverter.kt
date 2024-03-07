package app.trian.rust.data.dataSource.local

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DateTimeConverter{
    private val formatter:DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromDateTime(date:LocalDateTime):Long{
        return date.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun toDateTime(date:Long):LocalDateTime{
        return LocalDateTime.ofEpochSecond(date,0, ZoneOffset.UTC)
    }
}