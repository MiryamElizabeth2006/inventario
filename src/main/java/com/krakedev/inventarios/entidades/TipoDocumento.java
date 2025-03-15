package com.krakedev.inventarios.entidades;

public class TipoDocumento {
	private String codigo_doc;
	private String descripcion_doc;

	public String getCodigo_doc() {
		return codigo_doc;
	}

	public void setCodigo_doc(String codigo_doc) {
		this.codigo_doc = codigo_doc;
	}

	public String getDescripcion_doc() {
		return descripcion_doc;
	}

	public void setDescripcion_doc(String descripcion_doc) {
		this.descripcion_doc = descripcion_doc;
	}

	public TipoDocumento() {
	}

	public TipoDocumento(String codigo_doc, String descripcion_doc) {
		super();
		this.codigo_doc = codigo_doc;
		this.descripcion_doc = descripcion_doc;
	}

	@Override
	public String toString() {
		return "TipoDocumento [codigo_doc=" + codigo_doc + ", descripcion_doc=" + descripcion_doc + "]";
	}

}
