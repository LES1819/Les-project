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
import jpa.entities.Verbo;

/**
 *
 * @author joaohenriques
 */
@Stateless
public class VerboFacade extends AbstractFacade<Verbo> {

    @PersistenceContext(unitName = "FinalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VerboFacade() {
        super(Verbo.class);
    }
    
    public List pesquisaVerbos(int id) {
        return em.createNamedQuery("Frase.findByVerboId").setParameter("verbo_id", id).getResultList();
    }
    
}
