package com.example.sqlitedemo

import adapter.StudentAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import db.Student
import db.StudentDAO

class MainActivity : AppCompatActivity() {
    private lateinit var studentDAO: StudentDAO
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentDAO = StudentDAO(this)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etUsia = findViewById<EditText>(R.id.etUsia)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(
            studentDAO.getAllStudents().toMutableList(),
            onEdit = {student -> showEditDialog(student)  },
            onDelete = {student -> confirmDelete(student)  }
        )

        recyclerView.adapter = adapter

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val usia = etUsia.text.toString().toIntOrNull() ?: 0

            if (nama.isNotEmpty()) {
                studentDAO.insertStudent(nama,usia)
                etNama.text.clear()
                etUsia.text.clear()
                adapter.updateData(studentDAO.getAllStudents())
                Toast.makeText(this,"Data tersimpan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Nama tidak boleh kosong",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditDialog(student: Student) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        val etNama = EditText(this).apply {
            setText(student.name)
            hint= "Nama"
        }
        val etUsia = EditText(this).apply {
            setText(student.age.toString())
            hint= "Usia"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        layout.addView(etNama)
        layout.addView(etUsia)

        AlertDialog.Builder(this)
            .setTitle("Edit Mahasiswa")
            .setView(layout)
            .setPositiveButton("Simpan") { _, _ ->
                val namaBaru = etNama.text.toString().trim()
                val usiaBaru = etUsia.text.toString().toIntOrNull() ?: 0
                if (namaBaru.isNotEmpty()) {
                    studentDAO.updateStudent(student.id, namaBaru, usiaBaru)
                    adapter.updateData(studentDAO.getAllStudents())
                    Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
        private fun confirmDelete(student: Student) {
            AlertDialog.Builder(this)
                .setTitle("Hapus Mahasiswa")
                .setMessage("Yakin ingin menghapus ${student.name}?")
                .setPositiveButton("Ya") { _, _ ->
                    studentDAO.deleteStudent(student.id)
                    adapter.updateData(studentDAO.getAllStudents())
                    Toast.makeText(this, "Data di hapus", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Batal", null)
                .show()
        }
}