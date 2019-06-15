package jsf;

import jpa.entities.AgrupamentohasFrase;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.AgrupamentohasFraseFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import jpa.entities.Agrupamento;
import jpa.entities.Frase;
import jpa.session.FraseFacade;

@Named("agrupamentohasFraseController")
@SessionScoped
public class AgrupamentohasFraseController implements Serializable {

    private AgrupamentohasFrase current;
    private DataModel items = null;
    @EJB
    private jpa.session.AgrupamentohasFraseFacade ejbFacade;
    @EJB
    private jpa.session.FraseFacade ejbFraseFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private Agrupamento agrup;

    public AgrupamentohasFraseController() {
    }

    public AgrupamentohasFrase getCurrent() {
        return current;
    }

    public void setCurrent(AgrupamentohasFrase current) {
        this.current = current;
    }
    
    

    public AgrupamentohasFrase getSelected() {
        if (current == null) {
            current = new AgrupamentohasFrase();
            current.setAgrupamentohasFrasePK(new jpa.entities.AgrupamentohasFrasePK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private AgrupamentohasFraseFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        ejbFraseFacade = new jpa.session.FraseFacade();
        
        Frase f = new Frase();
        ListDataModel<Frase> a = new ListDataModel<>(ejbFraseFacade.findAll());
        items = a;
        return "List";
    }

    public String prepareView() {
        current = (AgrupamentohasFrase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public String prepareAssociate(Agrupamento ag){
        agrup = ag;
        return "AssociarFrases";
    }
    
    private ArrayList<Frase> prepareSelectedListFrase(HashMap<Frase, Boolean> frases_selecionadas){
        ArrayList<Frase> frasesOnList = new ArrayList<Frase>();
        for(Frase a : frases_selecionadas.keySet()){
            if(frases_selecionadas.get(a) == true){
                frasesOnList.add(a);
            }
        }
        return frasesOnList;
    }
    
    public String prepareCreateMult(HashMap<Frase, Boolean> frases_selecionadas){
        ArrayList<Frase> frasesOnList = prepareSelectedListFrase(frases_selecionadas);
        
        for(Frase a: frasesOnList){
            prepareCreateFraseAgrup(a);
            create();
        }
        
        return "/agrupamentohasFrase/List";
    }
    /*
    public String prepareCreateMult(DataModel<Frase> frases_selecionadas){
        Iterator<Frase> frases = frases_selecionadas.iterator();
        while(frases.hasNext()){
            Frase a = frases.next();
            prepareCreateFraseAgrup(a);
            create();
        }
        return "/Agrupamento/List";
    }*/
    
    public void prepareCreateFraseAgrup(Frase a) {
        current = new AgrupamentohasFrase();
        current.setDataCriacao(new Date());
        current.setAgrupamentohasFrasePK(new jpa.entities.AgrupamentohasFrasePK());
        selectedItemIndex = -1;
        current.setAgrupamento(agrup);
        current.setFrase(a);
        current.setUtilizadoridUtilizador(agrup.getUtilizadoridUtilizador());
    }

    public String prepareCreate() {
        current = new AgrupamentohasFrase();
        current.setDataCriacao(new Date());
        current.setAgrupamentohasFrasePK(new jpa.entities.AgrupamentohasFrasePK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getAgrupamentohasFrasePK().setFraseidFrase(current.getFrase().getIdFrase());
            current.getAgrupamentohasFrasePK().setAgrupamentoidAgrupamento(current.getAgrupamento().getIdAgrupamento());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentohasFraseCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (AgrupamentohasFrase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getAgrupamentohasFrasePK().setFraseidFrase(current.getFrase().getIdFrase());
            current.getAgrupamentohasFrasePK().setAgrupamentoidAgrupamento(current.getAgrupamento().getIdAgrupamento());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentohasFraseUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (AgrupamentohasFrase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentohasFraseDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public AgrupamentohasFrase getAgrupamentohasFrase(jpa.entities.AgrupamentohasFrasePK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = AgrupamentohasFrase.class)
    public static class AgrupamentohasFraseControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AgrupamentohasFraseController controller = (AgrupamentohasFraseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "agrupamentohasFraseController");
            return controller.getAgrupamentohasFrase(getKey(value));
        }

        jpa.entities.AgrupamentohasFrasePK getKey(String value) {
            jpa.entities.AgrupamentohasFrasePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.AgrupamentohasFrasePK();
            key.setAgrupamentoidAgrupamento(Integer.parseInt(values[0]));
            key.setFraseidFrase(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(jpa.entities.AgrupamentohasFrasePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getAgrupamentoidAgrupamento());
            sb.append(SEPARATOR);
            sb.append(value.getFraseidFrase());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AgrupamentohasFrase) {
                AgrupamentohasFrase o = (AgrupamentohasFrase) object;
                return getStringKey(o.getAgrupamentohasFrasePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + AgrupamentohasFrase.class.getName());
            }
        }

    }

}
