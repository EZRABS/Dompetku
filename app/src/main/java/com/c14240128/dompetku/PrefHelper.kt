package com.c14240128.dompetku

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefHelper(context: Context) {
    private val prefs = context.getSharedPreferences("keuangan_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    fun saveKategoriList(list: ArrayList<Kategori>) {
        val json = gson.toJson(list)
        prefs.edit().putString("key_kategori", json).apply()
    }

    fun getKategoriList(): ArrayList<Kategori> {
        val json = prefs.getString("key_kategori", null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<Kategori>>() {}.type
            gson.fromJson(json, type)
        } else {
            // Data kategori default saat pertama kali buka aplikasi
            val defaultList = arrayListOf(
                Kategori("KAT-1", "Gaji", "INCOME"),
                Kategori("KAT-2", "Bonus", "INCOME"),
                Kategori("KAT-3", "Makan & Minum", "EXPENSE"),
                Kategori("KAT-4", "Transportasi", "EXPENSE")
            )
            saveKategoriList(defaultList)
            defaultList
        }
    }

    fun saveTransaksiList(list: ArrayList<Transaksi>) {
        val json = gson.toJson(list)
        prefs.edit().putString("key_transaksi", json).apply()
    }

    fun getTransaksiList(): ArrayList<Transaksi> {
        val json = prefs.getString("key_transaksi", null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<Transaksi>>() {}.type
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }
}