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
import jpa.entities.Papel;

/**
 *
 * @author tiago
 */
@Stateless
public class PapelFacade extends AbstractFacade<Papel> {

    @PersistenceContext(unitName = "LESPROJECTO2.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PapelFacade() {
        super(Papel.class);
    }
    
    public List getNotAssociated() {
        return em.createNamedQuery("Papel.notAssociated").getResultList();
    }
    
    public int countNotAssociate() {
        return getNotAssociated().size();
    }
    
}