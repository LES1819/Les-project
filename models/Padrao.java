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
import javax.persistence.Lob;
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
 * @author andre
 */
@Entity
@Table(name = "Padrao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Padrao.findAll", query = "SELECT p FROM Padrao p"),
    @NamedQuery(name = "Padrao.findByIdPadr\u00e3o", query = "SELECT p FROM Padrao p WHERE p.idPadr\u00e3o = :idPadr\u00e3o"),
    @NamedQuery(name = "Padrao.findByNome", query = "SELECT p FROM Padrao p WHERE p.nome = :nome"),
    @NamedQuery(name = "Padrao.findByDescricao", query = "SELECT p FROM Padrao p WHERE p.descricao = :descricao"),
    @NamedQuery(name = "Padrao.findByDataCriacao", query = "SELECT p FROM Padrao p WHERE p.dataCriacao = :dataCriacao")})
public class Padrao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPadr\u00e3o")
    private Integer idPadrão;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nome")
    private String nome;
    @Lob
    @Column(name = "Img")
    private byte[] img;
    @Size(max = 500)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "padrao")
    private Collection<AtividadehasPadrao> atividadehasPadraoCollection;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "padrao")
    private Collection<AgrupamentohasPadrao> agrupamentohasPadraoCollection;

    public Padrao() {
    }

    public Padrao(Integer idPadrão) {
        this.idPadrão = idPadrão;
    }

    public Padrao(Integer idPadrão, String nome, Date dataCriacao) {
        this.idPadrão = idPadrão;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
    }

    public Integer getIdPadrão() {
        return idPadrão;
    }

    public void setIdPadrão(Integer idPadrão) {
        this.idPadrão = idPadrão;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
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

    @XmlTransient
    public Collection<AtividadehasPadrao> getAtividadehasPadraoCollection() {
        return atividadehasPadraoCollection;
    }

    public void setAtividadehasPadraoCollection(Collection<AtividadehasPadrao> atividadehasPadraoCollection) {
        this.atividadehasPadraoCollection = atividadehasPadraoCollection;
    }

    public Utilizador getUtilizadoridUtilizador() {
        return utilizadoridUtilizador;
    }

    public void setUtilizadoridUtilizador(Utilizador utilizadoridUtilizador) {
        this.utilizadoridUtilizador = utilizadoridUtilizador;
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
        hash += (idPadrão != null ? idPadrão.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Padrao)) {
            return false;
        }
        Padrao other = (Padrao) object;
        if ((this.idPadrão == null && other.idPadrão != null) || (this.idPadrão != null && !this.idPadrão.equals(other.idPadrão))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Padrao[ idPadr\u00e3o=" + idPadrão + " ]";
    }
    
}
