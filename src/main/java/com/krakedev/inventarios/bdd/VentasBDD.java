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

import com.krakedev.inventarios.entidades.DetalleVentas;
import com.krakedev.inventarios.entidades.Ventas;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class VentasBDD {
	public void insertarVentas(Ventas venta) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		PreparedStatement psCv = null;
		PreparedStatement psHis = null;
		ResultSet rsClave = null;
		int codigoCabeceraVentas = 0;
		
		Date fechaActual = new Date();
		Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime());

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("insert into cabecera_ventas(fecha_hora, total_sin_iva, iva, total) "
					+ "values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setTimestamp(1, fechaHoraActual);
			ps.setBigDecimal(2, BigDecimal.ZERO);
			ps.setBigDecimal(3, BigDecimal.ZERO);
			ps.setBigDecimal(4, BigDecimal.ZERO);	

			ps.executeUpdate();

			rsClave = ps.getGeneratedKeys();
			if (rsClave.next()) {
				codigoCabeceraVentas = rsClave.getInt(1);
			}

			ArrayList<DetalleVentas> detallesVenta = venta.getDetalles();
			for (DetalleVentas det : detallesVenta) {
				psDet = con.prepareStatement("insert into detalle_ventas(cabecera_ventas, producto, cantidad_vendida, precio_venta, subtotal, subtotal_iva) "
								+ "values(?,?,?,?,?,?)");

				psDet.setInt(1, codigoCabeceraVentas);
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidad_vendida());
				psDet.setBigDecimal(4, det.getProducto().getPrecioVenta());
				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidad_vendida());
				BigDecimal subtotal = pv.multiply(cantidad);
				double subtotalIvaDouble;
				BigDecimal subtotalIva;
				psDet.setBigDecimal(5, subtotal);
				
				if(det.getProducto().isTieneIva()) {
					double subtotalDouble = subtotal.doubleValue();
					double ivaDet = subtotalDouble * 0.12;
					subtotalIvaDouble = subtotalDouble + ivaDet;
					subtotalIva = BigDecimal.valueOf(subtotalIvaDouble);
				}else {
					subtotalIva = subtotal;
				}
				psDet.setBigDecimal(6, subtotalIva);
				psDet.executeUpdate();

				System.out.println(psDet);
			}
			
			BigDecimal totalSinIva = BigDecimal.ZERO;
			BigDecimal iva = BigDecimal.ZERO;
			BigDecimal total = BigDecimal.ZERO;
			
			for(DetalleVentas detalle : detallesVenta) {
				BigDecimal precioVenta = detalle.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(detalle.getCantidad_vendida());
				BigDecimal subtotal = precioVenta.multiply(cantidad);
				
				totalSinIva = totalSinIva.add(subtotal);
				
				if(detalle.getProducto().isTieneIva()) {
					double ivaDet = subtotal.doubleValue() * 0.12;
					iva = iva.add(BigDecimal.valueOf(ivaDet));
				}
			}
			
			total = totalSinIva.add(iva);
			
			psCv = con.prepareStatement("update cabecera_ventas set total_sin_iva = ?, iva = ?, total = ? where codigo_cab_vent = ?");
			psCv.setBigDecimal(1, totalSinIva);
			psCv.setBigDecimal(2, iva);
			psCv.setBigDecimal(3, total);
			psCv.setInt(4, codigoCabeceraVentas);
			psCv.executeUpdate();
			
			for(DetalleVentas det : detallesVenta){
				psHis = con.prepareStatement("insert into historial_stock(fecha_hora, referencia, producto, cantidad) "
						+"values(?, ?, ?, ?)");
				
				int CantidadV = det.getCantidad_vendida() * -1;
				psHis.setTimestamp(1, fechaHoraActual);
				psHis.setString(2, "Venta " + codigoCabeceraVentas);
				psHis.setInt(3, det.getProducto().getCodigo());
				psHis.setInt(4, CantidadV);
				psHis.executeUpdate();
			}
			
			
			System.out.println("Se crea historial "+ psHis);
			
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
