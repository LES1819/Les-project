/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entities.Agrupamento;

/**
 *
 * @author joaohenriques
 */
@Stateless
public class AgrupamentoFacade extends AbstractFacade<Agrupamento> {

    @PersistenceContext(unitName = "FinalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgrupamentoFacade() {
        super(Agrupamento.class);
    }
    
}
