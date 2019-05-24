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
import jpa.entities.Padrao;

/**
 *
 * @author andre
 */
@Stateless
public class PadraoFacade extends AbstractFacade<Padrao> {

    @PersistenceContext(unitName = "les_testePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PadraoFacade() {
        super(Padrao.class);
    }
    
    /*eu*/
    
    public List getNotAssociated(Atividade a) {
        return em.createNamedQuery("Padrao.notAssociated").setParameter("atividade",a).getResultList();
    }
    
    public int countNotAssociate(Atividade a) {
        return getNotAssociated(a).size();
    }
}
