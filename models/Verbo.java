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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author joaohenriques
 */
@Entity
@Table(name = "Verbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Verbo.findAll", query = "SELECT v FROM Verbo v"),
    @NamedQuery(name = "Verbo.findByIdVerbo", query = "SELECT v FROM Verbo v WHERE v.verboPK.idVerbo = :idVerbo"),
    @NamedQuery(name = "Verbo.findByNome", query = "SELECT v FROM Verbo v WHERE v.verboPK.nome = :nome"),
    @NamedQuery(name = "Verbo.findByTipo", query = "SELECT v FROM Verbo v WHERE v.tipo = :tipo"),
    @NamedQuery(name = "Verbo.findByDataCriacao", query = "SELECT v FROM Verbo v WHERE v.dataCriacao = :dataCriacao")})
public class Verbo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VerboPK verboPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "Tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "verbo")
    private Collection<Frase> fraseCollection;

    public Verbo() {
    }

    public Verbo(VerboPK verboPK) {
        this.verboPK = verboPK;
    }

    public Verbo(VerboPK verboPK, String tipo, Date dataCriacao) {
        this.verboPK = verboPK;
        this.tipo = tipo;
        this.dataCriacao = dataCriacao;
    }

    public Verbo(int idVerbo, String nome) {
        this.verboPK = new VerboPK(idVerbo, nome);
    }

    public VerboPK getVerboPK() {
        return verboPK;
    }

    public void setVerboPK(VerboPK verboPK) {
        this.verboPK = verboPK;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @XmlTransient
    public Collection<Frase> getFraseCollection() {
        return fraseCollection;
    }

    public void setFraseCollection(Collection<Frase> fraseCollection) {
        this.fraseCollection = fraseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (verboPK != null ? verboPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Verbo)) {
            return false;
        }
        Verbo other = (Verbo) object;
        if ((this.verboPK == null && other.verboPK != null) || (this.verboPK != null && !this.verboPK.equals(other.verboPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + verboPK;
    }
    
    
}
