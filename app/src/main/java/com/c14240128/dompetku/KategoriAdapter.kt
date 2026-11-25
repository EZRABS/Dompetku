package com.c14240128.dompetku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KategoriAdapter(
    private val list: ArrayList<Kategori>,
    private val onEdit: (Kategori, Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaKategori)
        val tvTipe: TextView = view.findViewById(R.id.tvTipeKategori)
        val btnEdit: Button = view.findViewById(R.id.btnEditKategori)
        val btnHapus: Button = view.findViewById(R.id.btnHapusKategori)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kategori, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNama.text = item.nama
        holder.tvTipe.text = if (item.tipe == "INCOME") "Pemasukan (Income)" else "Pengeluaran (Expense)"

        holder.btnEdit.setOnClickListener { onEdit(item, position) }
        holder.btnHapus.setOnClickListener { onDelete(position) }
    }

    override fun getItemCount(): Int = list.size
}