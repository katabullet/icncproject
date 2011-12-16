package at.icnc.om.backingbeans;


import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import jxl.write.DateFormat;

import at.icnc.om.entitybeans.TblCustomerstate;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.entitybeans.TblProtocol;
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
public class ProtocolBackingBean extends AbstractBean implements Filterable {
	
	// List with all Reminders to avoid constant DB-Reading
	private ArrayList<TblProtocol> protocolList;
	
	// Variable to save selected users
	private TblUser curUser;
	
	// Binding of SelectOneMenu with Users
	private HtmlSelectOneMenu bindingUser;
	
	// Variable to save selected protocol
	private Object curActivity;
	
	// Binding of SelectOneMenu with Protocol
	private HtmlSelectOneMenu bindingActivity;

	/*
	 * ______________________________________________________________________________
	 * EntityLister functions
	 */
	
	/**
	 * This functions returns a list with all reminders
	 * @return reminderList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblProtocol> getProtocolList(){	
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			if(protocolList == null){
				protocolList = new ArrayList<TblProtocol>();		
				protocolList.addAll((ArrayList<TblProtocol>)entityLister.getObjectList(TblProtocol.class));
			}

		} catch (Exception e) {

			init();			
		}	
		
		return protocolList;
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
		protocolList = null;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}
	
	//Fields for the Filter
	private String activity;
	private Date datefrom;
	private Date dateto;
	private String username;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Format format = new SimpleDateFormat("yyyy-MM-dd");
	
	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getActivity() {
		return activity;
	}

	public void setDatefrom(Date datefrom) {
		this.datefrom = datefrom;
	}

	public Date getDatefrom() {
		return datefrom;
	}
	
	public void setDateto(Date dateto) {
		this.dateto = dateto;
	}

	public Date getDateto() {
		return dateto;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
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
		if(activity!=null && activity!=""){
			
			activity = "*" + activity.toLowerCase() + "*";
			
			werte.add(activity);
			spalte.add("t.change");
		}
		
		if(datefrom != null || dateto != null){
			
			/*Start - Set a default Value if none is set*/
			if(datefrom==null)
				try {
					datefrom = dateFormat.parse("1999-01-01");
				} catch (ParseException e) {
				}
			if(dateto==null)
				try {
					dateto = dateFormat.parse("3000-01-01");
				} catch (ParseException e) {
				}
			/*Start - Set a default Value if none is set*/
			
			werte.add(format.format(datefrom) + ":" + format.format(dateto));
			spalte.add("t.date");
		}
		
		if(username != null && username!=""){
			werte.add(username);
			spalte.add("u.username");
			
			/*Add a part of the Joinstatement*/
			joinStatement +=" INNER JOIN t.tblUser u";
		}

		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			protocolList.clear();
			
			protocolList.addAll((ArrayList<TblProtocol>)entityLister.getFilterList(TblProtocol.class,"TblProtocol", joinStatement,werte, spalte));

			/*Method to close the Popup*/
			changeFilterPopupRender();
		} catch (Exception e) {
			init();
			protocolList.clear();
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		}
		
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
			activity = null;
			datefrom = dateFormat.parse("1999-01-01");
			dateto = dateFormat.parse("3000-01-01");
			username="";
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function to create SelectItems of all Activities plus an empty one
	 * Important for combobox (needs SelectItem, not objects of Activities)
	 * @return List of SelectItem 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getActivityList(){
		ArrayList<SelectItem> activityDescription = new ArrayList<SelectItem>();
		
		activityDescription.add(new SelectItem(""));
		activityDescription.add(new SelectItem("Erzeugt"));
		activityDescription.add(new SelectItem("Aktualisiert"));
		activityDescription.add(new SelectItem("Gelöscht"));
		
		return activityDescription;			
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curUser is set to the selected one
	 * @param vce
	 */
	public void changeActivity(ValueChangeEvent vce){
		setCurActivity(vce.getNewValue());				
	}
	
	/**
	 * Function to create SelectItems of all Users plus an empty one
	 * Important for combobox (needs SelectItem, not objects of Users)
	 * @return List of SelectItem 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getUserList(){
		ArrayList<SelectItem> user = new ArrayList<SelectItem>();
		
		ArrayList<TblUser> iState = new ArrayList<TblUser>();
		iState.addAll((ArrayList<TblUser>)
				entityLister.getObjectList(TblUser.class));
		/*An empty Item is added to SelectItem-List*/
		user.add(new SelectItem());
		for (TblUser item : iState) {
			/* Name of each User is added to SelectItem-List */
			user.add(new SelectItem(item.getUsername()));
		}
		return user;			
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curUser is set to the selected one
	 * @param vce
	 */
	public void changeUser(ValueChangeEvent vce){
		setCurUser((TblUser) entityLister.getSingleObject("SELECT * FROM OMuser WHERE username = '" + 
				vce.getNewValue().toString() + "'", TblUser.class));				
	}

	public void setCurUser(TblUser curUser) {
		this.curUser = curUser;
	}

	public TblUser getCurUser() {
		return curUser;
	}

	public void setBindingUser(HtmlSelectOneMenu bindingUser) {
		this.bindingUser = bindingUser;
	}

	public HtmlSelectOneMenu getBindingUser() {
		return bindingUser;
	}

	public void setCurActivity(Object curActivity) {
		this.curActivity = curActivity;
	}

	public Object getCurActivity() {
		return curActivity;
	}

	public void setBindingActivity(HtmlSelectOneMenu bindingActivity) {
		this.bindingActivity = bindingActivity;
	}

	public HtmlSelectOneMenu getBindingActivity() {
		return bindingActivity;
	}
}