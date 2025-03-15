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

public class TipoDocumentoBDD {
	public ArrayList<TipoDocumento> recuperar() throws KrakeDevException {
		ArrayList<TipoDocumento> documentos = new ArrayList<TipoDocumento>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TipoDocumento tipoDoc = null;

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("SELECT codigo_doc, descripcion "
					+ "FROM tipo_documento");
			rs = ps.executeQuery();
	
			while (rs.next()) {
				String codigo_doc = rs.getString("codigo_doc");
				String descripcion = rs.getString("descripcion");
				

				tipoDoc = new TipoDocumento(codigo_doc, descripcion);
				documentos.add(tipoDoc);
			}

		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al Consultar. Detalle: " + e.getMessage());
		}
		return documentos;
	}
	
	public void insertarDocumento(TipoDocumento documento) throws KrakeDevException {
		Connection con = null;
		try {
			con = ConexionBDD.obtenerConexion();
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO tipo_documento(codigo_doc, descripcion) VALUES(?,?)");

			ps.setString(1, documento.getCodigo_doc());
			ps.setString(2, documento.getDescripcion_doc());
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
