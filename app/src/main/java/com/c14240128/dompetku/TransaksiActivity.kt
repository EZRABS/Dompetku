package com.c14240128.dompetku

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class TransaksiActivity : AppCompatActivity() {

    private lateinit var prefHelper: PrefHelper
    private lateinit var allTransaksiList: ArrayList<Transaksi>
    private lateinit var filteredList: ArrayList<Transaksi>
    private lateinit var adapter: TransaksiAdapter
    private var tipeHalaman: String = "INCOME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        tipeHalaman = intent.getStringExtra("TIPE_TRANSAKSI") ?: "INCOME"
        prefHelper = PrefHelper(this)

        val tvJudul = findViewById<TextView>(R.id.tvJudulHalaman)
        val btnTambah = findViewById<Button>(R.id.btnTambahTransaksi)
        val rvTransaksi = findViewById<RecyclerView>(R.id.rvTransaksi)

        tvJudul.text = if (tipeHalaman == "INCOME") "Data Pemasukan (Income)" else "Data Pengeluaran (Expense)"
        btnTambah.text = if (tipeHalaman == "INCOME") "+ Catat Pemasukan" else "+ Catat Pengeluaran"

        allTransaksiList = prefHelper.getTransaksiList()
        filteredList = ArrayList(allTransaksiList.filter { it.tipe == tipeHalaman })

        adapter = TransaksiAdapter(
            list = filteredList,
            onEdit = { transaksi -> showFormDialog(transaksi) },
            onDelete = { transaksi ->
                allTransaksiList.removeAll { it.id == transaksi.id }
                prefHelper.saveTransaksiList(allTransaksiList)
                refreshList()
                Toast.makeText(this, "Data dihapus", Toast.LENGTH_SHORT).show()
            }
        )

        rvTransaksi.layoutManager = LinearLayoutManager(this)
        rvTransaksi.adapter = adapter

        btnTambah.setOnClickListener { showFormDialog(null) }
    }

    private fun refreshList() {
        filteredList.clear()
        filteredList.addAll(allTransaksiList.filter { it.tipe == tipeHalaman })
        adapter.notifyDataSetChanged()
    }

    private fun showFormDialog(existing: Transaksi?) {
        val kategoriSesuaiTipe = prefHelper.getKategoriList()
            .filter { it.tipe == tipeHalaman }
            .map { it.nama }

        if (kategoriSesuaiTipe.isEmpty()) {
            Toast.makeText(this, "Buat kategori $tipeHalaman terlebih dahulu!", Toast.LENGTH_LONG).show()
            return
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 12)
        }

        val spinnerKategori = Spinner(this)
        spinnerKategori.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, kategoriSesuaiTipe)
        if (existing != null) {
            val idx = kategoriSesuaiTipe.indexOf(existing.kategori)
            if (idx >= 0) spinnerKategori.setSelection(idx)
        }

        val etNominal = EditText(this).apply {
            hint = "Nominal (Rp)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setText(existing?.nominal?.toString() ?: "")
        }

        val etKeterangan = EditText(this).apply {
            hint = "Keterangan / Catatan"
            setText(existing?.keterangan ?: "")
        }

        val cal = Calendar.getInstance()
        val defaultDate = "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}"
        val etTanggal = EditText(this).apply {
            hint = "Tanggal"
            isFocusable = false
            setText(existing?.tanggal ?: defaultDate)
            setOnClickListener {
                DatePickerDialog(this@TransaksiActivity, { _, year, month, day ->
                    setText("$day/${month + 1}/$year")
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        layout.addView(spinnerKategori)
        layout.addView(etNominal)
        layout.addView(etKeterangan)
        layout.addView(etTanggal)

        AlertDialog.Builder(this)
            .setTitle(if (existing == null) "Catat $tipeHalaman" else "Ubah $tipeHalaman")
            .setView(layout)
            .setPositiveButton("Simpan") { _, _ ->
                val nominalStr = etNominal.text.toString().trim()
                val ket = etKeterangan.text.toString().trim()
                val tgl = etTanggal.text.toString().trim()
                val kat = spinnerKategori.selectedItem.toString()

                if (nominalStr.isEmpty()) {
                    Toast.makeText(this, "Nominal tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val nominal = nominalStr.toLong()
                if (existing == null) {
                    allTransaksiList.add(
                        Transaksi("TRX-${System.currentTimeMillis()}", nominal, kat, ket, tgl, tipeHalaman)
                    )
                } else {
                    val target = allTransaksiList.find { it.id == existing.id }
                    target?.apply {
                        this.nominal = nominal
                        this.kategori = kat
                        this.keterangan = ket
                        this.tanggal = tgl
                    }
                }

                prefHelper.saveTransaksiList(allTransaksiList)
                refreshList()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}