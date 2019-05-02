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
@Table(name = "Papel_has_Atividade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PapelhasAtividade.findAll", query = "SELECT p FROM PapelhasAtividade p"),
    @NamedQuery(name = "PapelhasAtividade.findByPapelidPapel", query = "SELECT p FROM PapelhasAtividade p WHERE p.papelhasAtividadePK.papelidPapel = :papelidPapel"),
    @NamedQuery(name = "PapelhasAtividade.findByAtividadeidAtividades", query = "SELECT p FROM PapelhasAtividade p WHERE p.papelhasAtividadePK.atividadeidAtividades = :atividadeidAtividades"),
    @NamedQuery(name = "PapelhasAtividade.associatedPapers", query = "SELECT p FROM PapelhasAtividade p WHERE p.atividade = :atividade"),
    @NamedQuery(name = "PapelhasAtividade.findByDataCriacao", query = "SELECT p FROM PapelhasAtividade p WHERE p.dataCriacao = :dataCriacao"),
    @NamedQuery(name = "PapelhasAtividade.findByIdAssociacao", query = "SELECT p FROM PapelhasAtividade p WHERE p.idAssociacao = :idAssociacao")})
public class PapelhasAtividade implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PapelhasAtividadePK papelhasAtividadePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Associacao")
    private int idAssociacao;
    @JoinColumn(name = "Papel_idPapel", referencedColumnName = "idPapel", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Papel papel;
    @JoinColumn(name = "Atividade_idAtividades", referencedColumnName = "idAtividades", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Atividade atividade;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;

    public PapelhasAtividade() {
    }

    public PapelhasAtividade(PapelhasAtividadePK papelhasAtividadePK) {
        this.papelhasAtividadePK = papelhasAtividadePK;
    }

    public PapelhasAtividade(PapelhasAtividadePK papelhasAtividadePK, Date dataCriacao, int idAssociacao) {
        this.papelhasAtividadePK = papelhasAtividadePK;
        this.dataCriacao = dataCriacao;
        this.idAssociacao = idAssociacao;
    }

    public PapelhasAtividade(int papelidPapel, int atividadeidAtividades) {
        this.papelhasAtividadePK = new PapelhasAtividadePK(papelidPapel, atividadeidAtividades);
    }

    public PapelhasAtividadePK getPapelhasAtividadePK() {
        return papelhasAtividadePK;
    }

    public void setPapelhasAtividadePK(PapelhasAtividadePK papelhasAtividadePK) {
        this.papelhasAtividadePK = papelhasAtividadePK;
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

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
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
        hash += (papelhasAtividadePK != null ? papelhasAtividadePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PapelhasAtividade)) {
            return false;
        }
        PapelhasAtividade other = (PapelhasAtividade) object;
        if ((this.papelhasAtividadePK == null && other.papelhasAtividadePK != null) || (this.papelhasAtividadePK != null && !this.papelhasAtividadePK.equals(other.papelhasAtividadePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.PapelhasAtividade[ papelhasAtividadePK=" + papelhasAtividadePK + " ]";
    }
    
}
