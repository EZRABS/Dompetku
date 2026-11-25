package com.c14240128.dompetku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnMenuKategori).setOnClickListener {
            startActivity(Intent(this, KategoriActivity::class.java))
        }

        findViewById<Button>(R.id.btnMenuIncome).setOnClickListener {
            val intent = Intent(this, TransaksiActivity::class.java)
            intent.putExtra("TIPE_TRANSAKSI", "INCOME")
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnMenuExpense).setOnClickListener {
            val intent = Intent(this, TransaksiActivity::class.java)
            intent.putExtra("TIPE_TRANSAKSI", "EXPENSE")
            startActivity(intent)
        }
    }
}