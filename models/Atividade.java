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
 * @author andre
 */
@Entity
@Table(name = "Atividade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Atividade.findAll", query = "SELECT a FROM Atividade a"),
    @NamedQuery(name = "Atividade.findByIdAtividades", query = "SELECT a FROM Atividade a WHERE a.idAtividades = :idAtividades"),
    @NamedQuery(name = "Atividade.findOriginal", query = "SELECT a FROM Atividade a WHERE a.processoidProcesso IS NULL AND a.idAtividades NOT IN(SELECT b.idAtividadeOriginal.idAtividades FROM Atividade b WHERE b.processoidProcesso.idProcesso = :param)"),
    @NamedQuery(name = "Atividade.isCopy",query ="SELECT a FROM Atividade a WHERE a.idAtividadeOriginal IS NOT NULL AND a.idAtividades = :atividade"),
    @NamedQuery(name = "Atividade.getCopies",query ="SELECT a FROM Atividade a WHERE a.idAtividadeOriginal = :atividade"),
    @NamedQuery(name = "Atividade.setIdOriginalNull",query ="UPDATE Atividade a SET a.idAtividadeOriginal = NULL WHERE a.idAtividades = :atividade"),
    @NamedQuery(name = "Atividade.destroyAssociatedPapers", query ="DELETE FROM PapelhasAtividade p WHERE p.atividade = :atividade"),
    @NamedQuery(name = "Atividade.destroyAssociatedPatterns", query ="DELETE FROM AtividadehasPadrao p WHERE p.atividade = :atividade"),
    @NamedQuery(name = "Atividade.destroyAssociatedProcesso", query ="DELETE FROM Atividade p WHERE p.processoidProcesso = :processo"),
    @NamedQuery(name = "Atividade.destroyCopias", query ="DELETE FROM Atividade a WHERE a.idAtividadeOriginal = :id"),
    @NamedQuery(name = "Atividade.destroyAssociatedProducts", query ="DELETE FROM ProdutohasAtividade p WHERE p.atividade = :atividade"),
    @NamedQuery(name = "Atividade.findByNome", query = "SELECT a FROM Atividade a WHERE a.nome = :nome"),
    @NamedQuery(name = "Atividade.findByDescricao", query = "SELECT a FROM Atividade a WHERE a.descricao = :descricao"),
    @NamedQuery(name = "Atividade.findByDataCriacao", query = "SELECT a FROM Atividade a WHERE a.dataCriacao = :dataCriacao")})
public class Atividade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAtividades")
    private Integer idAtividades;
    @Size(max = 40)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atividade")
    private Collection<AtividadehasPadrao> atividadehasPadraoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atividade")
    private Collection<ProdutohasAtividade> produtohasAtividadeCollection;
    @JoinColumn(name = "Processo_idProcesso", referencedColumnName = "idProcesso")
    @ManyToOne
    private Processo processoidProcesso;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;
    @OneToMany(mappedBy = "idAtividadeOriginal")
    private Collection<Atividade> atividadeCollection;
    @JoinColumn(name = "idAtividadeOriginal", referencedColumnName = "idAtividades")
    @ManyToOne
    private Atividade idAtividadeOriginal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atividade")
    private Collection<PapelhasAtividade> papelhasAtividadeCollection;

    public Atividade() {
    }

    public Atividade(Integer idAtividades) {
        this.idAtividades = idAtividades;
    }

    public Atividade(Integer idAtividades, String descricao) {
        this.idAtividades = idAtividades;
        this.descricao = descricao;
    }

    public Integer getIdAtividades() {
        return idAtividades;
    }

    public void setIdAtividades(Integer idAtividades) {
        this.idAtividades = idAtividades;
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

    @XmlTransient
    public Collection<AtividadehasPadrao> getAtividadehasPadraoCollection() {
        return atividadehasPadraoCollection;
    }

    public void setAtividadehasPadraoCollection(Collection<AtividadehasPadrao> atividadehasPadraoCollection) {
        this.atividadehasPadraoCollection = atividadehasPadraoCollection;
    }

    @XmlTransient
    public Collection<ProdutohasAtividade> getProdutohasAtividadeCollection() {
        return produtohasAtividadeCollection;
    }

    public void setProdutohasAtividadeCollection(Collection<ProdutohasAtividade> produtohasAtividadeCollection) {
        this.produtohasAtividadeCollection = produtohasAtividadeCollection;
    }

    public Processo getProcessoidProcesso() {
        return processoidProcesso;
    }

    public void setProcessoidProcesso(Processo processoidProcesso) {
        this.processoidProcesso = processoidProcesso;
    }

    public Utilizador getUtilizadoridUtilizador() {
        return utilizadoridUtilizador;
    }

    public void setUtilizadoridUtilizador(Utilizador utilizadoridUtilizador) {
        this.utilizadoridUtilizador = utilizadoridUtilizador;
    }

    @XmlTransient
    public Collection<Atividade> getAtividadeCollection() {
        return atividadeCollection;
    }

    public void setAtividadeCollection(Collection<Atividade> atividadeCollection) {
        this.atividadeCollection = atividadeCollection;
    }

    public Atividade getIdAtividadeOriginal() {
        return idAtividadeOriginal;
    }

    public void setIdAtividadeOriginal(Atividade idAtividadeOriginal) {
        this.idAtividadeOriginal = idAtividadeOriginal;
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
        hash += (idAtividades != null ? idAtividades.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Atividade)) {
            return false;
        }
        Atividade other = (Atividade) object;
        if ((this.idAtividades == null && other.idAtividades != null) || (this.idAtividades != null && !this.idAtividades.equals(other.idAtividades))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
    
}
