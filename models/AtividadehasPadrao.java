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
@Table(name = "Atividade_has_Padrao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtividadehasPadrao.findAll", query = "SELECT a FROM AtividadehasPadrao a"),
    @NamedQuery(name = "AtividadehasPadrao.findByAtividadeidAtividades", query = "SELECT a FROM AtividadehasPadrao a WHERE a.atividadehasPadraoPK.atividadeidAtividades = :atividadeidAtividades"),
    @NamedQuery(name = "AtividadehasPadrao.findByPadraoidPadr\u00e3o", query = "SELECT a FROM AtividadehasPadrao a WHERE a.atividadehasPadraoPK.padraoidPadr\u00e3o = :padraoidPadr\u00e3o"),
    @NamedQuery(name = "AtividadehasPadrao.associatedPatterns", query = "SELECT p FROM AtividadehasPadrao p WHERE p.atividade = :a"),
    @NamedQuery(name = "AtividadehasPadrao.findByIdAssociacao", query = "SELECT a FROM AtividadehasPadrao a WHERE a.idAssociacao = :idAssociacao"),
    @NamedQuery(name = "AtividadehasPadrao.findByDataCriacao", query = "SELECT a FROM AtividadehasPadrao a WHERE a.dataCriacao = :dataCriacao")})
public class AtividadehasPadrao implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AtividadehasPadraoPK atividadehasPadraoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Associacao")
    private int idAssociacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @JoinColumn(name = "Atividade_idAtividades", referencedColumnName = "idAtividades", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Atividade atividade;
    @JoinColumn(name = "Padrao_idPadr\u00e3o", referencedColumnName = "idPadr\u00e3o", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Padrao padrao;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;

    public AtividadehasPadrao() {
    }

    public AtividadehasPadrao(AtividadehasPadraoPK atividadehasPadraoPK) {
        this.atividadehasPadraoPK = atividadehasPadraoPK;
    }

    public AtividadehasPadrao(AtividadehasPadraoPK atividadehasPadraoPK, int idAssociacao, Date dataCriacao) {
        this.atividadehasPadraoPK = atividadehasPadraoPK;
        this.idAssociacao = idAssociacao;
        this.dataCriacao = dataCriacao;
    }

    public AtividadehasPadrao(int atividadeidAtividades, int padraoidPadrão) {
        this.atividadehasPadraoPK = new AtividadehasPadraoPK(atividadeidAtividades, padraoidPadrão);
    }

    public AtividadehasPadraoPK getAtividadehasPadraoPK() {
        return atividadehasPadraoPK;
    }

    public void setAtividadehasPadraoPK(AtividadehasPadraoPK atividadehasPadraoPK) {
        this.atividadehasPadraoPK = atividadehasPadraoPK;
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

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public Padrao getPadrao() {
        return padrao;
    }

    public void setPadrao(Padrao padrao) {
        this.padrao = padrao;
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
        hash += (atividadehasPadraoPK != null ? atividadehasPadraoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtividadehasPadrao)) {
            return false;
        }
        AtividadehasPadrao other = (AtividadehasPadrao) object;
        if ((this.atividadehasPadraoPK == null && other.atividadehasPadraoPK != null) || (this.atividadehasPadraoPK != null && !this.atividadehasPadraoPK.equals(other.atividadehasPadraoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.AtividadehasPadrao[ atividadehasPadraoPK=" + atividadehasPadraoPK + " ]";
    }
    
}
