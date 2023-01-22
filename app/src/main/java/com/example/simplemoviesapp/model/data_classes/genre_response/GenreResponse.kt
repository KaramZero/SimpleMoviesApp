package com.example.simplemoviesapp.model.data_classes.genre_response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "genreResponse")
data class GenreResponse(
                @ColumnInfo(name = "genres")     @SerializedName("genres" ) var genres : ArrayList<Genre> = arrayListOf(),
    @PrimaryKey @ColumnInfo(name = "id"    )     @SerializedName("id"     ) var id     :  String = "1"

    )
