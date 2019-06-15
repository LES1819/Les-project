/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.entities.Frase;

/**
 *
 * @author joaohenriques
 */
@Stateless
public class FraseFacade extends AbstractFacade<Frase> {

    @PersistenceContext(unitName = "FinalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FraseFacade() {
        super(Frase.class);
    }

    public List sortByDestinatario() {
        return em.createNamedQuery("Frase.sortByDestinatario").getResultList();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List sortByDestinatarioDesc() {
        return em.createNamedQuery("Frase.sortByDestinatarioDesc").getResultList();
    }
    
    public List sortBySujeito() {
        return em.createNamedQuery("Frase.sortBySujeito").getResultList();
    }

    public List sortBySujeitoDesc() {
        return em.createNamedQuery("Frase.sortBySujeitoDesc").getResultList();
    }
    
    public List sortByData() {
        return em.createNamedQuery("Frase.sortByData").getResultList();
    }

    public List sortByDataDesc() {
        return em.createNamedQuery("Frase.sortByDataDesc").getResultList();
    }
    
    public List sortByVerbo() {
        return em.createNamedQuery("Frase.sortByVerbo").getResultList();
    }

    public List sortByVerboDesc() {
        return em.createNamedQuery("Frase.sortByVerboDesc").getResultList();
    }
    
    
    public List pesquisaStringDest(String s) {
        return em.createNamedQuery("Frase.pesquisaStringDest").setParameter("dest", s).getResultList();
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List pesquisaFrasesAssociadas(int id_agrup){
        return em.createNamedQuery("AgrupamentohasFrase.findByAgrupamentoidAgrupamento").setParameter("agrupamentoidAgrupamento", id_agrup).getResultList();
    }
    
}
