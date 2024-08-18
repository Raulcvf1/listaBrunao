package com.example.listabrunao

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage

class UploadActivity : AppCompatActivity() {

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        val selectImageButton = findViewById<Button>(R.id.selectImageButton)
        val uploadImageButton = findViewById<Button>(R.id.uploadImageButton)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        uploadImageButton.setOnClickListener {
            if (::imageUri.isInitialized) {
                val storageReference = FirebaseStorage.getInstance().reference
                    .child("images/${System.currentTimeMillis()}.jpg")
                storageReference.putFile(imageUri)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Upload bem-sucedido", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Falha no upload", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Selecione uma imagem primeiro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data!!
        }
    }
}
