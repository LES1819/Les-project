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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author joaohenriques
 */
@Entity
@Table(name = "Agrupamento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agrupamento.findAll", query = "SELECT a FROM Agrupamento a"),
    @NamedQuery(name = "Agrupamento.findByIdAgrupamento", query = "SELECT a FROM Agrupamento a WHERE a.idAgrupamento = :idAgrupamento"),
    @NamedQuery(name = "Agrupamento.findByNome", query = "SELECT a FROM Agrupamento a WHERE a.nome = :nome"),
    @NamedQuery(name = "Agrupamento.findByDescricao", query = "SELECT a FROM Agrupamento a WHERE a.descricao = :descricao"),
    @NamedQuery(name = "Agrupamento.findByDataCriacao", query = "SELECT a FROM Agrupamento a WHERE a.dataCriacao = :dataCriacao")})
public class Agrupamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAgrupamento")
    private Integer idAgrupamento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nome")
    private String nome;
    @Size(max = 500)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agrupamento")
    private Collection<AgrupamentohasFrase> agrupamentohasFraseCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agrupamento")
    private Collection<AgrupamentohasPadrao> agrupamentohasPadraoCollection;

    public Agrupamento() {
    }

    public Agrupamento(Integer idAgrupamento) {
        this.idAgrupamento = idAgrupamento;
    }

    public Agrupamento(Integer idAgrupamento, String nome, Date dataCriacao) {
        this.idAgrupamento = idAgrupamento;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
    }

    public Integer getIdAgrupamento() {
        return idAgrupamento;
    }

    public void setIdAgrupamento(Integer idAgrupamento) {
        this.idAgrupamento = idAgrupamento;
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
    public Collection<AgrupamentohasFrase> getAgrupamentohasFraseCollection() {
        return agrupamentohasFraseCollection;
    }

    public void setAgrupamentohasFraseCollection(Collection<AgrupamentohasFrase> agrupamentohasFraseCollection) {
        this.agrupamentohasFraseCollection = agrupamentohasFraseCollection;
    }

    @XmlTransient
    public Collection<AgrupamentohasPadrao> getAgrupamentohasPadraoCollection() {
        return agrupamentohasPadraoCollection;
    }

    public void setAgrupamentohasPadraoCollection(Collection<AgrupamentohasPadrao> agrupamentohasPadraoCollection) {
        this.agrupamentohasPadraoCollection = agrupamentohasPadraoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAgrupamento != null ? idAgrupamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agrupamento)) {
            return false;
        }
        Agrupamento other = (Agrupamento) object;
        if ((this.idAgrupamento == null && other.idAgrupamento != null) || (this.idAgrupamento != null && !this.idAgrupamento.equals(other.idAgrupamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Agrupamento[ idAgrupamento=" + idAgrupamento + " ]";
    }
    
}
