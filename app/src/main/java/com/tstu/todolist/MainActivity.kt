package com.tstu.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private var TAG = "MainActivity"
    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate:Button
    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: StudentAdapter? = null
    private var std: StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)
        btnAdd.setOnClickListener { addStudent() }
        btnView.setOnClickListener { getStudents() }
        btnUpdate.setOnClickListener { updateStudents() }
        adapter?.setOnClickItem {
            Toast.makeText(this,it.name,Toast.LENGTH_SHORT).show()
            edName.setText(it.name)
            edEmail.setText(it.email)

            std = it
        }

        adapter?.setOnClickDeleteItem {
            deleteStudents(it.id)
        }
    }

    private fun deleteStudents(id:Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){dialog,_ ->
            sqliteHelper.deleteStudentById(id)
            getStudents()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){dialog,_ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
    private fun getStudents() {
        val stdList = sqliteHelper.getAllStudents()
        Log.d(TAG, "getStudents: ${stdList.size}")
        adapter?.addItems(stdList)
    }

    private fun addStudent() {
        val name = edName.text.toString()
        val email = edEmail.text.toString()

        if (email.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please enter required place", Toast.LENGTH_SHORT).show()
        } else {
            val std = StudentModel(name = name, email = email)
            val status = sqliteHelper.insertStudent(std)
            if (status > -1) {
                Toast.makeText(this, "Student successfully added", Toast.LENGTH_SHORT).show()
                clearEditText()
            } else {
                Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show()
                clearEditText()
            }
        }
    }
    private fun updateStudents(){
        val email = edEmail.text.toString()
        val name = edName.text.toString()

        // check record not changed
        if (email == std?.name && name == std?.name){
            Toast.makeText(this, "Record not changed", Toast.LENGTH_SHORT).show()
            return
        }else{
            if (std ==null)return

            val std = StudentModel(id = std!!.id, name = name,email = email)
            val status = sqliteHelper.updateStudents(std)
            if (status>-1){
                clearEditText()
                getStudents()
            }else{
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearEditText() {
        edName.setText("")
        edEmail.setText("")
        edName.requestFocus()
    }
    private fun initRecyclerView(){
         recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }
    private fun initView() {
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}