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
import java.util.concurrent.TimeUnit;
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
                items = getPagination().createPageDataModel();
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
            if(vl.next().getVerboPK().getNome().toLowerCase().equals(current.getVerboPK().getNome().toLowerCase())){
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
    
    public String destroyAndList() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
            recreateModel();
            return "List";
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
    
    //--------------------- ORDENAR TABELA -------------------------------
    
    public ArrayList<Verbo> DataModel_to_List(DataModel input){
        Iterator<Verbo> temp = input.iterator();
        ArrayList<Verbo> result = new ArrayList<>();
        while(temp.hasNext()){
            result.add(temp.next());
        }
        return result;
    }
    
    public void InsertionSort(Boolean order, char coluna){
        ArrayList<Verbo> arr = DataModel_to_List(items);
        
        int n = arr.size(); 
        for (int i = 1; i < n; ++i) { 
            Verbo key = arr.get(i);
            int j = i - 1;
            while (j >= 0 && comparator(arr.get(j), key, order, coluna)){
                arr.set(j+1, arr.get(j));
                j = j - 1; 
            } 
            arr.set(j+1, key);
        }
        
        items = new ListDataModel(arr);
    
    }
    
    private Boolean comparator(Verbo a, Verbo b, boolean order, char coluna){
        Boolean result = false;
        switch(coluna){
            case 'n':
                result = compareStrings(a.getVerboPK().getNome(), b.getVerboPK().getNome(), order);//compare_suj(a, b, order);
                break;
            case 't':
                result = compareStrings(a.getTipo(), b.getTipo(), order);
                break;
            case 'd':
                result = compareDates(a.getDataCriacao(), b.getDataCriacao(), order); //tenho de ver isto
                break;
                    }
        return result;
    }
    
    private Boolean compareStrings(String a, String b, Boolean order){
        Boolean result = false;
        if(order){
            if(a.toLowerCase().compareTo(b.toLowerCase()) > 0){
                result = true;
            }
        }else{
            if(a.toLowerCase().compareTo(b.toLowerCase()) < 0){
                result = true;
            }
        }
        return result;
    }
    
    private Boolean compareDates(Date a, Date b, Boolean order){
        Boolean result = false;
        if(order){
            if(a.compareTo(b) > 0){
                result = true;
            }
        }else{
            if(a.compareTo(b) < 0){
                result = true;
            }
        }
        return result;
    }    
    
    Boolean order_nome=true, order_tipo=true, order_data=true;
    
    public void ordenarNome(){
        InsertionSort(order_nome, 'n');
        order_nome = !order_nome;
    }
    
    public void ordenarTipo(){
        InsertionSort(order_tipo, 't');
        order_tipo = !order_tipo;
    }
    
    public void ordenarData(){
        InsertionSort(order_data, 'd');
        order_data = !order_data;
    }
    
    //-------------PESQUISA--------------------
    /**
     * pesquisa global
     */
    
    String pesquisa = "";

    public String getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(String pesquisa) {
        this.pesquisa = pesquisa;
    }
    
    
    
    public void pesquisar(){
        
        Iterator<Verbo> temporario = getFacade().findAll().iterator();
        List<Verbo> itens_pesquisa = new ArrayList<>();
        while(temporario.hasNext()){
            Verbo a = temporario.next();
            if(containsIgnoreCase(a.getTipo(), pesquisa)){
                itens_pesquisa.add(a);
            }else if(containsIgnoreCase(a.getVerboPK().getNome(), pesquisa)){
                System.out.println(a.getVerboPK().getNome() + "verbo nome");
                itens_pesquisa.add(a);
            }
        }
        
        items = new ListDataModel(itens_pesquisa);
        
    }
    
    Boolean andB=false;
    String nome="", tipo="";
    int data=0;

    public Boolean getAndB() {
        return andB;
    }

    public void setAndB(Boolean andB) {
        this.andB = andB;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
    
    public void pesquisaAvancada(){
        if(andB){
            pesquisaFiltradaAnd();
        }else{
            pesquisaFiltradaOr();
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    
    /**
     * todos os itens que contenham pelo menos um dos filtros escolhidos
     */
    public void pesquisaFiltradaOr(){
        Iterator<Verbo> temporario = getFacade().findAll().iterator();
        List<Verbo> itens_filtrados = new ArrayList<>();
        
        
        while(temporario.hasNext()){
            Verbo a = temporario.next();
            if(!nome.isEmpty() && containsIgnoreCase(a.getVerboPK().getNome(), nome)){
                itens_filtrados.add(a);
            }else if(tipo != null && containsIgnoreCase(a.getTipo(), tipo)){
                itens_filtrados.add(a);
            }else if(data != 0 && howManyDays(a.getDataCriacao()) <= data){
                itens_filtrados.add(a);
            }
        }
        
        items = new ListDataModel(itens_filtrados);
        
    }
    
    /**
     * ver ha quantos dias passou aquela data
     * @param d data que quero  estudar
     * @return numero de dias passados
     */
    private long howManyDays(Date d){
        Date current_date = new Date();
        long diffInMillies = Math.abs(d.getTime() - current_date.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;        
    }
    
    /**
     * retorna todos os itens que tem todos os filtros
     */
    public void pesquisaFiltradaAnd(){
        Iterator<Verbo> temporario = getFacade().findAll().iterator();
        List<Verbo> itens_filtrados = new ArrayList<>();
        while(temporario.hasNext()){
            Verbo a = temporario.next();
            if((containsIgnoreCase(a.getVerboPK().getNome(), nome) || nome.isEmpty()) && (containsIgnoreCase(a.getTipo(), tipo) ||  tipo.isEmpty()) && (data == 0 || howManyDays(a.getDataCriacao()) <= data)){
                itens_filtrados.add(a);
            }
        }
        
        items = new ListDataModel(itens_filtrados);
        
    }
    
    public void limparFiltros(){
        pesquisa = "";
        nome="";
        tipo ="";
        data=0;
        items = null;
        items = getItems(); 
    }
    
    //-------------------------------------------

}
