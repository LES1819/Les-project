package jsf;

import jpa.entities.Papel;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.PapelFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import jpa.entities.Atividade;
import jpa.entities.PapelhasAtividade;
import jpa.entities.PapelhasAtividadePK;
import jpa.entities.Utilizador;
import jpa.session.PapelhasAtividadeFacade;

@Named("papelController")
@SessionScoped
public class PapelController implements Serializable {
    @EJB
    private PapelhasAtividadeFacade papelhasAtividadeFacade;
    private Papel current;
    private DataModel items = null;
    @EJB
    private jpa.session.PapelFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
     // map for selected stuff on the JSF page
    private Map<Papel, Boolean> selectedItems;
    // products to be associated
    private List<Papel> papersOnList;
    
    public PapelController() {
        // map for selected stuff on the JSF page
        selectedItems = new HashMap<>();
        papersOnList = new ArrayList<>();
    }
    
     private void prepareSelectedList() {
        papersOnList = new ArrayList<>();
        for(Papel p : selectedItems.keySet()) {
            if (selectedItems.get(p) == true) {
                papersOnList.add(p);
            }
        }
    }
     
     //get HashMap
    public Map<Papel, Boolean> getSelectedItems() {
        return selectedItems;
    }
    
    public PaginationHelper getPaginationNotAssociated() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().countNotAssociate();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getNotAssociated());
                }
            };
        }
        return pagination;
    }
    
    public String associateAllPapers() {
        prepareSelectedList();
        for(int i = 0; i < papersOnList.size(); i++) {
            associate(papersOnList.get(i));
        }
        selectedItems = new HashMap<>();
        return "associatePapel";
    }
    
    public void associate(Papel p) {
        current = p;
        Atividade nova = new Atividade(10);
        Utilizador autor = new Utilizador(1);
        PapelhasAtividade association = new PapelhasAtividade();
        PapelhasAtividadePK pk = new PapelhasAtividadePK(current.getIdPapel(), nova.getIdAtividades());
        Date date = new Date();
        association.setAtividade(nova);
        association.setPapel(current);
        association.setUtilizadoridUtilizador(autor);
        association.setPapelhasAtividadePK(pk);
        association.setDataCriacao(date);
        papelhasAtividadeFacade.create(association);
        update();
    }
    
    public String prepareAssociate() {
        recreatePagination();
        recreateModel();
        return "/atividade/associatePapel";
    }
    
    public String prepareListOfPapers() {
        recreatePagination();
        recreateModel();
        return "/papel/List";
    }

    public Papel getSelected() {
        if (current == null) {
            current = new Papel();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PapelFacade getFacade() {
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
        return "List";
    }

    public String prepareView() {
        current = (Papel) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Papel();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String myCreate() {
        Utilizador user = new Utilizador(1);
        Papel p = new Papel();
        p.setUtilizadoridUtilizador(user);
        p.setNome(current.getNome());
        p.setDescricao(current.getDescricao());
        current = p;
        create();
        return "Create";
    }
    
    public String myUpdate() {
        Papel p = new Papel(46);
        Utilizador user = new Utilizador(1);
        p.setNome(current.getNome());
        p.setDescricao(current.getDescricao());
        p.setUtilizadoridUtilizador(user);
        current = p;
        update();
        return "Edit";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Papel) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    


    public String update() {
        recreatePagination();
        recreateModel();
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String nextNotAssociated() {
        getPaginationNotAssociated().nextPage();
        recreateModel();
        return "List";
    }

    public String previousNotAssociated() {
        getPaginationNotAssociated().previousPage();
        recreateModel();
        return "List";
    }
    
    public DataModel getItemsNotAssociated() {
        items = getPaginationNotAssociated().createPageDataModel();
        return items;
    }

    public String destroy() {
        current = (Papel) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelDeleted"));
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

    public Papel getPapel(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Papel.class)
    public static class PapelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PapelController controller = (PapelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "papelController");
            return controller.getPapel(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Papel) {
                Papel o = (Papel) object;
                return getStringKey(o.getIdPapel());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Papel.class.getName());
            }
        }

    }

}
