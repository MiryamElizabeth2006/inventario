package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.CategoriaUDM;
import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.EstadoPedido;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.entidades.UnidadDeMedida;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class VariosBDD {
	public void actualizarProducto(Producto producto) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("Update productos set nombre = ?, udm = ?, precio_venta = ?, tiene_iva = ?, coste = ?, categoria_prod = ?, stock = ? where codigo_prod = ?");

			ps.setString(1, producto.getNombre());
			ps.setString(2, producto.getUnidadMedida().getNombre());
			ps.setBigDecimal(3, producto.getPrecioVenta());
			ps.setBoolean(4, producto.isTieneIva());
			ps.setBigDecimal(5, producto.getCoste());
			ps.setInt(6, producto.getCategoria().getCodigo());
			ps.setInt(7, producto.getStock());
			ps.setInt(8, producto.getCodigo());
			System.out.println("ACTUALIZANDO>>>>>>>>>"+ps);
			ps.executeUpdate();
			
			System.out.println();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al Actualizar producto. Detalle: " + e.getMessage());
		}
	}

	public void insertar(Categoria categoria) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("insert into categorias(nombre, categoria_padre) " + " values(?, ?);");

			ps.setString(1, categoria.getNombre());
			ps.setInt(2, categoria.getCategoriaPadre().getCodigo());

			ps.executeUpdate();

		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar Categoria. Detalle: " + e.getMessage());
		}
	}

	public void actualizar(Categoria categoria) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("Update categorias set nombre = ?, categoria_padre = ? where codigo_cat = ?");

			ps.setString(1, categoria.getNombre());
			ps.setInt(2, categoria.getCategoriaPadre().getCodigo());
			ps.setInt(3, categoria.getCodigo());

			ps.executeUpdate();

		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al Actualizar Categoria. Detalle: " + e.getMessage());
		}
	}

	public ArrayList<Categoria> buscar() throws KrakeDevException {
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Categoria categoria;

		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("select codigo_cat, nombre, categoria_padre from categorias");

			rs = ps.executeQuery();
			while (rs.next()) {
				int codigo = rs.getInt("codigo_cat");
				String nombre = rs.getString("nombre");
				Categoria categoriaPadre = new Categoria();
				categoria = new Categoria(codigo, nombre, categoriaPadre);
				categorias.add(categoria);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al Buscar Categoria. Detalle: " + e.getMessage());
		}
		return categorias;
	}

	
	public ArrayList<Pedido> buscarPorProveedor(String identificador) throws KrakeDevException {
		ArrayList<Pedido> listPedidos = new ArrayList<Pedido>();
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psAux = null;
		ResultSet rs = null;
		ResultSet rsAux = null;
		Pedido p = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					"select pro.identificador, pro.nombre, pro.telefono, pro.correo, pro.direccion, td.codigo_doc, "
							+ "td.descripcion AS descipcion_doc, cp.codigocp, cp.fecha, cp.estado, ep.descripcion AS descripcion_estado "
							+ "from proveedores pro, cabecera_pedido cp, estados_pedido ep, tipo_documento td "
							+ "Where cp.proveedor = pro.identificador " + "AND ep.codigo_ped = cp.estado "
							+ "AND td.codigo_doc = pro.tipo_documento_prov " + "AND cp.proveedor = ?");
			ps.setString(1, identificador);
			rs = ps.executeQuery();
			while (rs.next()) {
				String codigoTd = rs.getString("codigo_doc");
				String descripcionTd = rs.getString("descipcion_doc");
				TipoDocumento td = new TipoDocumento(codigoTd, descripcionTd);

				String nombre = rs.getString("nombre");
				String telefono = rs.getString("telefono");
				String correo = rs.getString("correo");
				String direccion = rs.getString("direccion");
				Proveedor pro = new Proveedor(identificador, td, nombre, telefono, correo, direccion);

				String codigoEstado = rs.getString("estado");
				String descripcionEstado = rs.getString("descripcion_estado");
				EstadoPedido ep = new EstadoPedido(codigoEstado, descripcionEstado);

				int codigo = rs.getInt("codigocp");
				Date fecha = rs.getDate("fecha");
				p = new Pedido(codigo, pro, fecha, ep);

				psAux = con.prepareStatement(
						"select dt.codigo_det AS codigo_detalle , dt.cabecera_pedido, pro.codigo_prod, pro.nombre, "
								+ "udm.codigo_um, udm.descripcion AS descripcion_udm, udm.categoria_udm, cast(pro.precio_venta as decimal(6,2)), "
								+ "pro.tiene_iva, cast(pro.coste as decimal(5,4)), pro.categoria_prod, c.nombre As nombre_categoria, pro.stock, "
								+ "dt.cantidad_solicitada, cast(dt.subtotal as decimal(10,4)), dt.cantidad_recibida "
								+ "from detalle_pedido dt, productos pro, categorias c, unidades_medida udm "
								+ "Where dt.producto = pro.codigo_prod " + "AND pro.categoria_prod = c.codigo_cat "
								+ "AND pro.udm = udm.codigo_um " + "AND dt.cabecera_pedido = ?");
				psAux.setInt(1, codigo);
				rsAux = psAux.executeQuery();
				ArrayList<DetallePedido> listDetalle = new ArrayList<DetallePedido>();
				while (rsAux.next()) {
					String codigoUdm = rsAux.getString("codigo_um");
					String descripcionUdm = rsAux.getString("descripcion_udm");
					CategoriaUDM categoriaUdm = new CategoriaUDM(rsAux.getString("categoria_udm"), null);
					UnidadDeMedida udm = new UnidadDeMedida(codigoUdm, descripcionUdm, categoriaUdm);

					int codigoCat = rsAux.getInt("categoria_prod");
					String nombreCategoria = rsAux.getString("nombre_categoria");
					Categoria cate = new Categoria(codigoCat, nombreCategoria, null);

					int codigoProducto = rsAux.getInt("codigo_prod");
					String nombreProducto = rsAux.getString("nombre");
					BigDecimal precioVenta = rsAux.getBigDecimal("precio_venta");
					boolean tieneIva = rsAux.getBoolean("tiene_iva");
					BigDecimal coste = rsAux.getBigDecimal("coste");
					int stock = rsAux.getInt("stock");
					Producto producto = new Producto(codigoProducto, nombreProducto, udm, precioVenta, tieneIva, coste,
							cate, stock);

					int codigoDetalle = rsAux.getInt("cabecera_pedido");
					int cantidadSolicitada = rsAux.getInt("cantidad_solicitada");
					BigDecimal Subtotal = rsAux.getBigDecimal("subtotal");
					int cantidadRecibida = rsAux.getInt("cantidad_recibida");
					DetallePedido detalle = new DetallePedido(codigoDetalle, null, producto, cantidadSolicitada,
							Subtotal, cantidadRecibida);
					listDetalle.add(detalle);

				}
				p.setDetalles(listDetalle);
				listPedidos.add(p);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al Buscar Proveedor: " + e.getMessage());
		}
		return listPedidos;
	}

	public Proveedor buscarPorIdentificador(String cadena) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Proveedor proveedor = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					"select pro.identificador, pro.tipo_documento_prov, td.descripcion, pro.nombre, pro.telefono, pro.correo, pro.direccion from proveedores pro, tipo_documento td "
							+ "Where pro.tipo_documento_prov = td.codigo_doc " + "AND identificador = ?");
			ps.setString(1, cadena);
			rs = ps.executeQuery();
			if (rs.next()) {
				String identificador = rs.getString("identificador");
				String codigoTipoDocumento = rs.getString("tipo_documento_prov");
				String descripcionTd = rs.getString("descripcion");
				String nombre = rs.getString("nombre");
				String telefono = rs.getString("telefono");
				String correo = rs.getString("correo");
				String direccion = rs.getString("direccion");
				TipoDocumento tp = new TipoDocumento(codigoTipoDocumento, descripcionTd);
				proveedor = new Proveedor(identificador, tp, nombre, telefono, correo, direccion);
			}

		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al Buscar Proveedor: " + e.getMessage());
		}
		return proveedor;
	}

	public Producto buscarPorId(int codigo) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto p = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					"select pro.codigo_prod, pro.nombre, udm.codigo_um, udm.descripcion AS descripcion_udm, udm.categoria_udm, cast(pro.precio_venta as decimal(6,2)), "
							+ "pro.tiene_iva, cast(pro.coste as decimal(5,4)), pro.categoria_prod, c.nombre As nombre_categoria, pro.stock "
							+ "from productos pro, categorias c, unidades_medida udm "
							+ "Where pro.categoria_prod = c.codigo_cat " + "AND pro.udm = udm.codigo_um "
							+ "AND pro.codigo_prod = ?;");
			ps.setInt(1, codigo);
			rs = ps.executeQuery();
			if (rs.next()) {
				String codigoUdm = rs.getString("codigo_um");
				String descripcion = rs.getString("descripcion_udm");
				CategoriaUDM categoriaUdm = new CategoriaUDM(rs.getString("categoria_udm"), null);
				UnidadDeMedida udm = new UnidadDeMedida(codigoUdm, descripcion, categoriaUdm);

				int codigoCat = rs.getInt("categoria_prod");
				String nombreCategoria = rs.getString("nombre");
				Categoria cate = new Categoria(codigoCat, nombreCategoria, null);

				int codigoProducto = rs.getInt("codigo_prod");
				String nombreProducto = rs.getString("nombre");
				BigDecimal precioVenta = rs.getBigDecimal("precio_venta");
				boolean tieneIva = rs.getBoolean("tiene_iva");
				BigDecimal coste = rs.getBigDecimal("coste");
				;
				int stock = rs.getInt("stock");
				p = new Producto(codigoProducto, nombreProducto, udm, precioVenta, tieneIva, coste, cate, stock);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al Buscar Producto: " + e.getMessage());
		}

		return p;
	}
}
