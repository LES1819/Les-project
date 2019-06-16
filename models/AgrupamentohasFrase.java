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
 * @author joaohenriques
 */
@Entity
@Table(name = "Agrupamento_has_Frase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgrupamentohasFrase.findAll", query = "SELECT a FROM AgrupamentohasFrase a"),
    @NamedQuery(name = "AgrupamentohasFrase.findByAgrupamentoidAgrupamento", query = "SELECT a FROM AgrupamentohasFrase a WHERE a.agrupamentohasFrasePK.agrupamentoidAgrupamento = :agrupamentoidAgrupamento"),
    @NamedQuery(name = "AgrupamentohasFrase.findByIdFraseIdAgrup", query = "SELECT a FROM AgrupamentohasFrase a WHERE (a.agrupamentohasFrasePK.agrupamentoidAgrupamento = :idAgrup AND a.agrupamentohasFrasePK.fraseidFrase = :idFrase)"),
    @NamedQuery(name = "AgrupamentohasFrase.findByFraseidFrase", query = "SELECT a FROM AgrupamentohasFrase a WHERE a.agrupamentohasFrasePK.fraseidFrase = :fraseidFrase"),
    @NamedQuery(name = "AgrupamentohasFrase.findByIdAssociacao", query = "SELECT a FROM AgrupamentohasFrase a WHERE a.idAssociacao = :idAssociacao"),
    @NamedQuery(name = "AgrupamentohasFrase.findByDataCriacao", query = "SELECT a FROM AgrupamentohasFrase a WHERE a.dataCriacao = :dataCriacao")})
public class AgrupamentohasFrase implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AgrupamentohasFrasePK agrupamentohasFrasePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Associacao")
    private int idAssociacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @JoinColumn(name = "Agrupamento_idAgrupamento", referencedColumnName = "idAgrupamento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Agrupamento agrupamento;
    @JoinColumn(name = "Frase_idFrase", referencedColumnName = "idFrase", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Frase frase;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;

    public AgrupamentohasFrase() {
    }

    public AgrupamentohasFrase(AgrupamentohasFrasePK agrupamentohasFrasePK) {
        this.agrupamentohasFrasePK = agrupamentohasFrasePK;
    }

    public AgrupamentohasFrase(AgrupamentohasFrasePK agrupamentohasFrasePK, int idAssociacao, Date dataCriacao) {
        this.agrupamentohasFrasePK = agrupamentohasFrasePK;
        this.idAssociacao = idAssociacao;
        this.dataCriacao = dataCriacao;
    }

    public AgrupamentohasFrase(int agrupamentoidAgrupamento, int fraseidFrase) {
        this.agrupamentohasFrasePK = new AgrupamentohasFrasePK(agrupamentoidAgrupamento, fraseidFrase);
    }

    public AgrupamentohasFrasePK getAgrupamentohasFrasePK() {
        return agrupamentohasFrasePK;
    }

    public void setAgrupamentohasFrasePK(AgrupamentohasFrasePK agrupamentohasFrasePK) {
        this.agrupamentohasFrasePK = agrupamentohasFrasePK;
    }

    public int getIdAssociacao() {
        return idAssociacao;
    }

    public void setIdAssociacao(int idAssociacao) {
        this.idAssociacao = idAssociacao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Agrupamento getAgrupamento() {
        return agrupamento;
    }

    public void setAgrupamento(Agrupamento agrupamento) {
        this.agrupamento = agrupamento;
    }

    public Frase getFrase() {
        return frase;
    }

    public void setFrase(Frase frase) {
        this.frase = frase;
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
        hash += (agrupamentohasFrasePK != null ? agrupamentohasFrasePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgrupamentohasFrase)) {
            return false;
        }
        AgrupamentohasFrase other = (AgrupamentohasFrase) object;
        if ((this.agrupamentohasFrasePK == null && other.agrupamentohasFrasePK != null) || (this.agrupamentohasFrasePK != null && !this.agrupamentohasFrasePK.equals(other.agrupamentohasFrasePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.AgrupamentohasFrase[ agrupamentohasFrasePK=" + agrupamentohasFrasePK + " ]";
    }
    
}
