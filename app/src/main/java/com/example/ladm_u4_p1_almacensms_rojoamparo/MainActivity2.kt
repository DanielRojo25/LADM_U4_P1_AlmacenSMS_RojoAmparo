package com.example.ladm_u4_p1_almacensms_rojoamparo

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u4_p1_almacensms_rojoamparo.databinding.ActivityMain2Binding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        mostrar()
        binding.regresar.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
    private fun mostrar(){
        FirebaseFirestore.getInstance()
            .collection("smsenviados")
            .addSnapshotListener { value, error ->
                if (error!=null){
                    Toast.makeText(this,"No se pudo realizar la consulta", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                var lista = ArrayList<String>()
                for (documento in value!!){
                    val datos = documento.getString("NUMERO")+"\n"+
                            documento.getString("MENSAJE")+"\n"+
                            documento.getDate("FECHA")+"\n"
                    lista.add(datos)
                }
                binding.listaMensajes.adapter = ArrayAdapter<String>(this,R.layout.simple_list_item_1, lista)
            }
    }
}