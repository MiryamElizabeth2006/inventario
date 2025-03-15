package com.krakedev.inventarios.servicios;

import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.krakedev.inventarios.bdd.PedidoBDD;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("pedidos")
public class ServiciosPedido {
	@Path("registrar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crear(Pedido pedido) {
		System.out.println("********" + pedido);
		PedidoBDD cli = new PedidoBDD();
		try {
			cli.insertar(pedido);
			return Response.ok().build();

		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();

		}
	}
	
	@Path("recibir")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response actualizar(Pedido pedido) {
		System.out.println("ACTUALIZANDO >>>>>>>>" + pedido);
		PedidoBDD cli = new PedidoBDD();
		try {
			cli.modificar(pedido);
			return Response.ok().build();

		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();

		}
	}
}
