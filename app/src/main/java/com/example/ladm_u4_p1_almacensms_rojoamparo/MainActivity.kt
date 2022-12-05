package com.example.ladm_u4_p1_almacensms_rojoamparo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ladm_u4_p1_almacensms_rojoamparo.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        revisarPermisos()

        binding.btnEnviar.setOnClickListener {
            try {

                val smsManager:SmsManager
                if (Build.VERSION.SDK_INT>=32) {
                    smsManager = this.getSystemService(SmsManager::class.java)
                }
                else{
                    smsManager = SmsManager.getDefault()
                }

                smsManager.sendTextMessage(binding.textTelefono.text.toString(), null, binding.textMensaje.text.toString(),
                    null, null)

                Toast.makeText(this, "Mensaje enviado!", Toast.LENGTH_LONG).show()

                insertarDatosFirebase()

            } catch (e: Exception) {

                Toast.makeText(this, "Mensaje no enviado..", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnRegistros.setOnClickListener {
            startActivity(Intent(this,MainActivity2::class.java))
        }
    }

    private fun insertarDatosFirebase() {
        val baseRemota = FirebaseFirestore.getInstance()

        val datos = hashMapOf(
            "NUMERO" to binding.textTelefono.text.toString(),
            "MENSAJE" to binding.textMensaje.text.toString(),
            "FECHA" to Date()
        )

        baseRemota.collection("smsenviados")
            .add(datos)
            .addOnSuccessListener {
                Toast.makeText(this,"El mensaje se guardo con exito en la BD!",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "No se pudo guardar el mensaje en la BD!",Toast.LENGTH_LONG).show()
            }
    }

    private fun revisarPermisos(){
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS),101)
        }
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),101)
        }
    }
}