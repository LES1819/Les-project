package jsf;

import jpa.entities.AtividadehasPadrao;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.AtividadehasPadraoFacade;

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

@Named("atividadehasPadraoController")
@SessionScoped
public class AtividadehasPadraoController implements Serializable {

    private AtividadehasPadrao current;
    private DataModel items = null;
    private Atividade atividade;
    @EJB
    private jpa.session.AtividadehasPadraoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public AtividadehasPadraoController() {
    }

    public AtividadehasPadrao getSelected() {
        if (current == null) {
            current = new AtividadehasPadrao();
            current.setAtividadehasPadraoPK(new jpa.entities.AtividadehasPadraoPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private AtividadehasPadraoFacade getFacade() {
        return ejbFacade;
    }
    
    public DataModel getAssociatedPatterns(Atividade atividadeArg){
        atividade = atividadeArg;
        items = getPatternsPagination().createPageDataModel();
        return items;
    }
    
     public PaginationHelper getPatternsPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().countAssociatedPatterns(atividade);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getAssociatedPatterns(atividade));
                }
            };
        }
        return pagination;
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
        current = (AtividadehasPadrao) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new AtividadehasPadrao();
        current.setAtividadehasPadraoPK(new jpa.entities.AtividadehasPadraoPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getAtividadehasPadraoPK().setPadraoidPadrão(current.getPadrao().getIdPadrão());
            current.getAtividadehasPadraoPK().setAtividadeidAtividades(current.getAtividade().getIdAtividades());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadehasPadraoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (AtividadehasPadrao) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getAtividadehasPadraoPK().setPadraoidPadrão(current.getPadrao().getIdPadrão());
            current.getAtividadehasPadraoPK().setAtividadeidAtividades(current.getAtividade().getIdAtividades());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadehasPadraoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (AtividadehasPadrao) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadehasPadraoDeleted"));
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

    public AtividadehasPadrao getAtividadehasPadrao(jpa.entities.AtividadehasPadraoPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = AtividadehasPadrao.class)
    public static class AtividadehasPadraoControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AtividadehasPadraoController controller = (AtividadehasPadraoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "atividadehasPadraoController");
            return controller.getAtividadehasPadrao(getKey(value));
        }

        jpa.entities.AtividadehasPadraoPK getKey(String value) {
            jpa.entities.AtividadehasPadraoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.AtividadehasPadraoPK();
            key.setAtividadeidAtividades(Integer.parseInt(values[0]));
            key.setPadraoidPadrão(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(jpa.entities.AtividadehasPadraoPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getAtividadeidAtividades());
            sb.append(SEPARATOR);
            sb.append(value.getPadraoidPadrão());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AtividadehasPadrao) {
                AtividadehasPadrao o = (AtividadehasPadrao) object;
                return getStringKey(o.getAtividadehasPadraoPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + AtividadehasPadrao.class.getName());
            }
        }

    }

}
