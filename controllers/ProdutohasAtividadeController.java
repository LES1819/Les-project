package jsf;

import jpa.entities.ProdutohasAtividade;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.ProdutohasAtividadeFacade;

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

@Named("produtohasAtividadeController")
@SessionScoped
public class ProdutohasAtividadeController implements Serializable {

    private ProdutohasAtividade current;
    private DataModel items = null;
    @EJB
    private jpa.session.ProdutohasAtividadeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ProdutohasAtividadeController() {
    }
    
   
    
    public PaginationHelper getProductsPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().countAssociatedProducts(atividade);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getAssociatedProducts(atividade));
                }
            };
        }
        return pagination;
    }

    public ProdutohasAtividade getSelected() {
        if (current == null) {
            current = new ProdutohasAtividade();
            current.setProdutohasAtividadePK(new jpa.entities.ProdutohasAtividadePK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProdutohasAtividadeFacade getFacade() {
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
        current = (ProdutohasAtividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new ProdutohasAtividade();
        current.setProdutohasAtividadePK(new jpa.entities.ProdutohasAtividadePK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getProdutohasAtividadePK().setAtividadeidAtividades(current.getAtividade().getIdAtividades());
            current.getProdutohasAtividadePK().setProdutoidProduto(current.getProduto().getIdProduto());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutohasAtividadeCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (ProdutohasAtividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getProdutohasAtividadePK().setAtividadeidAtividades(current.getAtividade().getIdAtividades());
            current.getProdutohasAtividadePK().setProdutoidProduto(current.getProduto().getIdProduto());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutohasAtividadeUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (ProdutohasAtividade) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutohasAtividadeDeleted"));
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

    public ProdutohasAtividade getProdutohasAtividade(jpa.entities.ProdutohasAtividadePK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = ProdutohasAtividade.class)
    public static class ProdutohasAtividadeControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProdutohasAtividadeController controller = (ProdutohasAtividadeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "produtohasAtividadeController");
            return controller.getProdutohasAtividade(getKey(value));
        }

        jpa.entities.ProdutohasAtividadePK getKey(String value) {
            jpa.entities.ProdutohasAtividadePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.ProdutohasAtividadePK();
            key.setProdutoidProduto(Integer.parseInt(values[0]));
            key.setAtividadeidAtividades(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(jpa.entities.ProdutohasAtividadePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getProdutoidProduto());
            sb.append(SEPARATOR);
            sb.append(value.getAtividadeidAtividades());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ProdutohasAtividade) {
                ProdutohasAtividade o = (ProdutohasAtividade) object;
                return getStringKey(o.getProdutohasAtividadePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ProdutohasAtividade.class.getName());
            }
        }

    }
    
    // start of our code
     private Atividade atividade;
    
     public DataModel getAssociatedProducts(Atividade atividadeArg){
        atividade = atividadeArg;
        items = getProductsPagination().createPageDataModel();
        return items;
    }

}
