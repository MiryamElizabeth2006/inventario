package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.TipoDocumentoBDD;
import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;
@Path("tiposdocumento")
public class ServicioTipoDocumento {
	@Path("recuperar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerTipoDocumento() {
		TipoDocumentoBDD documento = new TipoDocumentoBDD();
		ArrayList<TipoDocumento> documentos = null;
		try {
			documentos = documento.recuperar();
			return Response.ok(documentos).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		
	}
	
	@Path("insertarDocumento")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertar(TipoDocumento documento) {
		System.out.println("********" + documento);
		TipoDocumentoBDD cli = new TipoDocumentoBDD();
		try {
			cli.insertarDocumento(documento);
			return Response.ok().build();

		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();

		}
	}
}

