package com.ifpb.mongodb.dao;

import com.ifpb.mongodb.database.MongoConnectionPojo;
import com.ifpb.mongodb.model.ItemVenda;
import com.ifpb.mongodb.model.Venda;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.push;

public class VendaDao {
    private MongoCollection collection;

    public VendaDao(){
        collection = new MongoConnectionPojo ().getCollection ("Venda", Venda.class);
        /*collection.createIndex (Indexes.ascending ("Venda.codigo"));*/
    }

    public void salvar(Venda venda){
        collection.insertOne (venda);
    }

    public List<Venda> listar(){
        MongoCursor cursor = collection.find ().iterator ();

        List<Venda> vendas = new ArrayList<> ();

        while (cursor.hasNext ()){
            vendas.add ((Venda) cursor.next ());
        }
        return vendas;
    }

    public Venda buscarVenda(int codigo){
        return (Venda) collection.find(eq("codigo", codigo)).first();
    }

    public boolean excluirVenda(int codigo){
        DeleteResult deleteResult = collection.deleteOne(eq("codigo", codigo));
        if(deleteResult.getDeletedCount () != 0){
            return true;
        } return false;
    }

    public boolean adicionaProdutoEmVenda(int codVenda, ItemVenda itemVenda){
        if(buscarVenda (codVenda) != null){
            collection.updateOne(eq("codigo", codVenda), push ("itens", itemVenda));
        }
        return false;
    }


}
