package com.krakedev.inventarios.servicios;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.VentasBDD;
import com.krakedev.inventarios.entidades.Ventas;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("Ventas")
public class ServiciosVentas {

	@Path("guardar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response guardar(Ventas ventas) {
		System.out.println("********" + ventas);
		VentasBDD cli = new VentasBDD();
		try {
			cli.insertarVentas(ventas);
			return Response.ok().build();

		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();

		}
	}
}
