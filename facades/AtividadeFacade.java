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

/**
 *
 * @author andre
 */
@Stateless
public class AtividadeFacade extends AbstractFacade<Atividade> {

    @PersistenceContext(unitName = "lesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AtividadeFacade() {
        super(Atividade.class);
    }
    
    public List getOriginal(){
        return em.createNamedQuery("Atividade.findOriginal").getResultList();
    }
    
    public int countOriginal(){
        return getOriginal().size();
    }
    
   public void auxiliarDestroy(Atividade atividade){
        em.createNamedQuery("Atividade.destroyAssociatedPapers").setParameter("atividade",atividade);
        em.createNamedQuery("Atividade.destroyAssociatedPatterns").setParameter("atividade",atividade);
        em.createNamedQuery("Atividade.destroyAssociatedProducts").setParameter("atividade",atividade);
    }
    
    public void destroyAtividade(Atividade atividade){
        List<Atividade> isCopies = em.createNamedQuery("Atividade.isCopy").setParameter("atividade",atividade.getIdAtividades()).getResultList();
        if(isCopies.size() > 0){ // is copy
           // System.out.println("is copy");
           // System.out.println(isCopies);
            auxiliarDestroy(atividade);
        }
        else{ // is original
          //  System.out.println("original");
            auxiliarDestroy(atividade);
            List<Atividade> copies = em.createNamedQuery("Atividade.getCopies").setParameter("atividade", atividade).getResultList();
            for(Atividade copy : copies){
                auxiliarDestroy(copy);
                em.remove(copy);
            }
        }

    }
}
