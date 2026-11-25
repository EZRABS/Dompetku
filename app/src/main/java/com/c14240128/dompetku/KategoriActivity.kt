package com.c14240128.dompetku

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class KategoriActivity : AppCompatActivity() {

    private lateinit var prefHelper: PrefHelper
    private lateinit var kategoriList: ArrayList<Kategori>
    private lateinit var adapter: KategoriAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori)

        prefHelper = PrefHelper(this)
        kategoriList = prefHelper.getKategoriList()

        val rvKategori = findViewById<RecyclerView>(R.id.rvKategori)
        val btnTambah = findViewById<Button>(R.id.btnTambahKategori)

        adapter = KategoriAdapter(
            list = kategoriList,
            onEdit = { kategori, pos -> showFormDialog(kategori, pos) },
            onDelete = { pos ->
                kategoriList.removeAt(pos)
                prefHelper.saveKategoriList(kategoriList)
                adapter.notifyItemRemoved(pos)
                adapter.notifyItemRangeChanged(pos, kategoriList.size)
                Toast.makeText(this, "Kategori dihapus", Toast.LENGTH_SHORT).show()
            }
        )

        rvKategori.layoutManager = LinearLayoutManager(this)
        rvKategori.adapter = adapter

        btnTambah.setOnClickListener { showFormDialog(null, -1) }
    }

    private fun showFormDialog(existing: Kategori?, position: Int) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 12)
        }

        val etNama = EditText(this).apply {
            hint = "Nama Kategori (misal: Gaji, Makan)"
            setText(existing?.nama ?: "")
        }

        val spinnerTipe = Spinner(this)
        val tipeOptions = arrayOf("INCOME", "EXPENSE")
        spinnerTipe.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipeOptions)
        if (existing != null) {
            spinnerTipe.setSelection(if (existing.tipe == "INCOME") 0 else 1)
        }

        layout.addView(etNama)
        layout.addView(spinnerTipe)

        AlertDialog.Builder(this)
            .setTitle(if (existing == null) "Tambah Kategori" else "Ubah Kategori")
            .setView(layout)
            .setPositiveButton("Simpan") { _, _ ->
                val nama = etNama.text.toString().trim()
                val tipe = spinnerTipe.selectedItem.toString()

                if (nama.isEmpty()) {
                    Toast.makeText(this, "Nama kategori wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (existing == null) {
                    val baru = Kategori("KAT-${System.currentTimeMillis()}", nama, tipe)
                    kategoriList.add(baru)
                    adapter.notifyItemInserted(kategoriList.size - 1)
                } else {
                    kategoriList[position].nama = nama
                    kategoriList[position].tipe = tipe
                    adapter.notifyItemChanged(position)
                }
                prefHelper.saveKategoriList(kategoriList)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}