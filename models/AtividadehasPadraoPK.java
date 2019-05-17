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
public class AtividadehasPadraoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Atividade_idAtividades")
    private int atividadeidAtividades;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Padrao_idPadr\u00e3o")
    private int padraoidPadrão;

    public AtividadehasPadraoPK() {
    }

    public AtividadehasPadraoPK(int atividadeidAtividades, int padraoidPadrão) {
        this.atividadeidAtividades = atividadeidAtividades;
        this.padraoidPadrão = padraoidPadrão;
    }

    public int getAtividadeidAtividades() {
        return atividadeidAtividades;
    }

    public void setAtividadeidAtividades(int atividadeidAtividades) {
        this.atividadeidAtividades = atividadeidAtividades;
    }

    public int getPadraoidPadrão() {
        return padraoidPadrão;
    }

    public void setPadraoidPadrão(int padraoidPadrão) {
        this.padraoidPadrão = padraoidPadrão;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) atividadeidAtividades;
        hash += (int) padraoidPadrão;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtividadehasPadraoPK)) {
            return false;
        }
        AtividadehasPadraoPK other = (AtividadehasPadraoPK) object;
        if (this.atividadeidAtividades != other.atividadeidAtividades) {
            return false;
        }
        if (this.padraoidPadrão != other.padraoidPadrão) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.AtividadehasPadraoPK[ atividadeidAtividades=" + atividadeidAtividades + ", padraoidPadr\u00e3o=" + padraoidPadrão + " ]";
    }
    
}
