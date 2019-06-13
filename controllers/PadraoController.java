package jsf;

import jpa.entities.Padrao;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.PadraoFacade;

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
import jpa.entities.AtividadehasPadrao;
import jpa.entities.AtividadehasPadraoPK;
import jpa.entities.Utilizador;
import jpa.session.AtividadehasPadraoFacade;

@Named("padraoController")
@SessionScoped
public class PadraoController implements Serializable {

    private Padrao current;
    private DataModel items = null;
    @EJB
    private jpa.session.PadraoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;


    public PadraoController() {
    }

    public Padrao getSelected() {
        if (current == null) {
            current = new Padrao();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PadraoFacade getFacade() {
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
        current = (Padrao) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Padrao();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PadraoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Padrao) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PadraoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Padrao) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PadraoDeleted"));
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

    public Padrao getPadrao(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Padrao.class)
    public static class PadraoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PadraoController controller = (PadraoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "padraoController");
            return controller.getPadrao(getKey(value));
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
            if (object instanceof Padrao) {
                Padrao o = (Padrao) object;
                return getStringKey(o.getIdPadrao());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Padrao.class.getName());
            }
        }

    }
    
    
    /*eu*/
    
    private Atividade atividade;
    private Utilizador utilizador;
    private AtividadehasPadrao association;
    @EJB
    private AtividadehasPadraoFacade atividadehasPadraoFacade;
    private Map<Padrao, Boolean> selectedItems;
    private List<Padrao> patternsOnList;
    public String prepareAssociate(int ativi){
        current = new Padrao();
        selectedItemIndex= -1;
        atividade = new Atividade();
        atividade.setIdAtividades(ativi);
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(1);
        current.setUtilizadoridUtilizador(utilizador);
        selectedItems = new HashMap<Padrao,Boolean>();
        return "/atividade/associatePadrao";
    }
    
    public void associate(Padrao p) {
        current.setIdPadrao(p.getIdPadrao());
        current.setDescricao(p.getDescricao());
        current.setNome(p.getNome());
        current.setDataCriacao(p.getDataCriacao());
        current.setImg(p.getImg());
        
        association = new AtividadehasPadrao();
        
        AtividadehasPadraoPK pk = new AtividadehasPadraoPK(atividade.getIdAtividades(), current.getIdPadrao());
        association.setAtividade(atividade);
        association.setPadrao(current);
        association.setUtilizadoridUtilizador(current.getUtilizadoridUtilizador());
        association.setAtividadehasPadraoPK(pk);
        association.setDataCriacao(new Date());
        atividadehasPadraoFacade.create(association);
        recreateModel();
        recreatePagination();
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PadraoAssociated"));
    }
    
    
     public DataModel getItemsNotAssociated() {
        items = getPaginationNotAssociated().createPageDataModel();
        return items;
    }
     
     public PaginationHelper getPaginationNotAssociated() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().countNotAssociate(atividade);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getNotAssociated(atividade));
                }
            };
        }
        return pagination;
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
    
     public String prepareListOfPatterns() {
        recreatePagination();
        recreateModel();
        return "/papel/List";
    }
     
     public String associateAllPatterns() {
        prepareSelectedList();
        for(int i = 0; i < patternsOnList.size(); i++) {
            associate(patternsOnList.get(i));
        }
        selectedItems = new HashMap<>();
        patternsOnList = new ArrayList<>();
        return "associatePadrao";
    }
     
     public Map<Padrao, Boolean> getSelectedItems() {
        return selectedItems;
    }
     
     private void prepareSelectedList() {
        patternsOnList = new ArrayList<Padrao>();
        System.out.println(selectedItems);
        for(Padrao p : selectedItems.keySet()) {
            if (selectedItems.get(p) == true) {
                patternsOnList.add(p);
            }
        }
    }

}
