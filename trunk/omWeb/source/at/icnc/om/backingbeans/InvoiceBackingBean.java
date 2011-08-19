package at.icnc.om.backingbeans;


import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

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
public class InvoiceBackingBean extends AbstractBean {
	
	// Variable to save selected Invoice
	private TblInvoice curInvoice = new TblInvoice();
	
	// List with all Invoices to avoid constant DB-Reading
	private ArrayList<TblInvoice> invoiceList;
	
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
	@Override
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
		setCurInvoice(new TblInvoice());
		getCurInvoice().setIdInvoice(0);
		paginator.gotoFirstPage();
		refresh();
		clearFilter();
	}

	/**
	 * Lists with DB-content are set NULL to
	 * make sure they are read from DB
	 */	
	@Override
	public void refresh() {
		visible = !(getCurInvoice().getIdInvoice() == 0);
		invoiceList = null;
		invoicestates = null;
		invoicestates = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}
	
	//___________________________________________________________________________
	//Filterpopup Methoden

	/*Fields declarations for the Filter*/
	private Integer invoicenumberFrom;
	private Integer invoicenumberTo;
	private Long sumFrom;
	private Long sumTo;
	private Long estimatateFrom;
	private Long estimatateTo;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Date duedateFrom;
	private Date duedateTo;
	private String invoicestate;
	private String ordernumber;
	private String incometype;
	private String settlementnumber;
	private Format format = new SimpleDateFormat("yyyy-MM-dd");
	
	/* Start of the Getter and Setter Methods for the Filter*/
	public void setInvoicenumberFrom(Integer invoicenumber) {
		this.invoicenumberFrom = invoicenumber;
	}

	public Integer getInvoicenumberFrom() {
		return invoicenumberFrom;
	}
	
	public void setInvoicenumberTo(Integer invoicenumber) {
		this.invoicenumberTo = invoicenumber;
	}

	public Integer getInvoicenumberTo() {
		return invoicenumberTo;
	}

	public void setSumFrom(Long sum) {
		this.sumFrom = sum;
	}

	public Long getSumFrom() {
		return sumFrom;
	}
	
	public void setSumTo(Long sum) {
		this.sumTo = sum;
	}

	public Long getSumTo() {
		return sumTo;
	}

	public void setEstimatateFrom(Long estimatate) {
		this.estimatateFrom = estimatate;
	}

	public Long getEstimatateFrom() {
		return estimatateFrom;
	}
	
	public void setEstimatateTo(Long estimatate) {
		this.estimatateTo = estimatate;
	}

	public Long getEstimatateTo() {
		return estimatateTo;
	}

	public void setDuedateFrom(Date duedate) {
		this.duedateFrom = duedate;
	}

	public Date getDuedateFrom() {
		return duedateFrom;
	}
	
	public void setDuedateTo(Date duedate) {
		this.duedateTo = duedate;
	}

	public Date getDuedateTo() {
		return duedateTo;
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
	/*End of the Getter and Setter Methods for the Filter*/
	
	/**
	 * Function to create SelectItems of all Invoicestates plus an empty one
	 * Important for combobox (needs SelectItem, not objects of invoicestates)
	 * @return List of SelectItem 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getinvoicestateListDescriptionFilter(){
		ArrayList<SelectItem> invoicestate = new ArrayList<SelectItem>();
	
		ArrayList<TblInvoicestate> iState = new ArrayList<TblInvoicestate>();
		iState.addAll((ArrayList<TblInvoicestate>)
				entityLister.getObjectList(TblInvoicestate.class));
		/*An empty Item is added to SelectItem-List*/
		invoicestate.add(new SelectItem());
		for (TblInvoicestate item : iState) {
			/* Description of each invoicestate is added to SelectItem-List */
			invoicestate.add(new SelectItem(item.getDescriptionIs()));
		}
		return invoicestate;			
	}
	
	/**
	 * Function to create SelectItems of all Ordernumbers plus an empty one
	 * Important for combobox (needs SelectItem, not objects of order)
	 * @return List of SelectItem 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getorderListDescription(){
		ArrayList<SelectItem> ordernumber = new ArrayList<SelectItem>();
	
		ArrayList<TblOrder> order = new ArrayList<TblOrder>();
		order.addAll((ArrayList<TblOrder>)
				entityLister.getObjectList(TblOrder.class));
		/*An empty Item is added to SelectItem-List*/
		ordernumber.add(new SelectItem());
		for (TblOrder item : order) {
			/* Number of each order is added to SelectItem-List */
			ordernumber.add(new SelectItem(item.getOrdernumber()));
		}
		return ordernumber;			
	}
	
	/**
	 * Function to create SelectItems of all Incometypes plus an empty one
	 * Important for combobox (needs SelectItem, not objects of incometype)
	 * @return List of SelectItem 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getincometypeListDescription(){
		ArrayList<SelectItem> incometypeDescription = new ArrayList<SelectItem>();
	
		ArrayList<TblIncometype> incometype = new ArrayList<TblIncometype>();
		incometype.addAll((Collection<? extends TblIncometype>) 
				entityLister.getObjectList(TblIncometype.class));
		/*An empty Item is added to SelectItem-List*/
		incometypeDescription.add(new SelectItem());
		for (TblIncometype item : incometype) {
			/* Description of each incometype is added to SelectItem-List */
			incometypeDescription.add(new SelectItem(item.getDescriptionIt()));
		}
		return incometypeDescription;			
	}
	
	/**
	 * Function to create SelectItems of all Settlement Ids plus an empty one
	 * Important for combobox (needs SelectItem, not objects of settlement)
	 * @return List of SelectItem 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getsettlementListDescription(){
		ArrayList<SelectItem> settlementID = new ArrayList<SelectItem>();
	
		ArrayList<TblSettlement> settlement = new ArrayList<TblSettlement>();
		settlement.addAll((Collection<? extends TblSettlement>) 
				entityLister.getObjectList(TblSettlement.class));
		/*An empty Item is added to SelectItem-List*/
		settlementID.add(new SelectItem());
		for (TblSettlement item : settlement) {
			/* ID of each settlement is added to SelectItem-List */
			settlementID.add(new SelectItem(item.getIdSettlement()));
		}
		return settlementID;			
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curInvoicestate is set to the selected one
	 * @param vce
	 */
	public void InvoicestateChangeFilter(ValueChangeEvent vce){
		setInvoicestate(vce.getNewValue().toString());		
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curOrder is set to the selected one
	 * @param vce
	 */
	public void OrderChangeFilter(ValueChangeEvent vce){
		setOrdernumber(vce.getNewValue().toString());	
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curIncometype is set to the selected one
	 * @param vce
	 */
	public void IncometypeChangeFilter(ValueChangeEvent vce){
		setIncometype(vce.getNewValue().toString());	
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curSettlement is set to the selected one
	 * @param vce
	 */
	public void SettlementChangeFilter(ValueChangeEvent vce){
		setSettlementnumber(vce.getNewValue().toString());
	}
	
	/**
	 * Filtern Method
	 * add the filter values and the columns to ArrayLists
	 * create the Join-Statement
	 * calls the "getFilterList" Method from the EntityLister to get the Filterlist
	 */
	public void Filtern() {
		/*ArrayList which managed the filter values*/
		ArrayList<String> werte = new ArrayList<String>();
		/*ArrayList which managed the filter columns*/
		ArrayList<String> spalte= new ArrayList<String>();
		/*Field with contains the Joinstatement*/
		String joinStatement="";
		
		/*Start Methods which check if the Fields are set and add them to the ArrayLists*/
		if(invoicenumberFrom != null && invoicenumberFrom!=0 || invoicenumberTo != null && invoicenumberTo!=0){
			
			/*Start - Set a default Value if none is set*/
			if(invoicenumberFrom==null) invoicenumberFrom=0;
			if(invoicenumberTo==0 || invoicenumberTo == null) invoicenumberTo=999999;
			/*End - Set a default Value if none is set*/
			
			werte.add(invoicenumberFrom + ":" + invoicenumberTo);
			spalte.add("t.invoicenumber");

		}
		
		if(sumFrom != null && sumFrom!=0 || sumTo != null && sumTo!=0){
			
			/*Start - Set a default Value if none is set*/
			if(sumFrom==null) sumFrom=(long) 0;
			if(sumTo==0 || sumTo == null) sumTo=(long)999999;
			/*End - Set a default Value if none is set*/
			
			werte.add(sumFrom + ":" + sumTo);
			spalte.add("t.sum");
		}
		
		if(estimatateFrom != null && estimatateFrom!=0 || estimatateTo != null && estimatateTo!=0){
			
			/*Start - Set a default Value if none is set*/
			if(estimatateFrom==null) estimatateFrom=(long)0;
			if(estimatateTo==0 || estimatateTo == null) estimatateTo=(long)999999;
			/*End - Set a default Value if none is set*/
			
			werte.add(estimatateFrom + ":" + estimatateTo);
			spalte.add("t.estimation");
		}
		
		if(duedateFrom != null || duedateTo != null){
			
			/*Start - Set a default Value if none is set*/
			if(duedateFrom==null)
				try {
					duedateFrom = dateFormat.parse("1999-01-01");
				} catch (ParseException e) {
				}
			if(duedateTo==null)
				try {
					duedateTo = dateFormat.parse("3000-01-01");
				} catch (ParseException e) {
				}
			/*Start - Set a default Value if none is set*/
			
			werte.add(format.format(duedateFrom) + ":" + format.format(duedateTo));
			spalte.add("t.duedate");
		}
		
		if(invoicestate != null && invoicestate!=""){
			werte.add(invoicestate);
			spalte.add("i.descriptionIs");
			
			/*Add a part of the Joinstatement*/
			joinStatement +=" INNER JOIN t.tblInvoicestate i";
		}
		
		if(ordernumber != null && ordernumber!=""){
			werte.add(ordernumber);
			spalte.add("o.ordernumber");
			
			/*Add a part of the Joinstatement*/
			joinStatement +=" INNER JOIN t.tblSettlement s INNER JOIN s.tblOrder o";
		}
		
		if(incometype != null && incometype != ""){
			werte.add(incometype);
			spalte.add("c.descriptionIt");
			
			/*Add a part of the Joinstatement*/
			if(!joinStatement.contains("t.tblSettlement s")){
				joinStatement +=" INNER JOIN t.tblSettlement s";
			}
			joinStatement +=" INNER JOIN s.tblIncometype c";
		}
		
		if(settlementnumber != null && settlementnumber !=""){
			werte.add(settlementnumber);
			spalte.add("s.idSettlement");
			
			/*Add a part of the Joinstatement*/
			if(!joinStatement.contains("t.tblSettlement s")){
				joinStatement +=" INNER JOIN t.tblSettlement s";
			}
		}
		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			invoiceList.clear();
			
			invoiceList.addAll((ArrayList<TblInvoice>)entityLister.getFilterList(TblInvoice.class,"TblInvoice", joinStatement,werte, spalte));
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		} catch (Exception e) {
			init();
			
			/*Deletes the current Table*/
			invoiceList.clear();
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		}	

	}	

	/**
	 * Deletes the current filter
	**/
	public void clearFilter(){
		invoicenumberFrom=null;
		invoicenumberTo=null;
		sumFrom=null;
		sumTo=null;
		estimatateFrom=null;
		estimatateTo=null;
		duedateFrom=null;
		duedateTo=null;
		invoicestate="";
		ordernumber="";
		incometype="";
		settlementnumber="";
	}	
	
	@Override
	public void deleteEntity(){
		entityLister.DeleteObject(curInvoice.getIdInvoice(), TblInvoice.class);
		refresh();
	}
	
	/**
	 * Updates currently selected invoice or creates new invoice
	 */
	@Override
	public void updateEntity(){
		curInvoice.setTblInvoicestate(curInvoicestate);
		entityLister.UpdateObject(TblInvoice.class, curInvoice, curInvoice.getIdInvoice());
		refresh();
	}
	
	/**
	 * Updates currently selected invoice or creates new invoice
	 */
	public void updateInvoice(){
		curInvoice.setTblInvoicestate(curInvoicestate);
		entityLister.UpdateObject(TblInvoice.class, curInvoice, curInvoice.getIdInvoice());
		refresh();
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
	
	
	/**
	 * Deletes currently selected invoice
	 */
	public void deleteInvoice(){
		entityLister.DeleteObject(curInvoice.getIdInvoice(), TblInvoice.class);
		curInvoice = new TblInvoice();
		getCurInvoice().setIdInvoice(0);
		refresh();
	}	
	
	public void validateSum(FacesContext context, UIComponent validate, Object value){
		FacesMessage msg = new FacesMessage("Das ist ein Test");
		Pattern pattern = Pattern.compile("[a-zA-Z]");
		Matcher matcher = pattern.matcher((String) value);
		
		if(matcher.matches()){
			context.addMessage(validate.getClientId(context), msg);
		}
		try {
			BigDecimal enteredSum = (BigDecimal) value;
						
		} catch (ValidatorException e) {
			context.addMessage(validate.getClientId(context), msg);
		} catch (ConverterException e){
			context.addMessage(validate.getClientId(context), msg);
		} catch (Exception e){
			context.addMessage(validate.getClientId(context), msg);
		}
	    
		context.addMessage(validate.getClientId(context), msg);
	}
}