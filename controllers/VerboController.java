package jsf;

import jpa.entities.Verbo;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.VerboFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import jpa.entities.Frase;
import static org.apache.taglibs.standard.functions.Functions.containsIgnoreCase;

@Named("verboController")
@SessionScoped
public class VerboController implements Serializable {

    private Verbo current;
    private DataModel items = null;
    @EJB
    private jpa.session.VerboFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private Map<Verbo, Boolean> selectedItems = new HashMap<Verbo, Boolean>();
    private List<Verbo> verbosOnList;
    private DataModel<Frase> frases;

    public DataModel<Frase> getFrases() {
        return frases;
    }

    public void setFrases(DataModel<Frase> frases) {
        this.frases = frases;
    }

    
    
    public Map<Verbo, Boolean> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(Map<Verbo, Boolean> selectedItems) {
        this.selectedItems = selectedItems;
    }
    
    public String destroyVerbos() {
        prepareSelectedList();
        for (int i = 0; i < verbosOnList.size(); i++) {
            destroyVerbo(verbosOnList.get(i));
        }
        selectedItems = new HashMap<>();
        verbosOnList = new ArrayList<>();
        return "List";
    }
    
    public void destroyVerbo(Verbo a) {
        current = a;
        performDestroy();
        recreatePagination();
        recreateModel();
    }
    
    private void prepareSelectedList(){
        verbosOnList = new ArrayList<Verbo>();
        for(Verbo a : selectedItems.keySet()){
            if(selectedItems.get(a) == true){
                verbosOnList.add(a);
            }
        }
        
        prepareSelectedListFrases();
    }
    
    /**
     * para colocar no DataModel frases as frases que contem os verbos de verbosOnList
     */
    public void prepareSelectedListFrases(){
        List<Frase> result = new ArrayList<Frase>();
        for(Verbo a: verbosOnList){
            result.addAll(getFacade().pesquisaVerbos(a.getVerboPK().getIdVerbo()));
        }
        
        frases = new ListDataModel(result);        
    }
    
    public String mostrarFrases(){
        prepareSelectedList();
        return "Message";
    }
    
    public VerboController() {
    }

    public Verbo getSelected() {
        if (current == null) {
            current = new Verbo();
            current.setVerboPK(new jpa.entities.VerboPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private VerboFacade getFacade() {
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
        current = (Verbo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        verbosOnList = new ArrayList<Verbo>();
        verbosOnList.add(current);
        prepareSelectedListFrases(); //completa o datamodel frases para mostrar as frases que contem o verbo na view do verbo
        return "View";
    }

    public String prepareCreate() {
        current = new Verbo();
        current.setVerboPK(new jpa.entities.VerboPK());
        current.setDataCriacao(new Date());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        
        boolean encontrou = procurarIgual();
        if(!encontrou){
            try {
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("VerboCreated"));
                return prepareCreate();
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
                return null;
            }
        }else{
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("VerboErrorName"));
            return prepareCreate();
        }
    }
    
    private boolean procurarIgual(){
        Iterator<Verbo> vl = getFacade().findAll().iterator();
        while(vl.hasNext()){
            if(containsIgnoreCase(vl.next().getVerboPK().getNome(), current.getVerboPK().getNome())){
                return true;
            }
        }
        /*
        for(Verbo v: vl){
            if(containsIgnoreCase(v.getVerboPK().getNome(), current.getVerboPK().getNome())){
                return true;
            }
        }*/
        return false;
    }
    

    public String prepareEdit() {
        current = (Verbo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("VerboUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Verbo) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("VerboDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
        items = getPagination().createPageDataModel();
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

    public Verbo getVerbo(jpa.entities.VerboPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Verbo.class)
    public static class VerboControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VerboController controller = (VerboController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "verboController");
            return controller.getVerbo(getKey(value));
        }

        jpa.entities.VerboPK getKey(String value) {
            jpa.entities.VerboPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.VerboPK();
            key.setIdVerbo(Integer.parseInt(values[0]));
            key.setNome(values[1]);
            return key;
        }

        String getStringKey(jpa.entities.VerboPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdVerbo());
            sb.append(SEPARATOR);
            sb.append(value.getNome());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Verbo) {
                Verbo o = (Verbo) object;
                return getStringKey(o.getVerboPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Verbo.class.getName());
            }
        }

    }

}
