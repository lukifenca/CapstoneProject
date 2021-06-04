package com.lukitor.myapplicationC.data.room.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "nutrients")
@Parcelize
data class Nutrients (

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id : Int,

    @ColumnInfo(name = "tanggal")
    var tanggal : String,

    @ColumnInfo(name = "kalori")
    var kalori : Int,

    @ColumnInfo(name = "garam")
    var garam : Int,

    @ColumnInfo(name = "gula")
    var gula : Int,

    @ColumnInfo(name = "lemak")
    var lemak : Int,):Parcelable
