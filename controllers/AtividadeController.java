package jsf;

import jpa.entities.Atividade;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.AtividadeFacade;

import java.io.Serializable;
import java.util.ArrayList;
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

@Named("atividadeController")
@SessionScoped
public class AtividadeController implements Serializable {

    private Atividade current;
    private DataModel items = null;
    private Map<Atividade, Boolean> selectedItems;
    private List<Atividade> activitiesOnList;
    @EJB
    private jpa.session.AtividadeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public AtividadeController() {
              selectedItems = new HashMap<>();
        activitiesOnList = new ArrayList<>();
    }
    
            public void prepareSelectedList() {
        activitiesOnList = new ArrayList<>();
        for(Atividade a : selectedItems.keySet()) {
            if (selectedItems.get(a) == true) {
                activitiesOnList.add(a);
            }
        }
    }

    public Map<Atividade, Boolean> getSelectedItems() {
        return selectedItems;
    }
    
    public DataModel getOriginalItems() {
        if (items == null) {
            items = getOriginalPagination().createPageDataModel();
        }
        return items;
    }
    
    public PaginationHelper getOriginalPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().countOriginal();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getOriginal());
                }

            };
        }
        return pagination;
    }
    
    public void destroyAtividade(Atividade a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    public String destroyActivities() {
        prepareSelectedList();
        for (int i = 0; i < activitiesOnList.size(); i++) {
            destroyAtividade(activitiesOnList.get(i));
        }
        return "List";
    }

    public Atividade getSelected() {
        if (current == null) {
            current = new Atividade();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AtividadeFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(5) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()-1}));
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
        current = (Atividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Atividade();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Atividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Atividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndList() {
        performDestroyFull();
        recreateModel();
        updateCurrentItem();
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

    private void performDestroyFull() {
        try {
            getFacade().destroyAtividade(current);
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeDeleted"));
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

    public Atividade getAtividade(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Atividade.class)
    public static class AtividadeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AtividadeController controller = (AtividadeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "atividadeController");
            return controller.getAtividade(getKey(value));
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
            if (object instanceof Atividade) {
                Atividade o = (Atividade) object;
                return getStringKey(o.getIdAtividades());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Atividade.class.getName());
            }
        }

    }

}
