package at.icnc.om.backingbeans;


import java.util.ArrayList;
import java.util.Collection;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblConcern;
import at.icnc.om.entitybeans.TblUser;
import at.icnc.om.entitybeans.TblUserrole;
import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of invoiceLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class UserBackingBean extends AbstractBean {
	
	// Variable to save selected User
	private TblUser curUser = new TblUser();
	
	// List with all Users to avoid constant DB-Reading
	private ArrayList<TblUser> userList;
	
	// List with all usersrole
	ArrayList<TblUserrole> userroles;
	
	// Variable to save selected usersrole
	private TblUserrole curUserrole;
	
	/*
	 * ______________________________________________________________________________
	 * EntityLister functions
	 */
	
	/**
	 * This functions returns a list with all users
	 * @return userList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblUser> getUserList(){		
		
		/* Users are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (userList == null) {
			userList = new ArrayList<TblUser>();
			userList.addAll((ArrayList<TblUser>) 
					entityLister.getObjectList(TblUser.class));
		}
		
		/* Makes the user know which invoice is selected (it is shaded) */
		if (curUser != null) {
			for(TblUser curItem : userList) {				
				if(curItem.getIdUser() == curUser.getIdUser()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return userList;
	}	
	
	/**
	 * This function returns a list with all userroles
	 * @return userroleList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblUserrole> getUserroleList(){
		
		/* Users are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(userroles == null){
			userroles = new ArrayList<TblUserrole>();
			userroles.addAll((Collection<? extends TblUserrole>) 
					entityLister.getObjectList(TblUserrole.class));
		}
		
		/* If no invoicestate is selected, curInvoicestate is set to selected
		 * otherwise the invoicestate is unselected
		 */		
		if(getCurUserrole() != null){
			for(TblUserrole curItem : userroles){				
				if(curItem.getIdUserrole() ==  getCurUserrole().getIdUserrole()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurUserrole(userroles.get(0));
		}		
	
		return userroles;
	}	

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {		
		
		/* If no user is selected, curUser is set to selected
		 * otherwise the user is unselected
		 */
		if(getCurUser() != null){			
			if(getCurUser().getIdUser() == userList.get(re.getRow()).getIdUser()){
				setCurUser(new TblUser());
				setVisible(false);
			}else {
				setCurUser(userList.get(re.getRow()));
				setVisible(true);
			}
		}
	}	

	/* Setter for currently selected user */
	public void setCurUser(TblUser curUser) {
		this.curUser= curUser;
	}
	
	/* Getter for currently selected user */
	public TblUser getCurUser() {
		return curUser;
	}	
	
	/**
	 * Function to create SelectItems of all Userroles
	 * Important for combobox (needs SelectItem, not objects of userrole)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getUserroleListDescription(){
		ArrayList<SelectItem> userrole = new ArrayList<SelectItem>();
		for (TblUserrole item : getUserroleList()) {
			/* Description of each userrole is added to SelectItem-List */
			userrole.add(new SelectItem(item.getDescriptionUr()));
		}
		return userrole;			
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curInvoicestate is set to the selected one
	 * @param vce
	 */
	public void changeUserrole(ValueChangeEvent vce){
		setCurUserrole((TblUserrole) entityLister.getSingleObject("SELECT * FROM OMuserrole WHERE description_ur = '" + 
				vce.getNewValue().toString() + "'", TblUserrole.class));				
	}

	/* Setter of curUserrole */
	public void setCurUserrole(TblUserrole curUserrole) {
		this.curUserrole = curUserrole;
	}

	/* Getter of curUserrole */
	public TblUserrole getCurUserrole() {
		return curUserrole;
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
		setCurUser(new TblUser());
		getCurUser().setIdUser(0);
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
		visible = !(getCurUser().getIdUser() == 0);
		userList = null;
		userroles = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}
	
	//___________________________________________________________________________
	//Filterpopup Methoden

	/*Fields declarations for the Filter*/
	private String username;
	private String userrole;

	
	/* Start of the Getter and Setter Methods for the Filter*/
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

	public String getUserrole() {
		return userrole;
	}
	/*End of the Getter and Setter Methods for the Filter*/
	
	/**
	 * Function to create SelectItems of all Userroles plus an empty one
	 * Important for combobox (needs SelectItem, not objects of userrole)
	 * @return List of SelectItem 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getuserroleListDescriptionFilter(){
		ArrayList<SelectItem> userrole = new ArrayList<SelectItem>();
	
		ArrayList<TblUserrole> uRole = new ArrayList<TblUserrole>();
		uRole.addAll((ArrayList<TblUserrole>)
				entityLister.getObjectList(TblUserrole.class));
		/*An empty Item is added to SelectItem-List*/
		userrole.add(new SelectItem());
		for (TblUserrole item : uRole) {
			/* Description of each userrole is added to SelectItem-List */
			userrole.add(new SelectItem(item.getDescriptionUr()));
		}
		return userrole;			
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curUserrole is set to the selected one
	 * @param vce
	 */
	public void UserroleChangeFilter(ValueChangeEvent vce){
		setUserrole(vce.getNewValue().toString());		
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
		if(username != null && username!=""){
			
			werte.add(username);
			spalte.add("t.username");

		}
		
		if(userrole != null && userrole !=""){
			werte.add(userrole);
			spalte.add("u.descriptionUr");
			
			/*Add a part of the Joinstatement*/
				joinStatement +=" INNER JOIN t.tblUserrole u";
			}
		
		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			userList.clear();
			
			userList.addAll((ArrayList<TblUser>)entityLister.getFilterList(TblUser.class,"TblUser", joinStatement,werte, spalte));
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		} catch (Exception e) {
			init();
		}	

	}	

	/**
	 * Deletes the current filter
	**/
	public void clearFilter(){
		username="";
		userrole="";
	}	
	
	@Override
	public void deleteEntity(){
		entityLister.DeleteObject(curUser.getIdUser(), TblUser.class);
		insertProtocol(TblUser.class, getCurUser().getIdUser(), deleteAction);
		refresh();
	}
	
	/**
	 * Updates currently selected invoice or creates new user
	 */
	@Override
	public void updateEntity(){
		boolean entityNew = (getCurUser().getIdUser() == 0);
		curUser.setTblUserrole(curUserrole);
		try {
			entityLister.UpdateObject(TblUser.class, curUser, curUser.getIdUser());
		} catch (Exception e) {
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
			insertProtocol(TblUser.class, getCurUser().getIdUser(), createAction);
		}else {
			insertProtocol(TblUser.class, getCurUser().getIdUser(), updateAction);
		}	
		
		refresh();
	}
	
	/**
	 * Method to open popup to create new User
	 */
	public void changePopupRenderNew(){
		
		/* New user is initialized */ 
		setCurUser(new TblUser());
		/* ID of new user is set to 0 (important for EntityManager) */
		getCurUser().setIdUser(0);
		
		/* Setting default values of new user */
		getCurUser().setTblUserrole((TblUserrole)entityLister.getSingleObject("SELECT * From omuserrole WHERE rownum <=1", TblUserrole.class));
		
		/*Set the Popupname*/
		setUserPopupName("Benutzer erstellen");
		/* Makes popup visible */
		changePopupRender();
	}
	
	
	/**
	 * Deletes currently selected invoice
	 */
	public void deleteUser(){
		entityLister.DeleteObject(curUser.getIdUser(), TblUser.class);
		curUser = new TblUser();
		getCurUser().setIdUser(0);
		refresh();
	}
	
	//Field to define the Popup name
	private String UserPopupName="Benutzer bearbeiten";
	
	//Setter method to set the Popup name
	public void setUserPopupName(String userPopupName) {
		UserPopupName = userPopupName;
	}
	//Getter method to get the Popup name
	public String getUserPopupName() {
		return UserPopupName;
	}
	
}