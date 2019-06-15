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
import jpa.entities.AgrupamentohasFrase;

/**
 *
 * @author joaohenriques
 */
@Stateless
public class AgrupamentohasFraseFacade extends AbstractFacade<AgrupamentohasFrase> {

    @PersistenceContext(unitName = "FinalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgrupamentohasFraseFacade() {
        super(AgrupamentohasFrase.class);
    }
    
    /**
     * ve se tem alguma linha na tabela com aquele id de frase e de agrupamento
     * @param idFrase
     * @param idAgrup
     * @return true se sim, false se nao
     */
    public List findIdFraseIdAgrup2(int idFrase, int idAgrup) {
       
        
        //List result = 
                return em.createNamedQuery("AgrupamentohasFrase.findByFraseidFrase").setParameter("fraseidFrase", idFrase).getResultList();
        /*if(!result.isEmpty()){
            return true;
        }
        else{
            return false;
        }*/
    }
    
    public List findIdFraseIdAgrup(){
        return em.createNamedQuery("AgrupamentohasFrase.findAll").getResultList();
    }
    
}
