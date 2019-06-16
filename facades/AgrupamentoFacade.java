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
import jpa.entities.Agrupamento;
import jpa.entities.AgrupamentohasFrase;

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
    
    public List pesquisaFrase(int id) {
        return em.createNamedQuery("AgrupamentohasFrase.findByAgrupamentoidAgrupamento").setParameter("agrupamentoidAgrupamento", id).getResultList();
    }
    public List pesquisaPadrao(int id) {
        return em.createNamedQuery("AgrupamentohasPadrao.findByAgrupamentoidAgrupamento").setParameter("agrupamentoidAgrupamento", id).getResultList();
    }
    
    public List findByIdFraseIdAgrup(int idFrase, int idAgrup){
        return em.createNamedQuery("AgrupamentohasFrase.findByIdFraseIdAgrup").setParameter("idAgrup", idAgrup).setParameter("idFrase", idFrase).getResultList();
    }
    public List findByIdPadraoIdAgrup(int idPadrao, int idAgrup){
        return em.createNamedQuery("AgrupamentohasPadrao.findByIdPadraoIdAgrup").setParameter("idAgrup", idAgrup).setParameter("idPadrao", idPadrao).getResultList();
    }
    
}
