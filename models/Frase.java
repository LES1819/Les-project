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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "Frase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Frase.findAll", query = "SELECT f FROM Frase f"),
    @NamedQuery(name = "Frase.findByIdFrase", query = "SELECT f FROM Frase f WHERE f.idFrase = :idFrase"),
    @NamedQuery(name = "Frase.findByDestinatario", query = "SELECT f FROM Frase f WHERE f.destinatario = :destinatario"),
    @NamedQuery(name = "Frase.findByVerboId", query = "SELECT f FROM Frase f WHERE f.verbo.verboPK.idVerbo = :verbo_id"),
    //@NamedQuery(name = "Frase.findByArtefacto", query = "SELECT f FROM Frase f WHERE f.artefacto = :artefacto"),
    @NamedQuery(name = "Frase.findByRecurso", query = "SELECT f FROM Frase f WHERE f.recurso = :recurso"),
    
    @NamedQuery(name = "Frase.sortByDestinatario", query = "SELECT f FROM Frase f ORDER BY f.destinatario"),
    @NamedQuery(name = "Frase.sortByDestinatarioDesc", query = "SELECT f FROM Frase f ORDER BY f.destinatario DESC"),
    @NamedQuery(name = "Frase.sortByData", query = "SELECT f FROM Frase f ORDER BY f.datCriacao"),
    @NamedQuery(name = "Frase.sortByDataDesc", query = "SELECT f FROM Frase f ORDER BY f.datCriacao DESC"),
    //@NamedQuery(name = "Frase.sortBySujeito", query = "SELECT f FROM Frase f AND Sujeito s INNER JOIN s WHERE f.Sujeito_idSujeito = s.idSujeito ORDER BY s.nome"),
    //@NamedQuery(name = "Frase.sortBySujeitoDesc", query = "SELECT f FROM Frase f INNER JOIN Sujeito ORDER BY Sujeito.nome DESC"),
    //@NamedQuery(name = "Frase.sortByVerbo", query = "SELECT f FROM Frase f INNER JOIN Sujeito ORDER BY Sujeito.nome"),
    //@NamedQuery(name = "Frase.sortByVerboDesc", query = "SELECT f FROM Frase f INNER JOIN Sujeito ORDER BY Sujeito.nome DESC"),
    
    @NamedQuery(name = "Frase.pesquisaStringDest", query = "SELECT f FROM Frase f WHERE f.destinatario = :dest"),
    @NamedQuery(name = "Frase.findByDataRealizacao", query = "SELECT f FROM Frase f WHERE f.dataRealizacao = :dataRealizacao"),
    @NamedQuery(name = "Frase.findByDatCriacao", query = "SELECT f FROM Frase f WHERE f.datCriacao = :datCriacao")})
public class Frase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFrase")
    private Integer idFrase;
    @Size(max = 45)
    @Column(name = "Destinatario")
    private String destinatario;
    /*@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "artefacto")
    private String artefacto;*/
    @Size(max = 45)
    @Column(name = "recurso")
    private String recurso;
    @Size(max = 45)
    @Column(name = "dataRealizacao")
    private String dataRealizacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datCriacao;
    @JoinColumn(name = "Artefactos_idArtefactos", referencedColumnName = "idArtefactos")
    @ManyToOne
    private Artefactos artefactosidArtefactos;
    @JoinColumn(name = "Sujeito_idSujeito", referencedColumnName = "idSujeito")
    @ManyToOne(optional = false)
    private Sujeito sujeitoidSujeito;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;
    @JoinColumns({
        @JoinColumn(name = "Verbo_idVerbo", referencedColumnName = "idVerbo"),
        @JoinColumn(name = "Verbo_nome", referencedColumnName = "nome")})
    @ManyToOne(optional = false)
    private Verbo verbo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "frase")
    private Collection<AgrupamentohasFrase> agrupamentohasFraseCollection;
    //private Agrupamento agrupamentoidAgrupamento;

    public Frase() {
    }

    public Frase(Integer idFrase) {
        this.idFrase = idFrase;
    }

    public Frase(Integer idFrase, Date datCriacao) {
        this.idFrase = idFrase;
        this.datCriacao = datCriacao;
    }

    public Integer getIdFrase() {
        return idFrase;
    }

    public void setIdFrase(Integer idFrase) {
        this.idFrase = idFrase;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public String getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(String dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public Date getDatCriacao() {
        return datCriacao;
    }

    public void setDatCriacao(Date datCriacao) {
        this.datCriacao = datCriacao;
    }

    public Artefactos getArtefactosidArtefactos() {
        return artefactosidArtefactos;
    }

    public void setArtefactosidArtefactos(Artefactos artefactosidArtefactos) {
        this.artefactosidArtefactos = artefactosidArtefactos;
    }

    public Sujeito getSujeitoidSujeito() {
        return sujeitoidSujeito;
    }

    public void setSujeitoidSujeito(Sujeito sujeitoidSujeito) {
        this.sujeitoidSujeito = sujeitoidSujeito;
    }

    public Utilizador getUtilizadoridUtilizador() {
        return utilizadoridUtilizador;
    }

    public void setUtilizadoridUtilizador(Utilizador utilizadoridUtilizador) {
        this.utilizadoridUtilizador = utilizadoridUtilizador;
    }

    public Verbo getVerbo() {
        return verbo;
    }
   

    public void setVerbo(Verbo verbo) {
        this.verbo = verbo;
    }

    @XmlTransient
    public Collection<AgrupamentohasFrase> getAgrupamentohasFraseCollection() {
        return agrupamentohasFraseCollection;
    }

    public void setAgrupamentohasFraseCollection(Collection<AgrupamentohasFrase> agrupamentohasFraseCollection) {
        this.agrupamentohasFraseCollection = agrupamentohasFraseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFrase != null ? idFrase.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Frase)) {
            return false;
        }
        Frase other = (Frase) object;
        if ((this.idFrase == null && other.idFrase != null) || (this.idFrase != null && !this.idFrase.equals(other.idFrase))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Frase[ idFrase=" + idFrase + " ]";
    }
    /*
    public void setAgrupamentoIdAgrupamento(Agrupamento agrupamentoidAgrupamento){
        this.agrupamentoidAgrupamento = agrupamentoidAgrupamento;
    }*/
    
}
