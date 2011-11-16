package at.icnc.om.backingbeans;


import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblCustomerstate;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.entitybeans.TblUser;
import at.icnc.om.entitybeans.TblUserrole;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of invoiceLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class ReminderBackingBean extends AbstractBean implements Filterable {
	
	// List with all Reminders to avoid constant DB-Reading
	private ArrayList<TblInvoice> reminderList;
	
	// List with all invoicestates 
	ArrayList<TblInvoicestate> invoicestates;
	
	// Variable to save selected invoicestate
	private TblInvoicestate curInvoicestate;

	/*
	 * ______________________________________________________________________________
	 * EntityLister functions
	 */
	
	/**
	 * This functions returns a list with all reminders
	 * @return reminderList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblInvoice> getReminderList(){	
		String status = "Erledigt";
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> Spalte = new ArrayList<String>();
		ArrayList<String> Werte = new ArrayList<String>();
		
		Spalte.add("t.duedate");
		Spalte.add("i.descriptionIs");
		
		Werte.add(format.format(new Date()));
		Werte.add(status);
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			if(reminderList == null){
			reminderList = new ArrayList<TblInvoice>();		
			reminderList.addAll((ArrayList<TblInvoice>)entityLister.getReminderList(Werte, Spalte));
			}

		} catch (Exception e) {

			init();			
		}	
		
		return reminderList;
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
		paginator.gotoFirstPage();
		resetFilter();
		refresh();
	}

	/**
	 * Lists with DB-content are set NULL to
	 * make sure they are read from DB
	 */	
	@Override
	public void refresh() {
		reminderList = null;
		invoicestate=null;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}
	
	//Fields for the Filter
	private Integer invoicenumberFrom;
	private Integer invoicenumberTo;
	private String customername;
	private Date duedateFrom;
	private Date duedateTo;
	private String invoicestate;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Format format = new SimpleDateFormat("yyyy-MM-dd");

	public void setInvoicenumberFrom(Integer invoicenumberFrom) {
		this.invoicenumberFrom = invoicenumberFrom;
	}

	public Integer getInvoicenumberFrom() {
		return invoicenumberFrom;
	}

	public void setInvoicenumberTo(Integer invoicenumberTo) {
		this.invoicenumberTo = invoicenumberTo;
	}

	public Integer getInvoicenumberTo() {
		return invoicenumberTo;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomername() {
		return customername;
	}

	public void setDuedateFrom(Date duedateFrom) {
		this.duedateFrom = duedateFrom;
	}

	public Date getDuedateFrom() {
		return duedateFrom;
	}

	public void setDuedateTo(Date duedateTo) {
		this.duedateTo = duedateTo;
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

	@Override
	public void filterEntities() {
		// TODO Auto-generated method stub
		
		/*ArrayList which managed the filter values*/
		ArrayList<String> werte = new ArrayList<String>();
		/*ArrayList which managed the filter columns*/
		ArrayList<String> spalte= new ArrayList<String>();
		/*Field with contains the Joinstatement*/
		String joinStatement="";
		
		/*Start Methods which check if the Fields are set and add them to the ArrayLists*/
		if(invoicenumberFrom != null || invoicenumberTo!= null){
			
			/*Start - Set a default Value if none is set*/
			if(invoicenumberFrom==null) invoicenumberFrom=0;
			if(invoicenumberTo == null) invoicenumberTo=999999;
			/*End - Set a default Value if none is set*/
			
			werte.add(invoicenumberFrom + ":" + invoicenumberTo);
			spalte.add("t.invoicenumber");
		}

		if(customername != null && customername!=""){
			
			werte.add(customername);
			spalte.add("c.customername");
			
			/*Add a part of the Joinstatement*/
			joinStatement +=" INNER JOIN t.tblSettlement s INNER JOIN s.tblOrder o INNER JOIN o.tblCustomer c";
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

		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			reminderList.clear();
			
			reminderList.addAll((ArrayList<TblInvoice>)entityLister.getFilterList(TblInvoice.class,"TblInvoice", joinStatement,werte, spalte));

			/*Method to close the Popup*/
			changeFilterPopupRender();
		} catch (Exception e) {
			init();
			reminderList.clear();
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		}
		
	}	

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

	@Override
	public void changePopupRenderNew() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rowEvent(RowSelectorEvent re) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEntity() {
		// TODO Auto-generated method stub
		
	}
	public void resetFilter(){
		try {
			invoicenumberFrom = null;
			invoicenumberTo = null;
			customername = "";
			duedateFrom = dateFormat.parse("1999-01-01");
			duedateTo = dateFormat.parse("3000-01-01");
			invoicestate="";
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}