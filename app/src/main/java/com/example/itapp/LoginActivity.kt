package com.example.itapp
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button //btn login
    private lateinit var contactButton: Button //btn contacto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        supportActionBar?.hide()

        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.btnlogin)
        contactButton = findViewById(R.id.showPopupButton)

        contactButton.setOnClickListener { showContactDialog() }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validar credenciales en un Coroutine
            lifecycleScope.launch {
                val isValidCredentials = validateCredentials(username, password)
                if (isValidCredentials) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("user_name", username);
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "Error en la autenticación de las credenciales", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showContactDialog() {
        val message = "Si tienes problemas para acceder, por favor póngase en contacto con el equipo de sistemas."
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Contacto Sistemas")
        builder.setMessage(message)
        builder.setPositiveButton("Confirmar") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private suspend fun validateCredentials(username: String, password: String): Boolean {
        // Lógica para conectar a la base de datos y validar credenciales
        // Aquí debes implementar la conexión a la base de datos y la validación de credenciales

        return withContext(Dispatchers.IO) {
            try {
                Class.forName("org.postgresql.Driver")
                val url = "jdbc:postgresql://10.0.2.2:5432/CMB"
                val connection = DriverManager.getConnection(url, "postgres", "123456")

                val query = "SELECT * FROM public.usuarios WHERE user_name = ? AND user_psw = ?"
                val statement: PreparedStatement = connection.prepareStatement(query)
                statement.setString(1, username)
                statement.setString(2, password)

                val resultSet: ResultSet = statement.executeQuery()
                resultSet.next() // Si existe al menos una fila, las credenciales son válidas

            } catch (e: SQLException) {
                e.printStackTrace()
                false
            }
        }
    }
}

