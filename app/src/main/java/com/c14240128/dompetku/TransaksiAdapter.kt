package com.c14240128.dompetku

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class TransaksiAdapter(
    private val list: ArrayList<Transaksi>,
    private val onEdit: (Transaksi) -> Unit,
    private val onDelete: (Transaksi) -> Unit
) : RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKategori: TextView = view.findViewById(R.id.tvKategoriTransaksi)
        val tvNominal: TextView = view.findViewById(R.id.tvNominalTransaksi)
        val tvDetail: TextView = view.findViewById(R.id.tvDetailTransaksi)
        val btnEdit: Button = view.findViewById(R.id.btnEditTransaksi)
        val btnHapus: Button = view.findViewById(R.id.btnHapusTransaksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaksi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val formatRp = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        holder.tvKategori.text = item.kategori
        holder.tvNominal.text = formatRp.format(item.nominal)
        holder.tvNominal.setTextColor(
            if (item.tipe == "INCOME") Color.parseColor("#2E7D32") else Color.parseColor("#C62828")
        )
        holder.tvDetail.text = "${item.tanggal} • ${item.keterangan}"

        holder.btnEdit.setOnClickListener { onEdit(item) }
        holder.btnHapus.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int = list.size
}