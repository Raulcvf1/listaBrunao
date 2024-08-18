package com.example.listabrunao

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage

class ImageListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list)

        val listView = findViewById<ListView>(R.id.imagesListView)
        val storageReference = FirebaseStorage.getInstance().reference.child("images/")
        storageReference.listAll().addOnSuccessListener { listResult ->
            val urls = mutableListOf<String>()
            listResult.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    urls.add(uri.toString())
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, urls)
                    listView.adapter = adapter
                }
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, ImageViewerActivity::class.java)
            intent.putExtra("imageUrl", listView.getItemAtPosition(position) as String)
            startActivity(intent)
        }
    }
}
