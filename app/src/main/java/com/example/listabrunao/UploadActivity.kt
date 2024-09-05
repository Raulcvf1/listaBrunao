package com.example.listabrunao

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage

// Classe de dados para armazenar o nome e a URL
data class ImageItem(val name: String, val url: String)

class UploadActivity : AppCompatActivity() {

    private lateinit var imageUri: Uri
    private val imageItems = mutableListOf<ImageItem>() // Lista de nomes e URLs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        val selectImageButton = findViewById<Button>(R.id.selectImageButton)
        val uploadImageButton = findViewById<Button>(R.id.uploadImageButton)
        val listView = findViewById<ListView>(R.id.imagesListView)

        // Botão para selecionar imagem da galeria
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        // Botão para fazer upload da imagem selecionada
        uploadImageButton.setOnClickListener {
            if (::imageUri.isInitialized) {
                val storageReference = FirebaseStorage.getInstance().reference
                    .child("images/${System.currentTimeMillis()}.jpg")
                storageReference.putFile(imageUri)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Upload bem-sucedido", Toast.LENGTH_SHORT).show()
                        // Atualiza a lista após o upload
                        fetchImages(listView)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Falha no upload", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Selecione uma imagem primeiro", Toast.LENGTH_SHORT).show()
            }
        }

        // Carrega as imagens existentes no Firebase Storage e exibe no ListView
        fetchImages(listView)

        // Ação ao clicar em um item do ListView
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedImage = imageItems[position]
            val intent = Intent(this, ImageViewerActivity::class.java)
            intent.putExtra("imageUrl", selectedImage.url)
            startActivity(intent)
        }
    }

    // Método para buscar e exibir as imagens do Firebase Storage
    private fun fetchImages(listView: ListView) {
        val storageReference = FirebaseStorage.getInstance().reference.child("images/")
        storageReference.listAll().addOnSuccessListener { listResult ->
            imageItems.clear() // Limpa a lista antes de adicionar novos itens
            listResult.items.forEach { item ->
                val fileName = item.name
                item.downloadUrl.addOnSuccessListener { uri ->
                    imageItems.add(ImageItem(fileName, uri.toString()))
                    // Atualiza o ListView com os nomes dos arquivos
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, imageItems.map { it.name })
                    listView.adapter = adapter
                }
            }
        }
    }

    // Método chamado ao retornar da galeria de imagens
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data!!
        }
    }
}
