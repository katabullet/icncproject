package at.icnc.om.backingbeans;


import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import at.icnc.om.entitybeans.TblConcern;
import at.icnc.om.entitybeans.TblContactperson;
import at.icnc.om.entitybeans.TblCustomer;
import at.icnc.om.entitybeans.TblCustomerstate;
import at.icnc.om.entitybeans.TblUser;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of customerLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class CustomerBackingBean extends AbstractBean implements Filterable {

	// Variable to save selected Interval
	private TblCustomer curCustomer = new TblCustomer();
	
	// List with all Intervals to avoid constant DB-Reading
	private ArrayList<TblCustomer> customerList;
	
	// List with all Customerstates to avoid constant DB-Reading
	private ArrayList<TblCustomerstate> customerStateList;
	
	// List with all Concerns to avoid constant DB-Reading
	private ArrayList<TblConcern> concernList;
	
	// List with all Contactpersons to avoid constant DB-Reading
	private ArrayList<TblContactperson> contactpersonList;
	
	// List with all Salesmen (Users) to avoid constant DB-Reading
	private ArrayList<TblUser> salesmanList;
	
	// Variable to save selected Customerstate
	private TblCustomerstate curCustomerstate = new TblCustomerstate();
	
	// Variable to save selected Concern
	private TblConcern curConcern = new TblConcern();
	
	// Variable to save selected Contactperson
	private TblContactperson curContactperson = new TblContactperson();
	
	// Variable to save selected Salesman
	private TblUser curSalesman = new TblUser();
	
	// Variable to save selected Contactperson
	private String contactperson;
	
	// Binding of SelectOneMenu with Customerstates
	private HtmlSelectOneMenu bindingCustomerstates;
	
	// Binding of SelectOneMenu with Contactpersons
	private HtmlSelectOneMenu bindingContactpersons;
	
	// Binding of SelectOneMenu with Concerns
	private HtmlSelectOneMenu bindingConcerns;
	
	/* Field Declaration for Filter */
	private String customernameFilter;
	private String sapCnrFrom;
	private String sapCnrTo;
	private String contactpersonFilter;
	private String customerstateFilter;
	private Date customerstatedateFrom;
	private Date customerstatedateTo;
	private String concernFilter;
	private String salesmanFilter;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Format format = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * This functions returns a list with all intervals
	 * @return intervalinvoiceList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblCustomer> getCustomerList(){
		
		/* Intervals are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (customerList == null) {
			customerList = new ArrayList<TblCustomer>();
			customerList.addAll((ArrayList<TblCustomer>) 
					entityLister.getObjectList(TblCustomer.class));
		}
		
		
		/* Makes the user know which invoice is selected (it is shaded) */
		if (customerList != null) {
						
			for(TblCustomer curItem : customerList) {
				curItem.setSelected(false);
				if(curItem.getIdCustomer() == curCustomer.getIdCustomer() && getCurCustomer().getIdCustomer() != 0) {
					curItem.setSelected(true);
				}
			}
		}	
		return customerList;
	}
	
	@Override
	public void filterEntities() {
		/*ArrayList which managed the filter values*/
		ArrayList<String> werte = new ArrayList<String>();
		/*ArrayList which managed the filter columns*/
		ArrayList<String> spalte= new ArrayList<String>();
		/*Field with contains the Joinstatement*/
		String joinStatement="";
		
		/*Start Methods which check if the Fields are set and add them to the ArrayLists*/
		if(sapCnrFrom != null && sapCnrFrom != "" || sapCnrTo != null && sapCnrTo != ""){
			/* Set a default Value if none is set */
			if(sapCnrFrom == null) sapCnrFrom = "";
			if(sapCnrTo == "" || sapCnrTo == null) sapCnrTo="999999";
			
			werte.add(sapCnrFrom + ":" + sapCnrTo);
			spalte.add("t.sapcnr");
		}
		
		if(customerstatedateFrom != null || customerstatedateTo != null){
			
			/* Set a default Value if none is set */
			if(customerstatedateFrom == null){
				try {
					customerstatedateFrom = dateFormat.parse("1999-01-01");
				} catch (ParseException e) {					
				}
			}
			
			if(customerstatedateTo == null){
				try {
					customerstatedateTo = dateFormat.parse("3000-01-01");
				} catch (ParseException e) {
				}
			}
			
			werte.add(format.format(customerstatedateFrom) + ":" + format.format(customerstatedateTo));
			spalte.add("t.customerstatedate");
		}
		
		if(customernameFilter != null && customernameFilter != ""){
			werte.add(customernameFilter);
			spalte.add("t.customername");
		}
		
		if(contactpersonFilter != null && contactpersonFilter != ""){
			String[] contactperson = contactpersonFilter.split(" ");			
			werte.add(contactperson[0]);
			spalte.add("cp.idContactperson");
			
			/* Add a part to the Joinstatement */
			joinStatement += " INNER JOIN t.tblContactperson cp";
		}
		
		if(customerstateFilter != null && customerstateFilter != ""){				
			werte.add(customerstateFilter);
			spalte.add("cs.descriptionCs");
			
			/* Add a part to the Joinstatement */
			joinStatement += " INNER JOIN t.tblCustomerstate cs";
		}
		
		if(concernFilter != null && concernFilter != ""){				
			werte.add(concernFilter);
			spalte.add("c.concernname");
			
			/* Add a part to the Joinstatement */
			joinStatement += " INNER JOIN t.tblConcern c";
		}
		
		if(salesmanFilter != null && salesmanFilter != ""){				
			werte.add(salesmanFilter);
			spalte.add("u.username");
			
			/* Add a part to the Joinstatement */
			joinStatement += " INNER JOIN t.tblUser u";
		}
		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			customerList.clear();
			
			customerList.addAll((ArrayList<TblCustomer>) entityLister.getFilterList(TblCustomer.class, "TblCustomer", joinStatement, werte, spalte));
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		} catch (Exception e) {
			init();
			
			/*Deletes the current Table*/
			customerList.clear();
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		}	
	}

	@Override
	public void changePopupRenderNew() {
		/* New Customer is initialized */ 
		setCurCustomer(new TblCustomer());
		/* ID of new customer is set to 0 (important for EntityManager) */
		getCurCustomer().setIdCustomer(0);
		
		getCurCustomer().setTblConcern((TblConcern) entityLister.getSingleObject("SELECT * FROM OMConcern WHERE rownum <= 1", 
																					TblConcern.class));
		
		getCurCustomer().setTblCustomerstate((TblCustomerstate) entityLister.getSingleObject("SELECT * FROM OMCustomerstate " + 
																"WHERE rownum <= 1", TblCustomerstate.class));
		
		getCurCustomer().setTblContactperson((TblContactperson) entityLister.getSingleObject("SELECT * FROM OMcontactperson " +
																"WHERE rownum <= 1", TblContactperson.class));
		contactperson = getCurCustomer().getTblContactperson().getIdContactperson() + " " + 
						getCurCustomer().getTblContactperson().getFirstname() + " " + 
						getCurCustomer().getTblContactperson().getLastname();		
		
		changePopupRender();
	}

	@Override
	public void rowEvent(RowSelectorEvent re) {
		/* If no customer is selected, curCustomer is set to selected
		 * otherwise the customer is unselected
		 */
		if(curCustomer != null){			
			if(curCustomer.getIdCustomer() == customerList.get(re.getRow()).getIdCustomer()){
				curCustomer = new TblCustomer();
				setVisible(false);
			}else {
				curCustomer = null;
				curCustomer = customerList.get(re.getRow());
				curCustomerstate = curCustomer.getTblCustomerstate();
				curConcern = curCustomer.getTblConcern();
				contactperson = getCurCustomer().getTblContactperson().getIdContactperson() + " " + 
								getCurCustomer().getTblContactperson().getFirstname() + " " + 
								getCurCustomer().getTblContactperson().getLastname();
				setCurContactperson(getCurCustomer().getTblContactperson());
				setCurSalesman(curCustomer.getTblUser());
				setVisible(true);
			}
		}
	}

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curCustomer
	 */
	@Override
	public void init() {
		curCustomer = new TblCustomer();
		curCustomer.setIdCustomer(0);
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
		visible = !(curCustomer.getIdCustomer() == 0);			
		customerList = null;
		contactpersonList = null;
		customerStateList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}
	
	/**
	 * Resets Filter
	 */
	public void resetFilter(){
		try {
			customernameFilter = "";
			sapCnrFrom = "";
			sapCnrTo = "";
			contactpersonFilter = "";
			customerstateFilter = "";
			customerstatedateFrom = dateFormat.parse("1999-01-01");
			customerstatedateTo = dateFormat.parse("3000-01-01");
			concernFilter = "";
			salesmanFilter = "";
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes currently selected Customer
	 */
	@Override
	public void deleteEntity() {
		entityLister.DeleteObject(curCustomer.getIdCustomer(), TblCustomer.class);
		insertProtocol(TblCustomer.class, getCurCustomer().getIdCustomer(), deleteAction);
		curCustomer = new TblCustomer();
		curCustomer.setIdCustomer(0);
		refresh();
	}

	/**
	 * Updates currently selected Customer
	 * @throws Exception 
	 */
	@Override
	public void updateEntity(){
		boolean entityNew = false;
		if(getCurCustomer().getTblCustomerstate().getDescriptionCs() != curCustomerstate.getDescriptionCs()){
			getCurCustomer().setCustomerstatedate(new Date());
		}
		getCurCustomer().setTblCustomerstate(curCustomerstate);
		getCurCustomer().setTblConcern(curConcern);
		getCurCustomer().setTblContactperson(curContactperson);
		
		getCurCustomer().setTblUser(curSalesman);
		
		entityNew = (getCurCustomer().getIdCustomer() == 0);
		
		try{
			entityLister.UpdateObject(TblCustomer.class, curCustomer, curCustomer.getIdCustomer());
		}catch (Exception e) {
			if (e.getCause() instanceof org.eclipse.persistence.exceptions.OptimisticLockException){
				/* Reaction to OptimisticLockException here in BackingBean
				 * Message for user is important to make him/her know what is going on 
				 * and why the selected entity is not updated 
				 */
				/* Reading values of sitesBean out of requestMap (Map with all created Managed Beans */
				SitesBean sitesBean = (SitesBean) 
													 FacesContext.getCurrentInstance()
													 .getExternalContext().getSessionMap().get("sitesBean");

				if(sitesBean != null){
					sitesBean.setOptimisticLock(true);
				}	
			}
		}						
		
		if(entityNew){
			insertProtocol(TblCustomer.class, getCurCustomer().getIdCustomer(), createAction);
		}else {
			insertProtocol(TblCustomer.class, getCurCustomer().getIdCustomer(), updateAction);
		}				
		
		refresh();
	}

	public void setCurCustomer(TblCustomer curCustomer) {
		this.curCustomer = curCustomer;
	}

	public TblCustomer getCurCustomer() {
		return curCustomer;
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curCustomer is set to the selected one
	 * @param vce
	 */
	public void changeCustomerstate(ValueChangeEvent vce){
		if(!filterpopupRender){
			setCurCustomerstate((TblCustomerstate) entityLister.getSingleObject("SELECT * FROM OMCustomerstate WHERE description_cs = '" +
					vce.getNewValue().toString() + "'", TblCustomerstate.class));
		}else{
			setCustomerstateFilter(vce.getNewValue().toString());
		}
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curConcern is set to the selected one
	 * @param vce
	 */
	public void changeConcern(ValueChangeEvent vce){
		if(!filterpopupRender){
			setCurConcern((TblConcern) entityLister.getSingleObject("SELECT * FROM OMConcern WHERE concernname = '" +
					vce.getNewValue().toString() + "'", TblConcern.class));
		}else {
			setConcernFilter(vce.getNewValue().toString());
		}
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curContactperson is set to the selected one
	 * @param vce
	 */
	public void changeSalesman(ValueChangeEvent vce){
		if(!filterpopupRender){
			setCurSalesman((TblUser) entityLister.getSingleObject("SELECT * FROM OMUser WHERE username = '" +
						vce.getNewValue().toString() + "'", TblUser.class));
		}else {
			setSalesmanFilter(vce.getNewValue().toString());
		}
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curContactperson is set to the selected one
	 * @param vce
	 */
	public void changeContactperson(ValueChangeEvent vce){
		if(filterpopupRender){
			setContactpersonFilter(vce.getNewValue().toString());
		}else {
			String[] contactperson = vce.getNewValue().toString().split(" ");
			String contactpersonId = contactperson[0];
			
			setCurContactperson((TblContactperson) entityLister.getSingleObject("SELECT * FROM OMcontactperson WHERE id_contactperson = '" +
					contactpersonId + "'", TblContactperson.class));	
		}
	}
	
	/**
	 * Function to create SelectItems of all Customerstates
	 * Important for combobox (needs SelectItem, not objects of Customerstate)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getCustomerstateListDescription(){
		ArrayList<SelectItem> customerstates = new ArrayList<SelectItem>();
		if(filterpopupRender){
			customerstates.add(new SelectItem());
		}
		for (TblCustomerstate item : getCustomerstateList()) {
			/* Description of each customerstate is added to SelectItem-List */
			customerstates.add(new SelectItem(item.getDescriptionCs()));
		}
		return customerstates;			
	}
	
	/**
	 * Function to create SelectItems of all Concerns
	 * Important for combobox (needs SelectItem, not objects of concern)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getConcernListDescription(){
		ArrayList<SelectItem> concerns = new ArrayList<SelectItem>();
		
		if(filterpopupRender){
			concerns.add(new SelectItem());
		}
		for (TblConcern item : getConcernList()) {
			/* Concernname of each concern is added to SelectItem-List */
			concerns.add(new SelectItem(item.getConcernname()));
		}				
		
		return concerns;
	}
	
	/**
	 * Function to create SelectItems of all Contactpersons
	 * Important for combobox (needs SelectItem, not objects of contactperson)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getContactpersonListDescription(){
		ArrayList<SelectItem> contactpersons = new ArrayList<SelectItem>();
		if(filterpopupRender){
			contactpersons.add(new SelectItem());
		}
		for (TblContactperson item : getContactpersonList()) {
			/* Data of each Contactperson is added to SelectItem-List */
			contactpersons.add(new SelectItem(item.getIdContactperson() + " " + item.getFirstname() + " " + item.getLastname()));
		}
		
		return contactpersons;			
	}
	
	/**
	 * Function to create SelectItems of all Salesman (User)
	 * Important for combobox (needs SelectItem, not objects of User)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getSalesmanListDescription(){
		ArrayList<SelectItem> salesmen = new ArrayList<SelectItem>();
		if(filterpopupRender){
			salesmen.add(new SelectItem());
		}
		for (TblUser item : getSalesmanList()) {
			/* Username of each Salesman (User) is added to SelectItem-List */
			salesmen.add(new SelectItem(item.getUsername()));
		}
		return salesmen;			
	}
	
	/**
	 * This function returns a list with all Customerstates
	 * @return customerstateList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblCustomerstate> getCustomerstateList(){
		
		/* Customerstates are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(customerStateList == null){
			customerStateList = new ArrayList<TblCustomerstate>();
			customerStateList.addAll((ArrayList<TblCustomerstate>) 
					entityLister.getObjectList(TblCustomerstate.class));
		}
		
		/* If no customerstate is selected, curCustomerstate is set to selected
		 * otherwise the customerstate is unselected
		 */		
		if(getCurCustomerstate() != null){
			for(TblCustomerstate curItem : customerStateList){				
				if(curItem.getIdCustomerstate() == getCurCustomerstate().getIdCustomerstate()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurCustomerstate(customerStateList.get(0));
		}		
	
		return customerStateList;
	}
	
	/**
	 * This function returns a list with all Contactpersons
	 * @return contactpersonList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblContactperson> getContactpersonList(){
		
		/* Cotactpersons are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(contactpersonList == null){
			contactpersonList = new ArrayList<TblContactperson>();
			contactpersonList.addAll((ArrayList<TblContactperson>) 
					entityLister.getObjectList(TblContactperson.class));
		}
		
		/* If no contactperson is selected, curContactperson is set to selected
		 * otherwise the contactperson is unselected
		 */		
		if(getCurContactperson() != null){
			for(TblContactperson curItem : contactpersonList){				
				if(curItem.getIdContactperson() == getCurContactperson().getIdContactperson()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurContactperson(contactpersonList.get(0));
		}		
	
		return contactpersonList;
	}
	
	/**
	 * This function returns a list with all Concerns
	 * @return ConcernList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblConcern> getConcernList(){
		
		/* Concerns are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(concernList == null){
			concernList = new ArrayList<TblConcern>();
			concernList.addAll((ArrayList<TblConcern>) 
					entityLister.getObjectList(TblConcern.class));
		}
		
		/* If no concern is selected, curConcern is set to selected
		 * otherwise the concern is unselected
		 */		
		if(getCurConcern() != null){
			for(TblConcern curItem : concernList){				
				if(curItem.getIdConcern() == getCurConcern().getIdConcern()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurConcern(concernList.get(0));
		}		
		
		return concernList;
	}
	
	/**
	 * This function returns a list with all Salesmen
	 * @return salesmanList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblUser> getSalesmanList(){
		/* Salesmen (Users) are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(salesmanList == null){
			salesmanList = new ArrayList<TblUser>();
			salesmanList.addAll((ArrayList<TblUser>) 
					entityLister.getObjectList(TblUser.class));
		}
		
		/* If no Salesman (User) is selected, curSalesman is set to selected
		 * otherwise the Salesman is unselected
		 */		
		if(getCurSalesman() != null){
			for(TblUser curItem : salesmanList){				
				if(curItem.getIdUser() == getCurSalesman().getIdUser()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurSalesman(salesmanList.get(0));
		}		
	
		return salesmanList;
	}

	public void setCurCustomerstate(TblCustomerstate curCustomerstate) {
		this.curCustomerstate = curCustomerstate;
	}

	public TblCustomerstate getCurCustomerstate() {
		return curCustomerstate;
	}

	public void setCurConcern(TblConcern curConcern) {
		this.curConcern = curConcern;
	}

	public TblConcern getCurConcern() {
		return curConcern;
	}

	public void setCurContactperson(TblContactperson curContactperson) {
		this.curContactperson = curContactperson;
	}

	public TblContactperson getCurContactperson() {
		return curContactperson;
	}

	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}

	public String getContactperson() {
		return contactperson;
	}

	public void setCurSalesman(TblUser curSalesman) {
		this.curSalesman = curSalesman;
	}

	public TblUser getCurSalesman() {
		return curSalesman;
	}

	public void setCustomernameFilter(String customernameFilter) {
		this.customernameFilter = customernameFilter;
	}

	public String getCustomernameFilter() {
		return customernameFilter;
	}

	public void setSapCnrFrom(String sapCnrFrom) {
		this.sapCnrFrom = sapCnrFrom;
	}

	public String getSapCnrFrom() {
		return sapCnrFrom;
	}

	public void setSapCnrTo(String sapCnrTo) {
		this.sapCnrTo = sapCnrTo;
	}

	public String getSapCnrTo() {
		return sapCnrTo;
	}

	public void setContactpersonFilter(String contactpersonFilter) {
		this.contactpersonFilter = contactpersonFilter;
	}

	public String getContactpersonFilter() {
		return contactpersonFilter;
	}

	public void setCustomerstateFilter(String customerstateFilter) {
		this.customerstateFilter = customerstateFilter;
	}

	public String getCustomerstateFilter() {
		return customerstateFilter;
	}

	public void setCustomerstatedateFrom(Date customerstatedateFrom) {
		this.customerstatedateFrom = customerstatedateFrom;
	}

	public Date getCustomerstatedateFrom() {
		return customerstatedateFrom;
	}

	public void setCustomerstatedateTo(Date customerstatedateTo) {
		this.customerstatedateTo = customerstatedateTo;
	}

	public Date getCustomerstatedateTo() {
		return customerstatedateTo;
	}

	public void setConcernFilter(String concernFilter) {
		this.concernFilter = concernFilter;
	}

	public String getConcernFilter() {
		return concernFilter;
	}

	public void setSalesmanFilter(String salesmanFilter) {
		this.salesmanFilter = salesmanFilter;
	}

	public String getSalesmanFilter() {
		return salesmanFilter;
	}	
	
	/**
	 * Method to reset CustomerstateCombobox
	 * Method is called from CustomerstateBackingBean
	 */
	public void resetCustomerstateCombobox(){
		getBindingCustomerstates().getChildren().clear();
		customerStateList = null;
		getCustomerstateListDescription();
	}
	
	/**
	 * Method to reset ContactpersonCombobox
	 * Method is called from ContactpersonBackingBean
	 */
	public void resetContactpersonCombobox(){
		getBindingContactpersons().getChildren().clear();
		contactpersonList = null;
		getContactpersonListDescription();
	}
	
	/**
	 * Method to reset ConcernCombobox
	 * Method is called from ConcernBackingBean
	 */
	public void resetConcernCombobox(){
		getBindingConcerns().getChildren().clear();
		concernList = null;
		getConcernListDescription();
	}

	public void setBindingCustomerstates(HtmlSelectOneMenu bindingCustomerstates) {
		this.bindingCustomerstates = bindingCustomerstates;
	}

	public HtmlSelectOneMenu getBindingCustomerstates() {
		return bindingCustomerstates;
	}

	public void setBindingContactpersons(HtmlSelectOneMenu bindingContactpersons) {
		this.bindingContactpersons = bindingContactpersons;
	}

	public HtmlSelectOneMenu getBindingContactpersons() {
		return bindingContactpersons;
	}

	public void setBindingConcerns(HtmlSelectOneMenu bindingConcerns) {
		this.bindingConcerns = bindingConcerns;
	}

	public HtmlSelectOneMenu getBindingConcerns() {
		return bindingConcerns;
	}	
}