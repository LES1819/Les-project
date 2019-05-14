package jsf;

import jpa.entities.Atividade;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.AtividadeFacade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import jpa.entities.Processo;
import jpa.entities.Utilizador;

@Named("atividadeController")
@SessionScoped
public class AtividadeController implements Serializable {

    private Atividade current;
    private DataModel items = null;
    @EJB
    private jpa.session.AtividadeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Utilizador utilizador;
    private Processo processo;
    
    private Map<Atividade, Boolean> selectedItems = new HashMap<Atividade, Boolean>();
    private List<Atividade> atividadesOnList;

    public AtividadeController() {
        selectedItems = new HashMap<>();
        atividadesOnList = new ArrayList<>();
    }

    public Atividade getSelected() {
        if (current == null) {
            current = new Atividade();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    private void prepareSelectedList(){
        atividadesOnList = new ArrayList<Atividade>();
        for(Atividade a : selectedItems.keySet()){
            if(selectedItems.get(a) == true){
                atividadesOnList.add(a);
            }
        }
    }
    
    public Map<Atividade, Boolean> getSelectedItems(){
        return selectedItems;
    }

    private AtividadeFacade getFacade() {
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
    
    public PaginationHelper getOriginalPagination(int pid) {
        if (pagination == null) {
            pagination = new PaginationHelper(6) {

                @Override
                public int getItemsCount() {
                    return getFacade().countOriginal(pid);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getOriginal(pid));
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
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(1);
        current.setUtilizadoridUtilizador(utilizador);
        return "Create";
    }
    

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources/Bundle").getString("AtividadeCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Atividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public void associateSelectedList(){
        prepareSelectedList();
        for(Atividade a : atividadesOnList){
            FinalAssociate(a);
        }
    }
        
     public String prepareAssociate(){
        current = new Atividade();
        selectedItemIndex= -1;
        processo = new Processo();
        processo.setIdProcesso(31);
        current.setProcessoidProcesso(processo);
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(1);
        current.setUtilizadoridUtilizador(utilizador);
        return "/atividade/Associate";
    }
    
    public String FinalAssociate(Atividade a){        
        current.setIdAtividades(0);
        current.setNome(a.getNome());
        current.setDescricao(a.getDescricao());
        current.setDataCriacao(new Date(System.currentTimeMillis()));
        current.setIdAtividadeOriginal(a);
        associate();
        recreatePagination();
        recreateModel();
        return "Associate";
    }
    
    public String associate() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Atividade Associada");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources/Bundle").getString("AtividadeUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void destroyAtividade(Atividade a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    public String destroyActivities() {
        prepareSelectedList();
        for (int i = 0; i < ativitidadesOnList.size(); i++) {
            destroyAtividade(ativitidadesOnList.get(i));
        }
        return "List";
    }
    
    public String destroy() {
        current = (Atividade) getItems().getRowData();
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
    
        public String destroyAndList() {
        performDestroyFull();
        recreateModel();
        updateCurrentItem();
        recreateModel();
        return "List";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources/Bundle").getString("AtividadeDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources/Bundle").getString("PersistenceErrorOccured"));
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
    
    public DataModel getOriginalItems(int pid){
        recreatePagination();
        recreateModel();
        if(items == null) {
            items = getOriginalPagination(pid).createPageDataModel();
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
    
    public String nextOriginal(int pid) {
        getOriginalPagination(pid).nextPage();
        recreateModel();
        return "List";
    }

    public String previousOriginal(int pid) {
        getOriginalPagination(pid).previousPage();
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
