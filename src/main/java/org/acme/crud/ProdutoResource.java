package org.acme.crud;

import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @GET
    public List<Produto> listar() {
        return Produto.listAll();
    }

    @POST
    @Transactional
    public Response criar(Produto produto) {
        if (produto.id != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID deve ser nulo ao criar um novo produto")
                    .build();
        }
        Produto.persist(produto);
        return Response.ok(produto).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, Produto produto) {
        Produto entidade = Produto.findById(id);
        if (entidade == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Produto não encontrado")
                    .build();
        }
        entidade.nome = produto.nome;
        entidade.preco = produto.preco;
        return Response.ok(entidade).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        if (id == null || id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        }
    
        Produto entidade = Produto.findById(id);
        if (entidade == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Produto com ID " + id + " não encontrado")
                    .build();
        }
    
        try {
            entidade.delete(); // Chama o método delete na instância do Produto
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao excluir o produto: " + e.getMessage())
                    .build();
        }
    }

}
