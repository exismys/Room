package com.example.room

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var database: ContactDatabase
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(applicationContext, ContactDatabase::class.java, "ContactDB").build()


        val idEditText = findViewById<EditText>(R.id.et_id)
        val nameEditText = findViewById<EditText>(R.id.et_name)
        val phoneEditText = findViewById<EditText>(R.id.et_phone)
        val add = findViewById<Button>(R.id.btn_add)
        val display = findViewById<Button>(R.id.btn_display)
        val listView = findViewById<ListView>(R.id.lv_contact)
        val list = ArrayList<Contact>()
        adapter = MyAdapter(this, R.layout.list_item, list)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val view = parent[position]
            val id = view.findViewById<TextView>(R.id.tv_id)
            val name = view.findViewById<TextView>(R.id.tv_name)
            val phone = view.findViewById<TextView>(R.id.tv_phone)

            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("Edit")

            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.VERTICAL

            val idEditText = EditText(this)
            idEditText.setText(id.text.toString())
            linearLayout.addView(idEditText)

            val nameEditText = EditText(this)
            nameEditText.setText(name.text.toString())
            linearLayout.addView(nameEditText)

            val phoneEditText = EditText(this)
            phoneEditText.setText(phone.text.toString())
            linearLayout.addView(phoneEditText)

            dialog.setView(linearLayout)

            dialog.setPositiveButton("Edit", DialogInterface.OnClickListener { dialog, which ->
                val updatedName = nameEditText.text.toString()
                val updatedPhone = phoneEditText.text.toString().toLong()
                GlobalScope.launch {
                    database.contactDAO().update(Contact(id.text.toString().toLong(), updatedName, updatedPhone))
                }
                Toast.makeText(this, "updated to $updatedName and $updatedPhone", Toast.LENGTH_SHORT).show()
            })

            dialog.setNegativeButton("Cancel", DialogInterface.OnClickListener {dialog, which ->
                dialog.cancel()
            })

            dialog.show()
        }

        add.setOnClickListener {
            val id = idEditText.text.toString().toLong()
            val name = nameEditText.text.toString()
            val phone = phoneEditText.text.toString().toLong()

            GlobalScope.launch {
                database.contactDAO().insert(Contact(id, name, phone))
            }
            Toast.makeText(this, "Added $id, $name and $phone", Toast.LENGTH_SHORT).show()
        }

        display.setOnClickListener {
            database.contactDAO().getContact().observe(this) {
                adapter.clear()
                adapter.addAll(it)
            }
        }
    }
}