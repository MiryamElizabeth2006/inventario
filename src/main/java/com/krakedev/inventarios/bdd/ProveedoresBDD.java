package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class ProveedoresBDD {
	public ArrayList<Proveedor> buscar(String subcadena) throws KrakeDevException {
		ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Proveedor proveedor = null;

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("SELECT prov.identificador, prov.tipo_documento_prov,td.descripcion,  prov.nombre, prov.telefono, prov.correo, prov.direccion " 
					+ "FROM proveedores prov, tipo_documento td " 
				    + "WHERE prov.tipo_documento_prov = td.codigo_doc " 
				    + "and upper(nombre) LIKE ?");

			ps.setString(1, "%"+subcadena.toUpperCase()+"%");
			rs = ps.executeQuery();
	
			while (rs.next()) {
				String identificador = rs.getString("identificador");
				String codigoTipoDocumento = rs.getString("tipo_documento_prov");
				String descripcionTipoDocumento = rs.getString("descripcion"); 
				String nombre = rs.getString("nombre");
				String telefono = rs.getString("telefono");
				String correo = rs.getString("correo");
				String direccion = rs.getString("direccion");
				
				TipoDocumento td = new TipoDocumento(codigoTipoDocumento, descripcionTipoDocumento);

				proveedor = new Proveedor(identificador, td, nombre, telefono, correo, direccion);
				proveedores.add(proveedor);
			}

		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al Consultar. Detalle: " + e.getMessage());
		}
		return proveedores;
	}
	
	public void crear(Proveedor proveedor) throws KrakeDevException {
		Connection con = null;
		try {
			con = ConexionBDD.obtenerConexion();
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO proveedores (identificador, tipo_documento_prov, nombre, telefono, correo, direccion) VALUES (?, ?, ?, ?, ?, ?)");

			ps.setString(1, proveedor.getIdentificador());
			ps.setObject(2, proveedor.getTipoDocumento().getCodigo_doc());
			ps.setString(3, proveedor.getNombre());
			ps.setString(4, proveedor.getTelefono());
			ps.setString(5, proveedor.getCorreo());
			ps.setString(6, proveedor.getDireccion());
			
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar cliente. Detalle: " + e.getMessage());
		} catch (KrakeDevException e) {
			throw e;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
