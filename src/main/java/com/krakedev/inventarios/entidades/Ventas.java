package com.krakedev.inventarios.entidades;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;

public class Ventas {
	private int codigo_vent;
	private Date fecha_hora;
	private BigDecimal total_sin_iva;
	private BigDecimal iva;
	private BigDecimal total;

	private ArrayList<DetalleVentas> detallesV;

	public Ventas() {
	}

	public Ventas(int codigo_vent, Date fecha_hora, BigDecimal total_sin_iva, BigDecimal iva, BigDecimal total,
			ArrayList<DetalleVentas> detallesV) {
		super();
		this.codigo_vent = codigo_vent;
		this.fecha_hora = fecha_hora;
		this.total_sin_iva = total_sin_iva;
		this.iva = iva;
		this.total = total;
		this.detallesV = detallesV;
	}

	public int getCodigo_vent() {
		return codigo_vent;
	}

	public void setCodigo_vent(int codigo_vent) {
		this.codigo_vent = codigo_vent;
	}

	public Date getFecha_hora() {
		return fecha_hora;
	}

	public void setFecha_hora(Date fecha_hora) {
		this.fecha_hora = fecha_hora;
	}

	public BigDecimal getTotal_sin_iva() {
		return total_sin_iva;
	}

	public void setTotal_sin_iva(BigDecimal total_sin_iva) {
		this.total_sin_iva = total_sin_iva;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public ArrayList<DetalleVentas> getDetalles() {
		return detallesV;
	}

	public void setDetalles(ArrayList<DetalleVentas> detalles) {
		this.detallesV = detalles;
	}

	@Override
	public String toString() {
		return "Ventas [codigo_vent=" + codigo_vent + ", fecha_hora=" + fecha_hora + ", total_sin_iva=" + total_sin_iva
				+ ", iva=" + iva + ", total=" + total + ", detallesV=" + detallesV + "]";
	}

}
