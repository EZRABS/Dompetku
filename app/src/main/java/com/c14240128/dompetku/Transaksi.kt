package com.c14240128.dompetku

data class Transaksi(
    val id: String,
    var nominal: Long,
    var kategori: String,
    var keterangan: String,
    var tanggal: String,
    var tipe: String
)
