/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author andre
 */
@Embeddable
public class PapelhasAtividadePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Papel_idPapel")
    private int papelidPapel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Atividade_idAtividades")
    private int atividadeidAtividades;

    public PapelhasAtividadePK() {
    }

    public PapelhasAtividadePK(int papelidPapel, int atividadeidAtividades) {
        this.papelidPapel = papelidPapel;
        this.atividadeidAtividades = atividadeidAtividades;
    }

    public int getPapelidPapel() {
        return papelidPapel;
    }

    public void setPapelidPapel(int papelidPapel) {
        this.papelidPapel = papelidPapel;
    }

    public int getAtividadeidAtividades() {
        return atividadeidAtividades;
    }

    public void setAtividadeidAtividades(int atividadeidAtividades) {
        this.atividadeidAtividades = atividadeidAtividades;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) papelidPapel;
        hash += (int) atividadeidAtividades;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PapelhasAtividadePK)) {
            return false;
        }
        PapelhasAtividadePK other = (PapelhasAtividadePK) object;
        if (this.papelidPapel != other.papelidPapel) {
            return false;
        }
        if (this.atividadeidAtividades != other.atividadeidAtividades) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.PapelhasAtividadePK[ papelidPapel=" + papelidPapel + ", atividadeidAtividades=" + atividadeidAtividades + " ]";
    }
    
}
