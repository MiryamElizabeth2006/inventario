package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.VariosBDD;
import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("Categorias")
	public class ServiciosVarios {
		@Path("actualizarProducto")
		@PUT
		@Consumes("application/json")
		public Response actualizarProducto(Producto producto) {
			VariosBDD servBDD = new VariosBDD();
			try {
				servBDD.actualizarProducto(producto);;
				return Response.ok().build();
			} catch (KrakeDevException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}
		}
		
		@Path("insertar")
		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		public Response crear(Categoria categoria) {
			VariosBDD servBDD = new VariosBDD();
			try {
				servBDD.insertar(categoria);
				return Response.ok().build();
			} catch (KrakeDevException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}
		}
		
		@Path("actualizar")
		@PUT
		@Consumes(MediaType.APPLICATION_JSON)
		public Response actualizarCategoria(Categoria categoria) {
			VariosBDD servBDD = new VariosBDD();
			try {
				servBDD.actualizar(categoria);
				return Response.ok().build();
			} catch (KrakeDevException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}
		}
		
		@Path("buscar")
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response obtenerCategorias() {
			VariosBDD servBDD = new VariosBDD();
			ArrayList<Categoria> categorias = null;
			try {
				categorias = servBDD.buscar();
				return Response.ok(categorias).build();
			} catch (KrakeDevException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}
		}
		
		@Path("buscarPedido/{sub}")
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response buscar(@PathParam("sub")String proveedor){
			VariosBDD servBDD = new VariosBDD();
			ArrayList<Pedido> listaPedidos = null;
			try {
				listaPedidos = servBDD.buscarPorProveedor(proveedor);
				return Response.ok(listaPedidos).build();
			} catch (KrakeDevException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}
		}
		
		@Path("buscarProveedor/{cadena}")
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response buscarProveedor(@PathParam("cadena")String identificador){
			VariosBDD servBDD = new VariosBDD();
			Proveedor proveedor= null;
			try {
				proveedor = servBDD.buscarPorIdentificador(identificador);
				return Response.ok(proveedor).build();
			} catch (KrakeDevException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}
		}
		
		@Path("buscarProducto/{sub}")
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response buscarProducto(@PathParam("sub")int cadena){
			VariosBDD servBDD = new VariosBDD();
			Producto producto = null;
			try {
				producto = servBDD.buscarPorId(cadena);
				return Response.ok(producto).build();
			} catch (KrakeDevException e) {
				e.printStackTrace();
				return Response.serverError().build();
			}
		}
}
