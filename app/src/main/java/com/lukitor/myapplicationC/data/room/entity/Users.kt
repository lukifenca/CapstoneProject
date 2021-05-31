package com.lukitor.myapplicationC.data.room.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "users")
@Parcelize
data class Users (

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "nama")
    var nama : String,

    @ColumnInfo(name = "umur")
    var umur : String,

    @ColumnInfo(name = "email")
    var email : String,

    @ColumnInfo(name = "tinggi")
    var tinggi : Int,

    @ColumnInfo(name = "berat")
    var berat : Int,

    @ColumnInfo(name = "sistolik")
    var sistolik : Int,

    @ColumnInfo(name = "diastolik")
    var diastolik : Int,

    @ColumnInfo(name = "LDL")
    var LDL : Int,

    @ColumnInfo(name = "HDL")
    var HDL : Int,

    @ColumnInfo(name = "trigliserida")
    var trigliserida : Int,

    @ColumnInfo(name = "gula")
    var Gula : Int, ):Parcelable