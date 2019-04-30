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
import jpa.entities.PapelhasAtividade;

/**
 *
 * @author andre
 */
@Stateless
public class PapelhasAtividadeFacade extends AbstractFacade<PapelhasAtividade> {

    @PersistenceContext(unitName = "lesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PapelhasAtividadeFacade() {
        super(PapelhasAtividade.class);
    }
        
    public List getAssociatedPapers(Atividade atividade){
        return em.createNamedQuery("PapelhasAtividade.associatedPapers").setParameter("atividade",atividade).getResultList();
    }
    
    public int countAssociatedPapers(Atividade atividade) {
        return getAssociatedPapers(atividade).size();
    }
    
}
