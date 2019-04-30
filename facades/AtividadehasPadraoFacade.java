/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entities.Atividade;
import jpa.entities.AtividadehasPadrao;

/**
 *
 * @author andre
 */
@Stateless
public class AtividadehasPadraoFacade extends AbstractFacade<AtividadehasPadrao> {

    @PersistenceContext(unitName = "lesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AtividadehasPadraoFacade() {
        super(AtividadehasPadrao.class);
    }
    
    public List getAssociatedPatterns(Atividade atividade){
        return em.createNamedQuery("AtividadehasPadrao.associatedPatterns").setParameter("a",atividade).getResultList();
    }
    
    public int countAssociatedPatterns(Atividade atividade) {
        return getAssociatedPatterns(atividade).size();
    }
    
}
