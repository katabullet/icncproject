package at.icnc.om.backingbeans;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblSettlement;
import at.icnc.om.interfaces.EntityListerLocal;
import at.icnc.om.interfaces.Refreshable;
import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of invoiceLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class InvoiceBackingBean implements Refreshable{
	
	// Session Bean of EntityLister to Read and Write Entities
	@EJB
	EntityListerLocal entityLister;

	// Variable to save selected Invoice
	private TblInvoice curInvoice = new TblInvoice();
	
	// List with all Invoices to avoid constant DB-Reading
	private ArrayList<TblInvoice> invoiceList;
	
	// DataPaginator for auto pagination and resetting to first page
	private DataPaginator paginator;
	
	// Variable to set Delete- and Edit-Button visible 
	private Boolean visible = false;
	
	// Variable to make Edit-Popup visible and rendered
	private Boolean popupRender = false;
	
	// Variable to make Filter-Popup visible and rendered
	private Boolean filterpopupRender = false;
	
	// Variable to make Delete-Popup visible and rendered
	private Boolean deletePopupRender = false;
	
	// List with all invoicestates 
	ArrayList<TblInvoicestate> invoicestates;
	
	// Variable to save selected invoicestate
	private TblInvoicestate curInvoicestate;
	
	/*
	 * ______________________________________________________________________________
	 * EntityLister functions
	 */
	
	/**
	 * This functions returns a list with all invoices
	 * @return invoiceList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblInvoice> getInvoiceList(){		
		
		/* Invoices are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (invoiceList == null) {
			invoiceList = new ArrayList<TblInvoice>();
			invoiceList.addAll((ArrayList<TblInvoice>) 
					entityLister.getObjectList(TblInvoice.class));
		}
		
		/* Makes the user know which invoice is selected (it is shaded) */
		if (curInvoice != null) {
			for(TblInvoice curItem : invoiceList) {				
				if(curItem.getIdInvoice() == curInvoice.getIdInvoice()) {
					curItem.setSelected(true);
				}
			}
		}
		
		return invoiceList;
	}	
	
	/**
	 * This function returns a list with all invoicestates
	 * @return invoicestateList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblInvoicestate> getInvoicestateList(){
		
		/* Invoices are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(invoicestates == null){
			invoicestates = new ArrayList<TblInvoicestate>();
			invoicestates.addAll((Collection<? extends TblInvoicestate>) 
					entityLister.getObjectList(TblInvoicestate.class));
		}
		
		/* If no invoicestate is selected, curInvoicestate is set to selected
		 * otherwise the invoicestate is unselected
		 */		
		if(getCurInvoicestate() != null){
			for(TblInvoicestate curItem : invoicestates){				
				if(curItem.getIdInvoicestate() == getCurInvoicestate().getIdInvoicestate()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurInvoicestate(invoicestates.get(0));
		}		
	
		return invoicestates;
	}	

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	public void rowEvent(RowSelectorEvent re) {		
		
		/* If no invoices is selected, curInvoice is set to selected
		 * otherwise the invoice is unselected
		 */
		if(getCurInvoice() != null){			
			if(getCurInvoice().getIdInvoice() == invoiceList.get(re.getRow()).getIdInvoice()){
				setCurInvoice(new TblInvoice());
				setVisible(false);
			}else {
				setCurInvoice(invoiceList.get(re.getRow()));
				setVisible(true);
			}
		}
	}
	
	/* Getter for Delete- and Edit-Button visible */
	public Boolean getVisible(){		
		return visible;
	}
	
	/* Setter for Delete- and Edit-Button visible */
	private void setVisible(Boolean visible){
		this.visible = visible;
	}

	/* Setter for currently selected invoice */
	public void setCurInvoice(TblInvoice curInvoice) {
		this.curInvoice = curInvoice;
	}
	
	/* Getter for currently selected invoice */
	public TblInvoice getCurInvoice() {
		return curInvoice;
	}	
	
	/**
	 * Function to create SelectItems of all Invoicestates
	 * Important for combobox (needs SelectItem, not objects of invoicestates)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getInvoicestateListDescription(){
		ArrayList<SelectItem> invoicsestates = new ArrayList<SelectItem>();
		for (TblInvoicestate item : getInvoicestateList()) {
			/* Description of each invoicestate is added to SelectItem-List */
			invoicsestates.add(new SelectItem(item.getDescriptionIs()));
		}
		return invoicsestates;			
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curInvoicestate is set to the selected one
	 * @param vce
	 */
	public void changeInvoicestate(ValueChangeEvent vce){
		setCurInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM OMinvoicestate WHERE description_is = '" + 
				vce.getNewValue().toString() + "'", TblInvoicestate.class));				
	}

	/* Setter of curInvoicestate */
	public void setCurInvoicestate(TblInvoicestate curInvoicestate) {
		this.curInvoicestate = curInvoicestate;
	}

	/* Getter of curInvoicestate */
	public TblInvoicestate getCurInvoicestate() {
		return curInvoicestate;
	}
	
	
	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curInvoice
	 */
	@Override
	public void init() {
		refresh();
		setCurInvoice(new TblInvoice());
		getCurInvoice().setIdInvoice(0);
		visible = false;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
		paginator.gotoFirstPage();
	}

	/**
	 * Lists with DB-content are set NULL to
	 * make sure they are read from DB
	 */
	@Override
	public void refresh() {
		invoiceList = null;
		invoicestates = null;	
	}

	/* Setter of Paginator */
	public void setPaginator(DataPaginator paginator) {
		this.paginator = paginator;
	}
	
	/* Getter of paginator */
	public DataPaginator getPaginator() {
		return paginator;
	}
	
	/**
	 * All Popups can be closed via this method
	 */
	public void closePopup(){
		init();
	}
	
	//___________________________________________________________________________
	//Filterpopup Methoden

	private String invoicenumber;
	private String sum;
	private String estimatate;
	private Date duedate;
	private String invoicestate;
	private String ordernumber;
	private String incometype;
	private String settlementnumber;
	private Format format = new SimpleDateFormat("dd.MM.yyyy");
	
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public String getInvoicenumber() {
		return invoicenumber;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getSum() {
		return sum;
	}

	public void setEstimatate(String estimatate) {
		this.estimatate = estimatate;
	}

	public String getEstimatate() {
		return estimatate;
	}

	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}

	public Date getDuedate() {
		return duedate;
	}

	public void setInvoicestate(String invoicestate) {
		this.invoicestate = invoicestate;
	}

	public String getInvoicestate() {
		return invoicestate;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setIncometype(String incometype) {
		this.incometype = incometype;
	}

	public String getIncometype() {
		return incometype;
	}

	public void setSettlementnumber(String settlementnumber) {
		this.settlementnumber = settlementnumber;
	}

	public String getSettlementnumber() {
		return settlementnumber;
	}
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getinvoicestateListDescriptionFilter(){
		ArrayList<SelectItem> invoicestate = new ArrayList<SelectItem>();
	
		ArrayList<TblInvoicestate> iState = new ArrayList<TblInvoicestate>();
		iState.addAll((ArrayList<TblInvoicestate>)
				entityLister.getObjectList(TblInvoicestate.class));
		
		invoicestate.add(new SelectItem());
		for (TblInvoicestate item : iState) {
			invoicestate.add(new SelectItem(item.getDescriptionIs()));
		}
		return invoicestate;			
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getorderListDescription(){
		ArrayList<SelectItem> ordernumber = new ArrayList<SelectItem>();
	
		ArrayList<TblOrder> order = new ArrayList<TblOrder>();
		order.addAll((ArrayList<TblOrder>)
				entityLister.getObjectList(TblOrder.class));
		
		ordernumber.add(new SelectItem());
		for (TblOrder item : order) {
			ordernumber.add(new SelectItem(item.getOrdernumber()));
		}
		return ordernumber;			
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getincometypeListDescription(){
		ArrayList<SelectItem> incometypeDescription = new ArrayList<SelectItem>();
	
		ArrayList<TblIncometype> incometype = new ArrayList<TblIncometype>();
		incometype.addAll((Collection<? extends TblIncometype>) 
				entityLister.getObjectList(TblIncometype.class));
		
		incometypeDescription.add(new SelectItem());
		for (TblIncometype item : incometype) {
			incometypeDescription.add(new SelectItem(item.getDescriptionIt()));
		}
		return incometypeDescription;			
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getsettlementListDescription(){
		ArrayList<SelectItem> settlementID = new ArrayList<SelectItem>();
	
		ArrayList<TblSettlement> settlement = new ArrayList<TblSettlement>();
		settlement.addAll((Collection<? extends TblSettlement>) 
				entityLister.getObjectList(TblSettlement.class));
		
		settlementID.add(new SelectItem());
		for (TblSettlement item : settlement) {
			settlementID.add(new SelectItem(item.getIdSettlement()));
		}
		return settlementID;			
	}
	
	public void InvoicestateChangeFilter(ValueChangeEvent vce){
		setInvoicestate(vce.getNewValue().toString());
		//setInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM OMinvoicestate WHERE description_is = '" + vce.getNewValue().toString() + "'", TblInvoicestate.class));					
	}
	
	public void OrderChangeFilter(ValueChangeEvent vce){
		setOrdernumber(vce.getNewValue().toString());	
		//setOrdernumber((TblOrder) entityLister.getSingleObject("SELECT * FROM OMorder WHERE ordernumber = '" + vce.getNewValue().toString() + "'", TblOrder.class));
	}
	
	public void IncometypeChangeFilter(ValueChangeEvent vce){
		setIncometype(vce.getNewValue().toString());	
		//setIncometype((TblIncometype) entityLister.getSingleObject("SELECT * FROM omIncometype WHERE description_it = '" + vce.getNewValue().toString() + "'", TblIncometype.class));
	}
	
	public void SettlementChangeFilter(ValueChangeEvent vce){
		setSettlementnumber(vce.getNewValue().toString());
		//setSettlementnumber((TblSettlement) entityLister.getSingleObject("SELECT * FROM OMsettlement WHERE id_settlement = '" + vce.getNewValue().toString() + "'", TblSettlement.class));
	}
	
	public void Filtern() {
		ArrayList<String> werte = new ArrayList<String>();
		ArrayList<String> spalte= new ArrayList<String>();
		
		if(invoicenumber != null && invoicenumber!=""){
			werte.add(invoicenumber);
			spalte.add("invoicenumber");
		}
		
		if(sum != null && sum !=""){
			werte.add(sum);
			spalte.add("sum");
		}
		
		if(estimatate != null && estimatate !=""){
			werte.add(estimatate);
			spalte.add("estimation");
		}
		
		if(duedate != null){
			werte.add(format.format(duedate));
			spalte.add("duedate");
		}
		
		if(invoicestate != null && invoicestate!=""){
			werte.add(invoicestate);
			spalte.add("s.descriptionIs");
		}
		
		if(ordernumber != null && ordernumber!=""){
			werte.add(ordernumber);
			spalte.add("OMOrder.ordernumber");
		}
		
		if(incometype != null && incometype != ""){
			werte.add(incometype);
			spalte.add("OMIncometype.description_it");
		}
		
		if(settlementnumber != null && settlementnumber !=""){
			werte.add(settlementnumber);
			spalte.add("OMSettlement.id_settlement");
		}
		
				
		String[] w =  new String[werte.size()];
		String[] s =  new String[spalte.size()];
		int i = 0;
		
		for (String item : werte) {
			w[i] = item;
			s[i] = spalte.get(i);
			i++;
		}
		invoiceList.clear();
		invoiceList.addAll((ArrayList<TblInvoice>)entityLister.getFilterList(TblInvoice.class,"TblInvoice",w, s));
		changeFilterPopupRender();
	}
	
	/*
	 * ____________________________________________________________________________________
	 * All Popup-Methods (incl. functions, methods, getter and setter
	 */
	
	/* 
	 * _______________________________________________
	 * invoicepopup
	 */
	
	/**
	 * Changes render value of Invoice-Popup
	 */
	public void changePopupRender(){
		popupRender = !popupRender;		
	}
	
	/* Getter for PopupRender */
	public Boolean getPopupRender(){
		return popupRender;
	}
	
	/**
	 * Updates currently selected invoice or creates new invoice
	 */
	public void updateInvoice(){
		curInvoice.setTblInvoicestate(curInvoicestate);
		entityLister.UpdateObject(TblInvoice.class, curInvoice, curInvoice.getIdInvoice());
		init();	
	}
	
	/**
	 * Method to open popup to create new Invoice
	 */
	public void changePopupRenderNew(){
		
		/* New invoice is initialized */ 
		setCurInvoice(new TblInvoice());
		/* ID of new invoice is set to 0 (important for EntityManager) */
		getCurInvoice().setIdInvoice(0);
		
		/* Setting default values of new invoice */
		getCurInvoice().setTblSettlement((TblSettlement) entityLister.getSingleObject("SELECT * FROM omsettlement WHERE rownum <= 1", TblSettlement.class));
		getCurInvoice().setDuedate(new Date());
		getCurInvoice().setTblInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM ominvoicestate WHERE rownum <= 1", TblInvoicestate.class));
		getCurInvoice().getTblSettlement().setTblOrder((TblOrder) entityLister.getSingleObject("SELECT * FROM omorder WHERE rownum <= 1", TblOrder.class));
		
		/* Makes popup visible */
		changePopupRender();
	}
	
	/* 
	 * _______________________________________________
	 * deletepopup
	 */
	
	/* Setter of DeletePopup Render */
	private void setDeletePopupRender(Boolean deletePopupRender) {
		this.deletePopupRender = deletePopupRender;
	}

	/* Getter of DeletePopup Render */
	public Boolean getDeletePopupRender() {
		return deletePopupRender;
	}
	
	/**
	 * Changes render value of Delete-Popup
	 */
	public void changeDeletePopupRender(){
		setDeletePopupRender(true);
	}
	
	/**
	 * Deletes currently selected invoice
	 */
	public void deleteInvoice(){
		entityLister.DeleteObject(curInvoice.getIdInvoice(), TblInvoice.class);
		init();
	}
	
	/* 
	 * _______________________________________________
	 * filterpopup
	 */
	
	/* Getter for FilterPopupRender */
	public Boolean getFilterPopupRender(){
		return filterpopupRender;
	}	
	
	/**
	 * Changes render value of Filter-Popup
	 */
	public void changeFilterPopupRender(){
		filterpopupRender = !filterpopupRender;		
	}
}