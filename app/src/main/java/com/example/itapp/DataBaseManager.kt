package com.example.itapp

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DataBaseManager {
    // Cambia estos valores según tu configuración de base de datos
    private val url = "jdbc:postgresql://10.0.2.2:5432/CMB"
    private val usuario = "postgres"
    private val contrasena = "123456"

    // Función para establecer la conexión a la base de datos
    private fun obtenerConexion(): Connection {
        return DriverManager.getConnection(url, usuario, contrasena)
    }

    // Función para registrar un nuevo usuario
    fun registrarUsuario(nombre: String, contrasena: String) {
        try {
            val conexion = obtenerConexion()

            val query = "INSERT INTO usuarios (nombre, contrasena) VALUES (?, ?);"
            val statement = conexion.prepareStatement(query)

            statement.setString(1, nombre)
            statement.setString(2, contrasena)

            statement.executeUpdate()

            statement.close()
            conexion.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    // Función para verificar las credenciales del usuario
    fun verificarCredenciales(nombre: String, contrasena: String): Boolean {
        try {
            val conexion = obtenerConexion()

            val query = "SELECT * FROM public.usuarios WHERE user_name = ? AND user_psw = ?"
            val statement = conexion.prepareStatement(query)

            statement.setString(1, nombre)
            statement.setString(2, contrasena)

            val resultSet = statement.executeQuery()

            val resultado = resultSet.next()

            statement.close()
            resultSet.close()
            conexion.close()

            return resultado
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }
    }

    // Función para obtener el nombre del usuario desde la base de datos (puedes usarla en la pantalla de bienvenida)
    fun obtenerNombreUsuario(s: String): String? {
        try {
            val conexion = obtenerConexion()

            val query = "SELECT user_name FROM public.usuarios;"
            val statement = conexion.prepareStatement(query)

            val resultSet = statement.executeQuery()

            var nombreUsuario: String? = null
            if (resultSet.next()) {
                nombreUsuario = resultSet.getString("user_name")
            }

            statement.close()
            resultSet.close()
            conexion.close()

            return nombreUsuario
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }
}
