package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class PedidoBDD {
	public void insertar(Pedido pedido) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rsClave = null;
		PreparedStatement psDet = null;
		int codigoCabecera = 0;

		Date fechaActual = new Date();
		java.sql.Date fechaSQL = new java.sql.Date(fechaActual.getTime());

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("insert into cabecera_pedido(proveedor, fecha, estado) " + "values(?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, pedido.getProveedor().getIdentificador());
			ps.setDate(2, fechaSQL);
			ps.setString(3, "S");

			ps.executeUpdate();

			rsClave = ps.getGeneratedKeys();

			if (rsClave.next()) {
				codigoCabecera = rsClave.getInt(1);
			}

			ArrayList<DetallePedido> detallesPedido = pedido.getDetalles();
			DetallePedido det;
			for (int i = 0; i < detallesPedido.size(); i++) {
				det = detallesPedido.get(i);
				psDet = con.prepareStatement(
						"insert into detalle_pedido(cabecera_pedido, producto, cantidad_solicitada,cantidad_recibida, subtotal) "
								+ "values(?,?,?,?,?)");

				psDet.setInt(1, codigoCabecera);
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidadSolicitada());
				psDet.setInt(4, 0);
				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadSolicitada());
				BigDecimal subtotal = pv.multiply(cantidad);
				psDet.setBigDecimal(5, subtotal);

				psDet.executeUpdate();

				System.out.println(psDet);
			}

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

	// Modificar lo que se inserto en pedidios y detalle pedidos
	public void modificar(Pedido pedido) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		PreparedStatement psHis=null;
		Date fechaActual = new Date();
		Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime());


		
		try {
			con = ConexionBDD.obtenerConexion();
			ps=con.prepareStatement("update cabecera_pedido set estado = 'R' WHERE codigoCP = ?");
			ps.setInt(1, pedido.getCodigo());
			
			ps.executeUpdate();

			ArrayList<DetallePedido> detallePedido = pedido.getDetalles();
			DetallePedido det;
			for(int i=0;i<detallePedido.size();i++) {
				det = detallePedido.get(i);
				psDet=con.prepareStatement("UPDATE detalle_pedido "
						+ "	SET subtotal=?, cantidad_recibida=? "
						+ "	WHERE codigo_det=?;");

				psDet.setInt(2, det.getCantidadRecibida());
				
				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadRecibida());
				BigDecimal subtotal = pv.multiply(cantidad);
				psDet.setBigDecimal(1, subtotal);
				
				psDet.setInt(3, det.getCodigo());
				
				psDet.executeUpdate();	
				
				//Insertar el la tabla historial
				
				psHis = con.prepareStatement("INSERT INTO historial_stock( "
						+ "	fecha_hora, referencia, producto, cantidad) "
						+ "	VALUES (?, ?, ?, ?);",Statement.RETURN_GENERATED_KEYS);
				
				
				psHis.setTimestamp(1,fechaHoraActual);
				psHis.setString(2, "Pedido "+pedido.getCodigo());
				psHis.setInt(3, det.getProducto().getCodigo());
				psHis.setInt(4, det.getCantidadRecibida());
				
				psHis.executeUpdate();
				
			
			System.out.println("Detalle de pedio agregado"+psDet);
			}}catch(SQLException e)
	{
		e.printStackTrace();
		throw new KrakeDevException("Error al insertar cliente. Detalle: " + e.getMessage());
	}catch(KrakeDevException e)
	{
		throw e;
	}finally
	{
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
}}
