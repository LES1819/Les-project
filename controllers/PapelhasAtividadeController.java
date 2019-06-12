package jsf;

import jpa.entities.PapelhasAtividade;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.PapelhasAtividadeFacade;

import java.io.Serializable;
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

@Named("papelhasAtividadeController")
@SessionScoped
public class PapelhasAtividadeController implements Serializable {

    private PapelhasAtividade current;
    private DataModel items = null;
    private Atividade atividade;
    @EJB
    private jpa.session.PapelhasAtividadeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public PapelhasAtividadeController() {
    }

    
    
    public PapelhasAtividade getSelected() {
        if (current == null) {
            current = new PapelhasAtividade();
            current.setPapelhasAtividadePK(new jpa.entities.PapelhasAtividadePK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private PapelhasAtividadeFacade getFacade() {
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
        current = (PapelhasAtividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new PapelhasAtividade();
        current.setPapelhasAtividadePK(new jpa.entities.PapelhasAtividadePK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getPapelhasAtividadePK().setPapelidPapel(current.getPapel().getIdPapel());
            current.getPapelhasAtividadePK().setAtividadeidAtividades(current.getAtividade().getIdAtividades());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelhasAtividadeCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (PapelhasAtividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getPapelhasAtividadePK().setPapelidPapel(current.getPapel().getIdPapel());
            current.getPapelhasAtividadePK().setAtividadeidAtividades(current.getAtividade().getIdAtividades());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelhasAtividadeUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (PapelhasAtividade) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelhasAtividadeDeleted"));
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

    public PapelhasAtividade getPapelhasAtividade(jpa.entities.PapelhasAtividadePK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = PapelhasAtividade.class)
    public static class PapelhasAtividadeControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PapelhasAtividadeController controller = (PapelhasAtividadeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "papelhasAtividadeController");
            return controller.getPapelhasAtividade(getKey(value));
        }

        jpa.entities.PapelhasAtividadePK getKey(String value) {
            jpa.entities.PapelhasAtividadePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.PapelhasAtividadePK();
            key.setPapelidPapel(Integer.parseInt(values[0]));
            key.setAtividadeidAtividades(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(jpa.entities.PapelhasAtividadePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getPapelidPapel());
            sb.append(SEPARATOR);
            sb.append(value.getAtividadeidAtividades());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PapelhasAtividade) {
                PapelhasAtividade o = (PapelhasAtividade) object;
                return getStringKey(o.getPapelhasAtividadePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PapelhasAtividade.class.getName());
            }
        }
    
     }
        // begins our code
        
        public DataModel getAssociatedPapers(Atividade atividadeArg){
        atividade = atividadeArg;
        items = getPapersPagination().createPageDataModel();
        return items;
    }
    
    public PaginationHelper getPapersPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().countAssociatedPapers(atividade);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getAssociatedPapers(atividade));
                }
            };
        }
        return pagination;
    }

    

}
