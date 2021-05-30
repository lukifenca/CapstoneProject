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
    val nama : String,

    @ColumnInfo(name = "umur")
    val umur : String,

    @ColumnInfo(name = "email")
    val email : String,

    @ColumnInfo(name = "tinggi")
    val tinggi : Int,

    @ColumnInfo(name = "berat")
    val berat : Int,

    @ColumnInfo(name = "sistolik")
    val sistolik : Int,

    @ColumnInfo(name = "diastolik")
    val diastolik : Int,

    @ColumnInfo(name = "LDL")
    val LDL : Int,

    @ColumnInfo(name = "HDL")
    val HDL : Int,

    @ColumnInfo(name = "trigliserida")
    val trigliserida : Int,

    @ColumnInfo(name = "gula")
    val Gula : Int, ):Parcelable