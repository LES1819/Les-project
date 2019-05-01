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
import jpa.entities.Processo;

/**
 *
 * @author diogo
 */
@Stateless
public class ProcessoFacade extends AbstractFacade<Processo> {

    @PersistenceContext(unitName = "lesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcessoFacade() {
        super(Processo.class);
    }
    
      public List getOriginal(){
        return em.createNamedQuery("Processo.findOriginal").getResultList();
    }
    
    public int countOriginal(){
        return getOriginal().size();
    }

    public void destroyProcesso(Processo processo){
        em.createNamedQuery("Processo.destroyAssociatedActivities").setParameter("processo",processo);     
    }
}
