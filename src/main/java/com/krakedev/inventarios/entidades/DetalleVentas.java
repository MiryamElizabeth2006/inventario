package com.krakedev.inventarios.entidades;
import java.math.BigDecimal;

public class DetalleVentas {
	private int codigo_dv;
	private Ventas cabecera_ventas;
	private Producto producto;
	private int cantidad_vendida;
	private BigDecimal precio_venta;
	private BigDecimal subtotal;
	private BigDecimal subtotal_iva;

	public DetalleVentas() {
	}

	public DetalleVentas(int codigo_dv, Ventas cabecera_ventas, Producto producto, int cantidad_vendida,
			BigDecimal precio_venta, BigDecimal subtotal, BigDecimal subtotal_iva) {
		super();
		this.codigo_dv = codigo_dv;
		this.cabecera_ventas = cabecera_ventas;
		this.producto = producto;
		this.cantidad_vendida = cantidad_vendida;
		this.precio_venta = precio_venta;
		this.subtotal = subtotal;
		this.subtotal_iva = subtotal_iva;
	}

	public int getCodigo_dv() {
		return codigo_dv;
	}

	public void setCodigo_dv(int codigo_dv) {
		this.codigo_dv = codigo_dv;
	}

	public Ventas getCabecera_ventas() {
		return cabecera_ventas;
	}

	public void setCabecera_ventas(Ventas cabecera_ventas) {
		this.cabecera_ventas = cabecera_ventas;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad_vendida() {
		return cantidad_vendida;
	}

	public void setCantidad_vendida(int cantidad_vendida) {
		this.cantidad_vendida = cantidad_vendida;
	}

	public BigDecimal getPrecio_venta() {
		return precio_venta;
	}

	public void setPrecio_venta(BigDecimal precio_venta) {
		this.precio_venta = precio_venta;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getSubtotal_iva() {
		return subtotal_iva;
	}

	public void setSubtotal_iva(BigDecimal subtotal_iva) {
		this.subtotal_iva = subtotal_iva;
	}

	@Override
	public String toString() {
		return "DetalleVentas [codigo_dv=" + codigo_dv + ", cabecera_ventas=" + cabecera_ventas + ", producto="
				+ producto + ", cantidad_vendida=" + cantidad_vendida + ", precio_venta=" + precio_venta + ", subtotal="
				+ subtotal + ", subtotal_iva=" + subtotal_iva + "]";
	}
}
