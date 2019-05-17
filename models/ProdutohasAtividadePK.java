/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author andre
 */
@Embeddable
public class ProdutohasAtividadePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Produto_idProduto")
    private int produtoidProduto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Atividade_idAtividades")
    private int atividadeidAtividades;

    public ProdutohasAtividadePK() {
    }

    public ProdutohasAtividadePK(int produtoidProduto, int atividadeidAtividades) {
        this.produtoidProduto = produtoidProduto;
        this.atividadeidAtividades = atividadeidAtividades;
    }

    public int getProdutoidProduto() {
        return produtoidProduto;
    }

    public void setProdutoidProduto(int produtoidProduto) {
        this.produtoidProduto = produtoidProduto;
    }

    public int getAtividadeidAtividades() {
        return atividadeidAtividades;
    }

    public void setAtividadeidAtividades(int atividadeidAtividades) {
        this.atividadeidAtividades = atividadeidAtividades;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) produtoidProduto;
        hash += (int) atividadeidAtividades;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProdutohasAtividadePK)) {
            return false;
        }
        ProdutohasAtividadePK other = (ProdutohasAtividadePK) object;
        if (this.produtoidProduto != other.produtoidProduto) {
            return false;
        }
        if (this.atividadeidAtividades != other.atividadeidAtividades) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.ProdutohasAtividadePK[ produtoidProduto=" + produtoidProduto + ", atividadeidAtividades=" + atividadeidAtividades + " ]";
    }
    
}
