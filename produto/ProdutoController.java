package jsf;

import jpa.entities.Produto;
import jpa.entities.Atividade;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.ProdutoFacade;

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
import jpa.entities.ProdutohasAtividade;
import jpa.entities.ProdutohasAtividadePK;
import jpa.entities.Utilizador;
import jpa.session.ProdutohasAtividadeFacade;

@Named("produtoController")
@SessionScoped
public class ProdutoController implements Serializable {

    @EJB
    private ProdutohasAtividadeFacade produtohasAtividadeFacade;
    private Produto current;
    private DataModel items = null;
    @EJB
    private jpa.session.ProdutoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    // map for selected stuff on the JSF page
    private Map<Produto, Boolean> selectedItems;
    // products to be associated
    private List<Produto> productsOnList;

    public ProdutoController() {
        // map for selected stuff on the JSF page
        selectedItems = new HashMap<>();
        productsOnList = new ArrayList<>();
    }

    private void prepareSelectedList() {
        productsOnList = new ArrayList<>();
        for(Produto p : selectedItems.keySet()) {
            if (selectedItems.get(p) == true) {
                productsOnList.add(p);
            }
        }
    }
    
    //get HashMap
    public Map<Produto, Boolean> getSelectedItems() {
        return selectedItems;
    }

    public Produto getSelected() {
        if (current == null) {
            current = new Produto();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProdutoFacade getFacade() {
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

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Produto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Produto();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String associateAllProducts() {
        prepareSelectedList();
        for(int i = 0; i < productsOnList.size(); i++) {
            associate(productsOnList.get(i));
        }
        selectedItems = new HashMap<>();
        return "associateProduct";
    }
    
    public void associate(Produto p) {
        current = p;
        Atividade nova = new Atividade(10);
        Utilizador autor = new Utilizador(1);
        ProdutohasAtividade association = new ProdutohasAtividade();
        ProdutohasAtividadePK pk = new ProdutohasAtividadePK(current.getIdProduto(), nova.getIdAtividades());
        Date date = new Date();
        association.setAtividade(nova);
        association.setProduto(current);
        association.setUtilizadoridUtilizador(autor);
        association.setProdutohasAtividadePK(pk);
        association.setDataCriacao(date);
        produtohasAtividadeFacade.create(association);
        update();
    }
   
    /*public String associate() {
        current = (Produto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();

        Atividade nova = new Atividade(428);
        current.setAtividadeidAtividades(nova);
        update();

        Collection<Produto> temp = nova.getProdutoCollection();
        temp.add(current);
        nova.setProdutoCollection(temp);
        return "associateProduct";
    }*/

    public String prepareAssociate() {
        recreatePagination();
        recreateModel();
        return "/atividade/associateProduct";
    }

    public String prepareListOfProducts() {
        recreatePagination();
        recreateModel();
        return "/produto/List";
    }

    /*
    public String prepareAssociate(){
        current = new Atividade();
        selectedItemIndex= -1;
        processo = new Processo();
        processo.setIdProcesso(31);
        current.setProcessoidProcesso(processo);
        return "/atividade/Associate";
    }*/
    public String prepareEdit() {
        current = (Produto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        recreatePagination();
        recreateModel();
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Produto) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoDeleted"));
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

    public DataModel getItemsNotAssociated() {
        items = getPaginationNotAssociated().createPageDataModel();
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

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Produto getProduto(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Produto.class)
    public static class ProdutoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProdutoController controller = (ProdutoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "produtoController");
            return controller.getProduto(getKey(value));
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
            if (object instanceof Produto) {
                Produto o = (Produto) object;
                return getStringKey(o.getIdProduto());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Produto.class.getName());
            }
        }

    }
}
