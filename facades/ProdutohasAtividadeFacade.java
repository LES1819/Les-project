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
import jpa.entities.ProdutohasAtividade;

/**
 *
 * @author andre
 */
@Stateless
public class ProdutohasAtividadeFacade extends AbstractFacade<ProdutohasAtividade> {

    @PersistenceContext(unitName = "lesPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProdutohasAtividadeFacade() {
        super(ProdutohasAtividade.class);
    }
    
    public List getAssociatedProducts(Atividade atividade){
        return em.createNamedQuery("ProdutohasAtividade.associatedProducts").setParameter("atividade",atividade).getResultList();
    }
    
    public int countAssociatedProducts(Atividade atividade) {
        return getAssociatedProducts(atividade).size();
    }
    
}
