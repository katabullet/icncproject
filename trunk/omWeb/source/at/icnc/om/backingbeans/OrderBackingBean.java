package at.icnc.om.backingbeans;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblCustomer;
import at.icnc.om.entitybeans.TblCustomerstate;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblOrderstate;
import at.icnc.om.entitybeans.TblCostcentre;
import at.icnc.om.entitybeans.TblIncometype;
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
	
	// Variable to save selected Incometype
	private TblIncometype curIncometype;
	
	// Variable to save selected Costcentre
	private TblCostcentre curCostcentre;
	
	// Variable to save selected Orderstate
	private TblOrderstate curOrderstate;
	
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
			orderList.addAll((ArrayList<TblOrder>) 
					entityLister.getObjectList(TblOrder.class));
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
		private String travelcostsFilter;
		private String customerFilter;
		private String incometypesFilter;
		private String costcentresFilter;
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
			String joinStatement="";
			
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
			
			if(travelcostsFilter != null && travelcostsFilter != ""){
				werte.add(travelcostsFilter);
				spalte.add("t.travelcosts");
			}
			
			if(ordernumberfromFilter != null && ordernumberfromFilter != "" || ordernumbertoFilter != null && ordernumbertoFilter != ""){
				/* Set a default Value if none is set */
				if(ordernumberfromFilter == null) ordernumberfromFilter = "";
				if(ordernumbertoFilter == "" || ordernumbertoFilter == null) ordernumbertoFilter = "999999";
				
				werte.add(ordernumberfromFilter + ":" + ordernumbertoFilter);
				spalte.add("t.ordernumber");
			}
			
			if(orderstateFilter != null && orderstateFilter != ""){				
				werte.add(orderstateFilter);
				spalte.add("os.orderstate");
				
				/* Add a part to the Joinstatement */
				joinStatement += " INNER JOIN t.tblOrderstate os";
			}
			
			/* if(incometypesFilter != null && incometypesFilter != ""){				
				werte.add(incometypesFilter);
				spalte.add("it.incometype"); */
				
				/* Add a part to the Joinstatement */
				/* joinStatement += " INNER JOIN t.tblIncometype it";
			}
			
			if(costcentresFilter != null && costcentresFilter != ""){				
				werte.add(costcentresFilter);
				spalte.add("cc.costcentre");*/
				
				/* Add a part to the Joinstatement */
				/*joinStatement += " INNER JOIN t.tblCostcentre cc";
			}*/
			
			if(customerFilter != null && customerFilter != ""){			
				werte.add(customerFilter);
				spalte.add("c.customer");
				
				/* Add a part to the Joinstatement */
				joinStatement += " INNER JOIN t.tblCustomer c";
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
				
				/*Deletes the current Table*/
				orderList.clear();
				
				/*Method to close the Popup*/
				changeFilterPopupRender();
			}	
		}
		
		

	/**
	 * Method to open popup to create new Order
	 */
	@Override
	public void changePopupRenderNew() {
		curOrder = new TblOrder();
		curOrder.setIdOrder(0);
		
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
			}
		}
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
		entityLister.DeleteObject(curOrder.getIdOrder(), TblOrder.class);
		curOrder = new TblOrder();
		curOrder.setIdOrder(0);
		refresh();
	}

	/**
	 * Updates currently selected Order
	 */
	@Override
	public void updateEntity() {
		getCurOrder().setTblOrderstate(curOrderstate);
		/* Costcentre und Incometype setzen */
		
		entityLister.UpdateObject(TblOrder.class, curOrder, curOrder.getIdOrder());
		refresh();
	}
	
	/**
	 * Deletes the current filter
	**/
	public void clearFilter(){
		try {
			ordernumberfromFilter = "";
			ordernumbertoFilter = "";
			travelcostsFilter = "";
			customerFilter = "";
			incometypesFilter = "";
			orderdatefromFilter = dateFormat.parse("1999-01-01");
			orderdatetoFilter = dateFormat.parse("3000-01-01");
			costcentresFilter = "";
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
			setCurOrderstate((TblOrderstate) entityLister.getSingleObject("SELECT * FROM OMOrderstate WHERE description_os = '" +
					vce.getNewValue().toString() + "'", TblOrderstate.class));
		}else{
			setOrderstateFilter(vce.getNewValue().toString());
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
	
	public void setCurOrderstate(TblOrderstate curOrderstate) {
		this.curOrderstate = curOrderstate;
	}

	public TblOrderstate getCurOrderstate() {
		return curOrderstate;
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
	
	public void setCostcentresFilter(String costcentresFilter) {
		this.costcentresFilter = costcentresFilter;
	}
	
	public String getCostcentresFilter() {
		return costcentresFilter;
	}
	
	public void setIncometypesFilter(String incometypesFilter) {
		this.incometypesFilter = incometypesFilter;
	}
	
	public String getIncometypesFilter() {
		return incometypesFilter;
	}
	
	public void setCustomerFilter(String customerFilter) {
		this.customerFilter = customerFilter;
	}
	
	public String getCustomerFilter() {
		return customerFilter;
	}
	
	public void setTravelcostsFilter(String travelcostsFilter) {
		this.travelcostsFilter = travelcostsFilter;
	}
	
	public String getTravelcostsFilter() {
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
}
