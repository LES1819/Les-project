/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andre
 */
@Entity
@Table(name = "Produto_has_Atividade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProdutohasAtividade.findAll", query = "SELECT p FROM ProdutohasAtividade p"),
    @NamedQuery(name = "ProdutohasAtividade.findByProdutoidProduto", query = "SELECT p FROM ProdutohasAtividade p WHERE p.produtohasAtividadePK.produtoidProduto = :produtoidProduto"),
    @NamedQuery(name = "ProdutohasAtividade.findByAtividadeidAtividades", query = "SELECT p FROM ProdutohasAtividade p WHERE p.produtohasAtividadePK.atividadeidAtividades = :atividadeidAtividades"),
    @NamedQuery(name = "ProdutohasAtividade.associatedProducts", query = "SELECT p FROM ProdutohasAtividade p WHERE p.atividade = :atividade"),
    @NamedQuery(name = "ProdutohasAtividade.findByDataCriacao", query = "SELECT p FROM ProdutohasAtividade p WHERE p.dataCriacao = :dataCriacao"),
    @NamedQuery(name = "ProdutohasAtividade.findByIdAssociacao", query = "SELECT p FROM ProdutohasAtividade p WHERE p.idAssociacao = :idAssociacao")})
public class ProdutohasAtividade implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProdutohasAtividadePK produtohasAtividadePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @Basic(optional = false)
    @Column(name = "id_Associacao")
    private int idAssociacao;
    @JoinColumn(name = "Atividade_idAtividades", referencedColumnName = "idAtividades", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Atividade atividade;
    @JoinColumn(name = "Produto_idProduto", referencedColumnName = "idProduto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Produto produto;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;

    public ProdutohasAtividade() {
    }

    public ProdutohasAtividade(ProdutohasAtividadePK produtohasAtividadePK) {
        this.produtohasAtividadePK = produtohasAtividadePK;
    }

    public ProdutohasAtividade(ProdutohasAtividadePK produtohasAtividadePK, Date dataCriacao, int idAssociacao) {
        this.produtohasAtividadePK = produtohasAtividadePK;
        this.dataCriacao = dataCriacao;
        this.idAssociacao = idAssociacao;
    }

    public ProdutohasAtividade(int produtoidProduto, int atividadeidAtividades) {
        this.produtohasAtividadePK = new ProdutohasAtividadePK(produtoidProduto, atividadeidAtividades);
    }

    public ProdutohasAtividadePK getProdutohasAtividadePK() {
        return produtohasAtividadePK;
    }

    public void setProdutohasAtividadePK(ProdutohasAtividadePK produtohasAtividadePK) {
        this.produtohasAtividadePK = produtohasAtividadePK;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getIdAssociacao() {
        return idAssociacao;
    }

    public void setIdAssociacao(int idAssociacao) {
        this.idAssociacao = idAssociacao;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Utilizador getUtilizadoridUtilizador() {
        return utilizadoridUtilizador;
    }

    public void setUtilizadoridUtilizador(Utilizador utilizadoridUtilizador) {
        this.utilizadoridUtilizador = utilizadoridUtilizador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (produtohasAtividadePK != null ? produtohasAtividadePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProdutohasAtividade)) {
            return false;
        }
        ProdutohasAtividade other = (ProdutohasAtividade) object;
        if ((this.produtohasAtividadePK == null && other.produtohasAtividadePK != null) || (this.produtohasAtividadePK != null && !this.produtohasAtividadePK.equals(other.produtohasAtividadePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.ProdutohasAtividade[ produtohasAtividadePK=" + produtohasAtividadePK + " ]";
    }
    
}
