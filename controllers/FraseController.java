package jsf;

import com.sun.xml.internal.ws.util.StringUtils;
import jpa.entities.Frase;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.FraseFacade;

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
import jpa.entities.Agrupamento;
import jpa.entities.AgrupamentohasFrase;
import jpa.entities.AgrupamentohasFrasePK;
import jpa.entities.Artefactos;
import jpa.entities.Sujeito;
import jpa.entities.Utilizador;
import jpa.session.AgrupamentohasFraseFacade;
import static org.apache.taglibs.standard.functions.Functions.containsIgnoreCase;

@Named("fraseController")
@SessionScoped
public class FraseController implements Serializable {

    private Frase current;
    private DataModel items = null;
    private DataModel itemsNotAssociated =  null;
    @EJB
    private jpa.session.FraseFacade ejbFacade;
    @EJB
    private AgrupamentohasFraseFacade agrupamentohasFraseFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    
    
    
    private Map<Frase, Boolean> selectedItems = new HashMap<Frase, Boolean>();
    private int agrupamentoId;
    //associacoes a agrupamentos
    private Agrupamento agrupamento; //isso é para fazer a associação ao agrupamento
    private Utilizador utilizador;
    private List<Frase> frasesOnList;
    private DataModel items_possible = null; //itens que posso associar ao agrupamento
    private Boolean facil = true; //este boolean está mal escrito
    
    //pesquisa
    private String pesquisa, destinatario = "", sujeito="", verbo="";
    private int data = 0;
    private Boolean andB  = false;
    //ordenacao
    boolean order_dest = true, order_data = true, order_sujeito = true, order_verbo = true;
    
    private AgrupamentohasFrase association;

    public DataModel getItems_possible() {
        return items_possible;
    }

    public void setItems_possible(DataModel items_possible) {
        this.items_possible = items_possible;
    }
    
    public Map<Frase, Boolean> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(Map<Frase, Boolean> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Boolean getAndB() {
        return andB;
    }

    public void setAndB(Boolean andB) {
        this.andB = andB;
    }
    
    public String getDestinatario() {
        return destinatario;
    }

    public DataModel getItemsNotAssociated() {
        return itemsNotAssociated;
    }

    public void setItemsNotAssociated(DataModel itemsNotAssociated) {
        this.itemsNotAssociated = itemsNotAssociated;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(String pesquisa) {
        this.pesquisa = pesquisa;
    }

    public String getSujeito() {
        return sujeito;
    }

    public void setSujeito(String sujeito) {
        this.sujeito = sujeito;
    }

    public String getVerbo() {
        return verbo;
    }

    public void setVerbo(String verbo) {
        this.verbo = verbo;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public FraseController() {
        selectedItems = new HashMap<>();
        frasesOnList = new ArrayList<>();
    }

    public Frase getSelected() {
        if (current == null) {
            current = new Frase();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    private void prepareSelectedList(){
        frasesOnList = new ArrayList<Frase>();
        for(Frase a : selectedItems.keySet()){
            if(selectedItems.get(a) == true){
                frasesOnList.add(a);
            }
        }
    }
    
    //----------------- ORDENAR A TABELA------------------------------
    public ArrayList<Frase> DataModel_to_List(DataModel input){
        Iterator<Frase> temp = input.iterator();
        ArrayList<Frase> result = new ArrayList<>();
        while(temp.hasNext()){
            result.add(temp.next());
        }
        return result;
    }
    
    public void InsertionSort(Boolean order, char coluna){
        ArrayList<Frase> arr = DataModel_to_List(items);
        
        int n = arr.size(); 
        for (int i = 1; i < n; ++i) { 
            Frase key = arr.get(i);
            int j = i - 1;
            while (j >= 0 && comparator(arr.get(j), key, order, coluna)){
                arr.set(j+1, arr.get(j));
                j = j - 1; 
            } 
            arr.set(j+1, key);
        }
        
        items = new ListDataModel(arr);
    
    }
    
    private Boolean comparator(Frase a, Frase b, boolean order, char coluna){
        Boolean result = false;
        switch(coluna){
            case 's':
                result = compareStrings(a.getSujeitoidSujeito().getNome(), b.getSujeitoidSujeito().getNome(), order);//compare_suj(a, b, order);
                break;
            case 'd':
                result = compareStrings(a.getDestinatario(), b.getDestinatario(), order);
                break;
            case 'v':
                result = compareStrings(a.getVerbo().getVerboPK().getNome(), b.getVerbo().getVerboPK().getNome(), order);
                break;
            case 'k':
                result = compareDates(a.getDatCriacao(), b.getDatCriacao(), order); //tenho de ver isto
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
    
    public void ordenarSujeito(){
        InsertionSort(order_sujeito, 's');
        order_sujeito = !order_sujeito;
    }
    
    public void ordenarVerbo(){
        InsertionSort(order_verbo, 'v');
        order_verbo = !order_verbo;
    }
    
    public void ordenarData(){
        InsertionSort(order_data, 'k');
        order_data = !order_data;
    }
    
    public void ordenarDestinatario(){
        InsertionSort(order_dest, 'd');
        order_dest = !order_dest;
    }
    
    //-------------PESQUISA--------------------
    /**
     * pesquisa global
     */
    public void pesquisar(){
        
        Iterator<Frase> temporario = getFacade().findAll().iterator();
        List<Frase> itens_pesquisa = new ArrayList<>();
        while(temporario.hasNext()){
            Frase a = temporario.next();
            if(containsIgnoreCase(a.getDestinatario(), pesquisa)){
                itens_pesquisa.add(a);
            }else if(containsIgnoreCase(a.getSujeitoidSujeito().getNome(), pesquisa)){
                itens_pesquisa.add(a);
            }else if(containsIgnoreCase(a.getVerbo().getVerboPK().getNome(), pesquisa)){
                itens_pesquisa.add(a);
            }else if(containsIgnoreCase(a.getDataRealizacao(), pesquisa)){
                itens_pesquisa.add(a);
            }
        }
        
        items = new ListDataModel(itens_pesquisa);
        
    }
    
    public void pesquisaAvancada(){
        if(andB){
            pesquisaFiltradaAnd();
        }else{
            pesquisaFiltradaOr();
        }
    }
    
    /**
     * todos os itens que contenham pelo menos um dos filtros escolhidos
     */
    public void pesquisaFiltradaOr(){
        Iterator<Frase> temporario = getFacade().findAll().iterator();
        List<Frase> itens_filtrados = new ArrayList<>();
        
        
        while(temporario.hasNext()){
            Frase a = temporario.next();
            if(!destinatario.isEmpty()  && containsIgnoreCase(a.getDestinatario(), destinatario)){
                itens_filtrados.add(a);
            }else if(!sujeito.isEmpty() && containsIgnoreCase(a.getSujeitoidSujeito().getNome(), sujeito)){
                itens_filtrados.add(a);
            }else if(!verbo.isEmpty() && containsIgnoreCase(a.getVerbo().getVerboPK().getNome(), verbo)){
                itens_filtrados.add(a);
            }else if(data != 0 && howManyDays(a.getDatCriacao()) <= data){
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
        Iterator<Frase> temporario = getFacade().findAll().iterator();
        List<Frase> itens_filtrados = new ArrayList<>();
        while(temporario.hasNext()){
            Frase a = temporario.next();
            if((containsIgnoreCase(a.getDestinatario(), destinatario) || destinatario.isEmpty()) && (containsIgnoreCase(a.getSujeitoidSujeito().getNome(), sujeito) ||  sujeito.isEmpty()) && (containsIgnoreCase(a.getVerbo().getVerboPK().getNome(), verbo) || verbo.isEmpty()) && (data == 0 || howManyDays(a.getDatCriacao()) <= data)){
                itens_filtrados.add(a);
            }
        }
        
        items = new ListDataModel(itens_filtrados);
        
    }
    
    public void limparFiltros(){
        pesquisa = "";
        items = null;
        destinatario  = "";
        sujeito = "";
        verbo = "";
        data = 0;
        andB = false;
        items = getItems(); 
    }
    
    //-------------------------------------------

    private FraseFacade getFacade() {
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
        current = (Frase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Frase();
        current.setIdFrase(0);
        current.setDatCriacao(new Date());
        current.setDataRealizacao(""+new Date());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            items = getPagination().createPageDataModel();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("FraseCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Frase) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("FraseUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public void destroyFrase(Frase a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    public String destroyFrases() {
        prepareSelectedList();
        for (int i = 0; i < frasesOnList.size(); i++) {
            destroyFrase(frasesOnList.get(i));
        }
        selectedItems = new HashMap<>();
        frasesOnList = new ArrayList<>();
        return "List";
    }

    public String destroy() {
        current = (Frase) getItems().getRowData();
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
    
    private void performDestroyFull() {
        try {
            //getFacade().destroyFrase(current);
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("FraseDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("FraseDeleted"));
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

    public Frase getFrase(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Frase.class)
    public static class FraseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FraseController controller = (FraseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fraseController");
            return controller.getFrase(getKey(value));
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
            if (object instanceof Frase) {
                Frase o = (Frase) object;
                return getStringKey(o.getIdFrase());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Frase.class.getName());
            }

             }

       }
    /*
    public String associateSelectedList(){
        prepareSelectedList();
        for(Frase a : frasesOnList){
            //prepareAssociate(agrupamento.getIdAgrupamento());
            AgrupamentohasFrase k = new AgrupamentohasFrase();
            AgrupamentohasFrasePK pk = new AgrupamentohasFrasePK();
            pk.setAgrupamentoidAgrupamento(agrupamentoId);
            pk.setFraseidFrase(a.getIdFrase());
            k.setAgrupamentohasFrasePK(pk);
            AgrupamentohasFraseController agr = new AgrupamentohasFraseController();
            agr.setCurrent(k);
            agr.create();
            //FinalAssociate(a);
            //associate();
        }
        selectedItems = new HashMap<>();
        frasesOnList = new ArrayList<>();
        recreatePagination();
        recreateModel();
        return "/agrupamento/View";
    }
    */
    
    
       
    
    private void prepareItemsPossible(int id_agrup){
        List<AgrupamentohasFrase> associadas = getFacade().pesquisaFrasesAssociadas(id_agrup);
        List<Frase> todas = getFacade().findAll();
        List<Frase> result = new ArrayList<Frase>();
        for(Frase a: todas){
            result.add(a);
            for(AgrupamentohasFrase k: associadas){
                if(a.getIdFrase() == k.getFrase().getIdFrase()){
                    result.remove(result.size()-1);
                    break;
                }
            }
        }
        items_possible = new ListDataModel(result);        
    }
    
    public String prepareAssociate(Agrupamento agrup){
        current = new Frase();
        selectedItemIndex= -1;
        agrupamento = agrup;
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(1);
        current.setUtilizadoridUtilizador(utilizador);
        prepareItemsPossible(agrup.getIdAgrupamento());
            return "AssociarFrases";
        
        
    }
    
    public String associateAllFrases() {
        prepareSelectedList();
        for(int i = 0; i < frasesOnList.size(); i++) {
            associate(frasesOnList.get(i));
        }
        selectedItems = new HashMap<>();
        frasesOnList = new ArrayList<>();
        return "List";
    }
    
    public void associate(Frase f) {
        current = f;
        
        association = new AgrupamentohasFrase();
        
        AgrupamentohasFrasePK pk = new AgrupamentohasFrasePK(agrupamento.getIdAgrupamento(), current.getIdFrase());
        Date date = new Date();
        association.setAgrupamento(agrupamento);
        association.setFrase(current);
        association.setUtilizadoridUtilizador(current.getUtilizadoridUtilizador());
        association.setAgrupamentohasFrasePK(pk);
        association.setDataCriacao(date);
        agrupamentohasFraseFacade.create(association);
        recreateModel();
        recreatePagination();
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("fraseAssociated"));
    }
    
    public ListDataModel<Frase> getFrases(){
        return new ListDataModel(getFacade().findAll());
    }
    
    public String associate() {
        recreatePagination();
        recreateModel();
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Frase Associada");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
}
