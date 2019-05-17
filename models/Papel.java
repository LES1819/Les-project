/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author tiago
 */
@Entity
@Table(name = "Papel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Papel.findAll", query = "SELECT p FROM Papel p"),
    @NamedQuery(name = "Papel.notAssociated", query = "SELECT p FROM Papel p WHERE p NOT IN(SELECT a.papel FROM PapelhasAtividade a)"),
    @NamedQuery(name = "Papel.findByIdPapel", query = "SELECT p FROM Papel p WHERE p.idPapel = :idPapel"),
    @NamedQuery(name = "Papel.findByNome", query = "SELECT p FROM Papel p WHERE p.nome = :nome"),
    @NamedQuery(name = "Papel.destroyAssociatedAtividade", query ="DELETE FROM PapelhasAtividade p WHERE p.papel = :papel"),
    @NamedQuery(name = "Papel.findByDescricao", query = "SELECT p FROM Papel p WHERE p.descricao = :descricao"),
    @NamedQuery(name = "Papel.findByDataCriacao", query = "SELECT p FROM Papel p WHERE p.dataCriacao = :dataCriacao")})
public class Papel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPapel")
    private Integer idPapel;
    @Size(max = 45)
    @Column(name = "nome")
    private String nome;
    @Size(max = 500)
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "papel")
    private Collection<PapelhasAtividade> papelhasAtividadeCollection;

    public Papel() {
    }

    public Papel(Integer idPapel) {
        this.idPapel = idPapel;
    }

    public Integer getIdPapel() {
        return idPapel;
    }

    public void setIdPapel(Integer idPapel) {
        this.idPapel = idPapel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Utilizador getUtilizadoridUtilizador() {
        return utilizadoridUtilizador;
    }

    public void setUtilizadoridUtilizador(Utilizador utilizadoridUtilizador) {
        this.utilizadoridUtilizador = utilizadoridUtilizador;
    }

    @XmlTransient
    public Collection<PapelhasAtividade> getPapelhasAtividadeCollection() {
        return papelhasAtividadeCollection;
    }

    public void setPapelhasAtividadeCollection(Collection<PapelhasAtividade> papelhasAtividadeCollection) {
        this.papelhasAtividadeCollection = papelhasAtividadeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPapel != null ? idPapel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Papel)) {
            return false;
        }
        Papel other = (Papel) object;
        if ((this.idPapel == null && other.idPapel != null) || (this.idPapel != null && !this.idPapel.equals(other.idPapel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Papel[ idPapel=" + idPapel + " ]";
    }
    
}
