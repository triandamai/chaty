package app.trian.rust.data.dataSource.local

import androidx.room.TypeConverter
import app.trian.rust.data.dataSource.local.entity.AttachmentType
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class AttachmentTypeConverter{

    @TypeConverter
    fun fromType(type:AttachmentType):String{
        return type.name
    }

    @TypeConverter
    fun toType(type:String):AttachmentType{
        return AttachmentType.valueOf(type)
    }
}