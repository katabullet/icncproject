package at.icnc.om.backingbeans;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblCostcentre;
import at.icnc.om.entitybeans.TblCustomer;
import at.icnc.om.entitybeans.TblDocument;
import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblOrderstate;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of orderLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class OrderBackingBean extends AbstractBean implements Filterable {
	
	// Variable to save selected Order
	private TblOrder curOrder = new TblOrder();
	
	// List with all Orders to avoid constant DB-Reading
	private ArrayList<TblOrder> orderList;
	
	// List with all Customers to avoid constant DB-Reading
	private ArrayList<TblCustomer> customerList;
	
	// List with all Incometypes to avoid constant DB-Reading
	private ArrayList<TblIncometype> incometypeList;
	
	// List with all Costcentres to avoid constant DB-Reading
	private ArrayList<TblCostcentre> costcentreList;
	
	// List with all Orderstates to avoid constant DB-Reading
	private ArrayList<TblOrderstate> orderstateList;
	
	// Variable to save selected Customer
	private TblCustomer curCustomer;
	
	// Variable to save selected Orderstate
	private TblOrderstate curOrderstate;
	
	// Binding of SelectOneMenu with Orderstate
	private HtmlSelectOneMenu bindingOrderstate;
	
	// Binding of SelectOneMenu with Customer
	private HtmlSelectOneMenu bindingCustomer;
	
	// Binding of SelectManyMenu with Incometypes
	private HtmlSelectManyMenu bindingIncometype;
	
	// Binding of SelectManyManu with Costcentres
	private HtmlSelectManyMenu bindingCostcentres;
	
	// List to save current incometypes
	private ArrayList<TblIncometype> curIncometypes;
	private String[] selectedIncometypes;	
	private String[] selectedCostcentres;
	
	//Field Declaration for Salesmanquery
	String username="";
	String join ="INNER JOIN t.tblCustomer c INNER JOIN c.tblUser u";
	
	/**
	 * This functions returns a list with all orders
	 * @return orderList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblOrder> getOrderList(){		
		
		/* Orders are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (orderList == null) {
			orderList = new ArrayList<TblOrder>();
			
		if(UserBean.userrole!=5){
			orderList.addAll((ArrayList<TblOrder>) 
					entityLister.getObjectList(TblOrder.class)); 		
		}
		else
		{
			UserBean user = (UserBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
			if(user!=null){
				username =user.getUsername();
			}
			
			orderList.addAll((ArrayList<TblOrder>)
					entityLister.getObjectListSalesman(TblOrder.class,"TblOrder", username , join));			
		}
		}
		
		/* Makes the user know which order is selected (it is shaded) */
		if (curOrder != null) {
			for(TblOrder curItem : orderList) {				
				if(curItem.getIdOrder() == curOrder.getIdOrder()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return orderList;
	}		
	
	/*
	 * Filter
	 * @see at.icnc.om.interfaces.Filterable#filterEntities()
	 */
	
	/* Field declaration for the filter */
		private String ordernumberfromFilter;
		private String ordernumbertoFilter;
		private Date orderdatefromFilter;
		private Date orderdatetoFilter;
		private Boolean travelcostsFilter = false;
		private String customerFilter;
		private String[] incometypesFilter;	
		private String[] costcentresFilter;
		private String orderstateFilter;
		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		private Format format = new SimpleDateFormat("yyyy-MM-dd");

		/**
		 * Filtern Method
		 * add the filter values and the columns to ArrayLists
		 * create the Join-Statement
		 * calls the "getFilterList" Method from the EntityLister to get the Filterlist
		 */
		@Override
		public void filterEntities() {
			/*ArrayList which managed the filter values*/
			ArrayList<String> werte = new ArrayList<String>();
			/*ArrayList which managed the filter columns*/
			ArrayList<String> spalte= new ArrayList<String>();
			/*Field with contains the Joinstatement*/
			String joinStatement ="";
			
			if(UserBean.userrole==5){
				
				UserBean user = (UserBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
				if(user!=null){
					username = user.getUsername();
				}
				joinStatement =" " + join;
				werte.add(username);
				spalte.add("u.username");
			}
			
			/*Start Methods which check if the Fields are set and add them to the ArrayLists*/
			if(orderdatefromFilter != null || orderdatetoFilter != null){
				
				/* Set a default Value if none is set */
				if(orderdatefromFilter == null){
					try {
						orderdatefromFilter = dateFormat.parse("1999-01-01");
					} catch (ParseException e) {					
					}
				}
				
				if(orderdatetoFilter == null){
					try {
						orderdatetoFilter = dateFormat.parse("3000-01-01");
					} catch (ParseException e) {
					}
				}
				
				werte.add(format.format(orderdatefromFilter) + ":" + format.format(orderdatetoFilter));
				spalte.add("t.orderdate");
			}
			
			/* if(travelcostsFilter != null){
				werte.add(travelcostsFilter ? "1" : "0");
				spalte.add("t.travelcosts");
			}*/
			
			if(ordernumberfromFilter != null && ordernumberfromFilter != "" || ordernumbertoFilter != null && ordernumbertoFilter != ""){
				/* Set a default Value if none is set */
				if(ordernumberfromFilter == null) ordernumberfromFilter = "";
				if(ordernumbertoFilter == "" || ordernumbertoFilter == null) ordernumbertoFilter = "999999";
				
				werte.add(ordernumberfromFilter + ":" + ordernumbertoFilter);
				spalte.add("t.ordernumber");
			}
			
			if(orderstateFilter != null && orderstateFilter != ""){				
				werte.add(orderstateFilter);
				spalte.add("os.descriptionOs");
				
				/* Add a part to the Joinstatement */
				joinStatement += " INNER JOIN t.tblOrderstate os";
			}
			
			try {
				if(incometypesFilter[0] != "");
			} catch (Exception e) {
				incometypesFilter = new String[1];
				incometypesFilter[0] = "";
			}
			if(incometypesFilter != null && incometypesFilter[0] != ""){
				String incometypes = "";
				for(String curIt : incometypesFilter){
					incometypes += curIt + ":";
				}
				incometypes = incometypes.substring(0, incometypes.length() - 1);
				werte.add(incometypes);
				spalte.add("it.descriptionIt");				
				
				joinStatement += " LEFT JOIN t.tblIncometypes it";
			}
			
			try {
				if(costcentresFilter[0] != "");
			} catch (Exception e) {
				costcentresFilter = new String[1];
				costcentresFilter[0] = "";
			}
			if(costcentresFilter != null && costcentresFilter[0] != ""){
				String costcentres = "";
				for(String curCc : costcentresFilter){
					costcentres += curCc + ":";
				}
				costcentres = costcentres.substring(0, costcentres.length() - 1);
				werte.add(costcentres);
				spalte.add("cc.descriptionCc");
				
				/* Add a part to the Joinstatement */
				joinStatement += " LEFT JOIN t.tblCostcentres cc";
			}
			
			if(customerFilter != null && customerFilter != ""){			
				werte.add(customerFilter);
				spalte.add("c.customername");
				
				if(UserBean.userrole!=5){
				/* Add a part to the Joinstatement */
				joinStatement += " INNER JOIN t.tblCustomer c";
				}
			}
			
			/*End Methods which check if the Fields are set and add them to the ArrayLists*/
			
			/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
			try {
				/*Deletes the current Table*/
				orderList.clear();
				
				orderList.addAll((ArrayList<TblOrder>)entityLister.getFilterList(TblOrder.class,"TblOrder", joinStatement,werte, spalte));
				
				/*Method to close the Popup*/
				changeFilterPopupRender();
			} catch (Exception e) {
				init();
			}	
		}		

	/**
	 * Method to open popup to create new Order
	 */
	@Override
	public void changePopupRenderNew() {
		/* New order is initialized */ 
		setCurOrder(new TblOrder());
		/* ID of new order is set to 0 (important for EntityManager) */
		getCurOrder().setIdOrder(0);
		setSelectedIncometypes(null);
		
		/* Setting default values of new order */
		getCurOrder().setOrderdate(new Date());
		getCurOrder().setTravelcosts(0);
		getCurOrder().setTblOrderstate((TblOrderstate) entityLister.getSingleObject("SELECT * FROM omorderstate WHERE rownum <= 1", TblOrderstate.class));
		getCurOrder().setTblCustomer((TblCustomer) entityLister.getSingleObject("SELECT * FROM omcustomer WHERE rownum <= 1", TblCustomer.class));
		/* Makes popup visible */
		changePopupRender();
	}

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {
		/* If no order is selected, curOrderstate is set to selected
		 * otherwise the order is unselected
		 */
		if(curOrder != null){			
			if(curOrder.getIdOrder() == orderList.get(re.getRow()).getIdOrder()){
				curOrder = new TblOrder();
				setVisible(false);
			}else {
				curOrder = orderList.get(re.getRow());
				setVisible(true);
				setCurOrderstate(curOrder.getTblOrderstate());
			}
		}
		setSelectedIncometypes(null);
		setSelectedCostcentres(null);
	}

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curOrder
	 */
	@Override
	public void init() {
		curOrder = new TblOrder();
		curOrder.setIdOrder(0);
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
		visible = !(curOrder.getIdOrder() == 0);
		orderList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}
	
	/**
	 * Deletes currently selected Order
	 */
	@Override
	public void deleteEntity() {
		try {
			entityLister.DeleteObject(curOrder.getIdOrder(), TblOrder.class);
			insertProtocol(TblOrder.class, getCurOrder().getIdOrder(), deleteAction);
		} catch (Exception e) {
			if (e.getCause() instanceof org.eclipse.persistence.exceptions.OptimisticLockException){
				handleOptimisticLockException();
			}
		}
		curOrder = new TblOrder();
		curOrder.setIdOrder(0);
		refresh();
	}

	/**
	 * Updates currently selected Order
	 */
	@Override
	public void updateEntity() {
		boolean entityNew = false;
		getCurOrder().setTblOrderstate(curOrderstate);
		getCurOrder().setTblCustomer(getCurCustomer());
		
		/* Costcentre und Incometype setzen */
		List<TblIncometype> selectedIt = new ArrayList<TblIncometype>();
		for(String item : getSelectedIncometypes()){
			selectedIt.add((TblIncometype) entityLister.getSingleObject("SELECT * FROM OMIncometype WHERE description_it = '" + item + "'", TblIncometype.class));
		}
		getCurOrder().setTblIncometypes(new HashSet<TblIncometype>(selectedIt));
		
		List<TblCostcentre> selectedCc = new ArrayList<TblCostcentre>();
		for(String item : getSelectedCostcentres()){
			selectedCc.add((TblCostcentre) entityLister.getSingleObject("SELECT * FROM OMCostcentre WHERE description_cc = '" + item + "'", TblCostcentre.class));
		}
		getCurOrder().setTblCostcentres(new HashSet<TblCostcentre>(selectedCc));
		
		entityNew = (getCurOrder().getIdOrder() == 0);
		try {
			entityLister.UpdateObject(TblOrder.class, curOrder, curOrder.getIdOrder());
			if(entityNew){
				insertProtocol(TblOrder.class, getCurOrder().getIdOrder(), createAction);
			}else {
				insertProtocol(TblOrder.class, getCurOrder().getIdOrder(), updateAction);
			}
		} catch (Exception e) {
			if (e.getCause() instanceof org.eclipse.persistence.exceptions.OptimisticLockException){
				handleOptimisticLockException();
			}
		}
		
		resetOrderCombobox();
		refresh();
	}
	
	/**
	 * Deletes the current filter
	**/
	public void clearFilter(){
		try {
			ordernumberfromFilter = "";
			ordernumbertoFilter = "";
			travelcostsFilter = false;
			customerFilter = "";
			incometypesFilter = new String[1];
			incometypesFilter[0] = "";
			orderdatefromFilter = dateFormat.parse("1999-01-01");
			orderdatetoFilter = dateFormat.parse("3000-01-01");
			costcentresFilter = new String[1];
			costcentresFilter[0] = "";
			orderstateFilter = "";
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}	
	
	/* Setter of curOrder */
	public void setCurOrder(TblOrder order){
		this.curOrder = order;
	}
	
	/* Getter of curOrder */
	public TblOrder getCurOrder(){
		return curOrder;
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curOrder is set to the selected one
	 * @param vce
	 */
	public void changeOrderstate(ValueChangeEvent vce){
		if(!filterpopupRender){
			setCurOrderstate((TblOrderstate) entityLister.getSingleObject("SELECT * FROM OMorderstate WHERE description_os = '" +
					vce.getNewValue().toString() + "'", TblOrderstate.class));
		}else{
			if(vce.getNewValue() != null){
				setOrderstateFilter(vce.getNewValue().toString());
			}
		}
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * selectedIncometypes is set to the selected ones
	 * @param vce
	 */
	public void changeIncometype(ValueChangeEvent vce){
		if(!filterpopupRender){
			setSelectedIncometypes((String[]) vce.getNewValue());
		}else{
			if(vce.getNewValue() != null){
				setIncometypesFilter((String[]) vce.getNewValue());
			}
		}
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * selectedCostcentres is set to the selected ones
	 * @param vce
	 */
	public void changeCostcentre(ValueChangeEvent vce){
		if(!filterpopupRender){
			setSelectedCostcentres((String[]) vce.getNewValue());
		}else{
			if(vce.getNewValue() != null){
				setCostcentresFilter((String[]) vce.getNewValue());
			}
		}
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curOrder is set to the selected one
	 * @param vce
	 */
	public void changeCustomer(ValueChangeEvent vce){
		if(!filterpopupRender){
			setCurCustomer((TblCustomer) entityLister.getSingleObject("SELECT * FROM OMcustomer WHERE customername = '" +
					vce.getNewValue().toString() + "'", TblCustomer.class));
		}else{
			if(vce.getNewValue() != null){
				setCustomerFilter(vce.getNewValue().toString());
			}
		}
	}
	
	/**
	 * Function to create SelectItems of all Orderstates
	 * Important for combobox (needs SelectItem, not objects of Orderstate)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getOrderstateListDescription(){
		ArrayList<SelectItem> orderstates = new ArrayList<SelectItem>();
		if(filterpopupRender){
			orderstates.add(new SelectItem());
		}
		for (TblOrderstate item : getOrderstateList()) {
			/* Description of each orderstate is added to SelectItem-List */
			orderstates.add(new SelectItem(item.getDescriptionOs()));
		}
		return orderstates;			
	}
	
	/**
	 * Function to create SelectItems of all Incometypes
	 * Important for combobox (needs SelectItem, not objects of Incometype)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getIncometypeListDescription(){
		ArrayList<SelectItem> incometypes = new ArrayList<SelectItem>();
		if(filterpopupRender){
			incometypes.add(new SelectItem(""));
		}
		String[] selectedIt = new String[getIncometypeList().size()];
		int x = 0;
		//ArrayList<String> selectedIt = new ArrayList<String>();
		for (TblIncometype item : getIncometypeList()) {
			
			if(getCurOrder().getIncometypesString().contains(item.getDescriptionIt())){
				//selectedIt.add(item.getDescriptionIt());
				selectedIt[x] = item.getDescriptionIt();
				x++;
			}
			/* Description of each incometype is added to SelectItem-List */
			incometypes.add(new SelectItem(item.getDescriptionIt()));
		}
		if(getSelectedIncometypes() == null){
			setSelectedIncometypes(selectedIt);
		}
		return incometypes;			
	}
	
	/**
	 * Function to create SelectItems of all Costcentres
	 * Important for combobox (needs SelectItem, not objects of Costcentre)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getCostcentreListDescription(){
		ArrayList<SelectItem> costcentres = new ArrayList<SelectItem>();
		if(filterpopupRender){
			costcentres.add(new SelectItem(""));
		}
		String[] selectedCc = new String[getCostcentreList().size()];
		int x = 0;
		//ArrayList<String> selectedIt = new ArrayList<String>();
		for (TblCostcentre item : getCostcentreList()) {
			if(getCurOrder().getCostcentresString().contains(item.getDescriptionCc())){
				selectedCc[x] = item.getDescriptionCc();
				x++;
			}
			
			/* Description of each costcentre is added to SelectItem-List */
			costcentres.add(new SelectItem(item.getDescriptionCc()));
		}
		if(getSelectedCostcentres() == null){
			setSelectedCostcentres(selectedCc);
		}
		return costcentres;			
	}
	
	/**
	 * Function to create SelectItems of all Customers
	 * Important for combobox (needs SelectItem, not objects of Customer)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getCustomerListDescription(){
		ArrayList<SelectItem> customers = new ArrayList<SelectItem>();
		if(filterpopupRender){
			customers.add(new SelectItem());
		}
		for (TblCustomer item : getCustomerList()) {
			/* Description of each customer is added to SelectItem-List */
			customers.add(new SelectItem(item.getCustomername()));
		}
		return customers;			
	}
	
	/**
	 * This function returns a list with all Orderstates
	 * @return orderstateList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblOrderstate> getOrderstateList(){
		
		/* Orderstates are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(orderstateList == null){
			orderstateList = new ArrayList<TblOrderstate>();
			orderstateList.addAll((ArrayList<TblOrderstate>) 
					entityLister.getObjectList(TblOrderstate.class));
		}
		
		/* If no orderstate is selected, curOrderstate is set to selected
		 * otherwise the orderstate is unselected
		 */		
		if(getCurOrderstate() != null){
			for(TblOrderstate curItem : orderstateList){				
				if(curItem.getIdOrderstate() == getCurOrderstate().getIdOrderstate()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurOrderstate(orderstateList.get(0));
		}		
	
		return orderstateList;
	}
	
	/**
	 * This function returns a list with all Incometypes
	 * @return incometypeList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblIncometype> getIncometypeList(){
		
		/* Incometypes are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(incometypeList == null){
			incometypeList = new ArrayList<TblIncometype>();
			incometypeList.addAll((ArrayList<TblIncometype>) 
					entityLister.getObjectList(TblIncometype.class));
		}
		
		/* If no incometype is selected, curIncometype is set to selected
		 * otherwise the incometype is unselected
		 */		
		if(getCurIncometypes() != null){
			for(TblIncometype curItem : incometypeList){				
				for(TblIncometype item : getCurIncometypes()){
					if(curItem.getIdIncometype() == item.getIdIncometype()){
						curItem.setSelected(true);
					}
				}
			}
		}else{
			setCurIncometypes(new ArrayList<TblIncometype>());
		}		
	
		return incometypeList;
	}
	
	/**
	 * This function returns a list with all Costcentres
	 * @return costcentreList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblCostcentre> getCostcentreList(){
		
		/* Costcentres are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(costcentreList == null){
			costcentreList = new ArrayList<TblCostcentre>();
			costcentreList.addAll((ArrayList<TblCostcentre>) 
					entityLister.getObjectList(TblCostcentre.class));
		}
	
		return costcentreList;
	}
	
	/**
	 * This function returns a list with all Customers
	 * @return customerList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblCustomer> getCustomerList(){
		
		/* Customers are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(customerList == null){
			customerList = new ArrayList<TblCustomer>();
			customerList.addAll((ArrayList<TblCustomer>) 
					entityLister.getObjectList(TblCustomer.class));
		}
		
		/* If no customer is selected, curCustomer is set to selected
		 * otherwise the customer is unselected
		 */		
		if(getCurCustomer() != null){
			for(TblCustomer curItem : customerList){				
				if(curItem.getIdCustomer() == getCurCustomer().getIdCustomer()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurCustomer(customerList.get(0));
		}		
	
		return customerList;
	}
	
	public void setCurOrderstate(TblOrderstate curOrderstate) {
		this.curOrderstate = curOrderstate;
	}

	public TblOrderstate getCurOrderstate() {
		return curOrderstate;
	}
	
	public void setCurIncometypes(ArrayList<TblIncometype> curIncometypes) {
		this.curIncometypes = curIncometypes;
	}

	public ArrayList<TblIncometype> getCurIncometypes() {
		return curIncometypes;
	}
	
	public void setCurCustomer(TblCustomer curCustomer) {
		this.curCustomer = curCustomer;
	}

	public TblCustomer getCurCustomer() {
		return curCustomer;
	}
	
	public void setOrderstateFilter(String orderstateFilter) {
		this.orderstateFilter = orderstateFilter;
	}

	public String getOrderstateFilter() {
		return orderstateFilter;
	}
	
	public void setOrdernumberfromFilter(String ordernumberFromFilter) {
		this.ordernumberfromFilter = ordernumberFromFilter;
	}
	
	public String getOrdernumberfromFilter() {
		return ordernumberfromFilter;
	}
	
	public void setOrdernumbertoFilter(String ordernumberToFilter) {
		this.ordernumbertoFilter = ordernumberToFilter;
	}
	
	public String getOrdernumbertoFilter() {
		return ordernumbertoFilter;
	}
	
	public void setCostcentresFilter(String[] costcentresFilter) {
		this.costcentresFilter = costcentresFilter;
	}
	
	public String[] getCostcentresFilter() {
		return costcentresFilter;
	}
	
	public void setIncometypesFilter(String[] incometypesFilter) {
		this.incometypesFilter = incometypesFilter;
	}
	
	public String[] getIncometypesFilter() {
		return incometypesFilter;
	}
	
	public void setCustomerFilter(String customerFilter) {
		this.customerFilter = customerFilter;
	}
	
	public String getCustomerFilter() {
		return customerFilter;
	}
	
	public void setTravelcostsFilter(Boolean travelcostsFilter) {
		this.travelcostsFilter = travelcostsFilter;
	}
	
	public Boolean getTravelcostsFilter() {
		return travelcostsFilter;
	}
	
	public void setOrderdatetoFilter(Date orderdateToFilter) {
		this.orderdatetoFilter = orderdateToFilter;
	}
	
	public Date getOrderdatetoFilter() {
		return orderdatetoFilter;
	}
	
	public void setOrderdatefromFilter(Date orderdateFromFilter) {
		this.orderdatefromFilter = orderdateFromFilter;
	}
	
	public Date getOrderdatefromFilter() {
		return orderdatefromFilter;
	}
	
	/**
	 * Method to reset OrderstateCombobox
	 * Method is called from OrderstateBackingBean
	 */
	public void resetOrderstateCombobox(){
		getBindingOrderstate().getChildren().clear();
		orderstateList = null;
		getOrderstateListDescription();
	}

	public void setBindingOrderstate(HtmlSelectOneMenu bindingOrderstate) {
		this.bindingOrderstate = bindingOrderstate;
	}

	public HtmlSelectOneMenu getBindingOrderstate() {
		return bindingOrderstate;
	}
	
	/**
	 * Method to reset IncometypesCombobox
	 * Method is called from IncometypeBackingBean
	 */
	public void resetIncometypeCombobox(){
		getBindingIncometype().getChildren().clear();
		incometypeList = null;
		getIncometypeListDescription();
	}

	public void setBindingIncometype(HtmlSelectManyMenu bindingIncometype) {
		this.bindingIncometype = bindingIncometype;
	}

	public HtmlSelectManyMenu getBindingIncometype() {
		return bindingIncometype;
	}
	
	/**
	 * Method to reset CustomerCombobox
	 * Method is called from CustomerBackingBean
	 */
	public void resetCustomerCombobox(){
		getBindingCustomer().getChildren().clear();
		customerList = null;
		getCustomerListDescription();
	}
	
	/**
	 * Method to reset SelectManyMenuCostcentres
	 * Method is called from CostcentreBackingBean
	 */
	public void resetCostcentresSelectManyMenu(){
		getBindingCostcentres().getChildren().clear();
		costcentreList = null;
		getCostcentreListDescription();
	}

	public void setBindingCustomer(HtmlSelectOneMenu bindingCustomer) {
		this.bindingCustomer = bindingCustomer;
	}

	public HtmlSelectOneMenu getBindingCustomer() {
		return bindingCustomer;
	}
	
	public void setSelectedIncometypes(String[] selectedIncometypes) {
		this.selectedIncometypes = selectedIncometypes;
	}

	public String[] getSelectedIncometypes() {
		return selectedIncometypes;
	}

	public void setSelectedCostcentres(String[] selectedCostcentres) {
		this.selectedCostcentres = selectedCostcentres;
	}
	
	public String[] getSelectedCostcentres() {
		return selectedCostcentres;
	}

	public void setBindingCostcentres(HtmlSelectManyMenu bindingCostcentres) {
		this.bindingCostcentres = bindingCostcentres;
	}

	public HtmlSelectManyMenu getBindingCostcentres() {
		return bindingCostcentres;
	}	
	
	/**
	 * Method to reset OrderCombobox used in settlementpopup
	 */
	public void resetOrderCombobox(){
		/* Reading values of settlementLister out of requestMap (Map with all created Managed Beans */
		SettlementBackingBean settlementLister = (SettlementBackingBean) 
											 FacesContext.getCurrentInstance()
											 .getExternalContext().getRequestMap().get("settlementLister");

		if(settlementLister != null){
			settlementLister.resetOrderCombobox();
		}	
	}
}
