/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import Modelo.Cliente;
import Util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author kreiv
 */
public class ClienteDAO {

    public void crearCliente(Cliente cliente) throws SQLException {
        String sql = """
        INSERT INTO Clientes (
            primer_nombre, 
            segundo_nombre, 
            primer_apellido, 
            segundo_apellido, 
            celular, 
            direccion, 
            cedula
        ) VALUES (?, ?, ?, ?, ?, ?, ?)""";

        try (Connection c = ConexionDB.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, cliente.getPrimerNombre());
            stmt.setString(2, cliente.getSegundoNombre());
            stmt.setString(3, cliente.getPrimerApellido());
            stmt.setString(4, cliente.getSegundoApellido());
            stmt.setString(5, cliente.getCelular());
            stmt.setString(6, cliente.getDireccion());
            stmt.setString(7, cliente.getCedula());
            stmt.executeUpdate();
        }
    }

    public List<Cliente> leerTodosClientes() throws SQLException {
        String sql = "SELECT * FROM Clientes";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection c = ConexionDB.getConnection(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setPrimerNombre(rs.getString("primer_nombre"));
                cliente.setSegundoNombre(rs.getString("segundo_nombre"));
                cliente.setPrimerApellido(rs.getString("primer_apellido"));
                cliente.setSegundoApellido(rs.getString("segundo_apellido"));
                cliente.setCelular(rs.getString("celular"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setCedula(rs.getString("cedula"));
                clientes.add(cliente);
            }
        }
        return clientes;
    }

// Métodos para Actualizar y Eliminar
// Método para actualizar un cliente
    public void actualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE Clientes SET primer_nombre = ?, segundo_nombre = ?, primer_apellido = ?, segundo_apellido = ?, celular = ?, direccion = ?, cedula = ? WHERE id_cliente = ?";

        try (Connection c = ConexionDB.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, cliente.getPrimerNombre());
            stmt.setString(2, cliente.getSegundoNombre());
            stmt.setString(3, cliente.getPrimerApellido());
            stmt.setString(4, cliente.getSegundoApellido());
            stmt.setString(5, cliente.getCelular());
            stmt.setString(6, cliente.getDireccion());
            stmt.setString(7, cliente.getCedula());
            stmt.setInt(8, cliente.getIdCliente());
            stmt.executeUpdate();
        }
    }
// Método para eliminar un cliente

    public void eliminarCliente(int idCliente) throws SQLException {
        String sql = "DELETE FROM Clientes WHERE id_cliente = ?";

        try (Connection c = ConexionDB.getConnection(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        try {
        ClienteDAO dao = new ClienteDAO();
        // Actualizar un cliente
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1); // ID existente
        cliente.setPrimerNombre("Juan");
        cliente.setSegundoNombre("Carlos");
        cliente.setPrimerApellido("Pérez");
        cliente.setSegundoApellido("Gómez");
        cliente.setCelular("12345678");
        cliente.setDireccion("Calle 123");
        cliente.setCedula("12345678");
        dao.actualizarCliente(cliente);
        System.out.println("Cliente actualizado.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
