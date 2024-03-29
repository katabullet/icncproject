package at.icnc.om.backingbeans;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblCostcentre;
import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblInterval;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblSettlement;
import at.icnc.om.entitybeans.TblUser;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

public class SettlementBackingBean extends AbstractBean implements Filterable {
	
	// List with all Settlements to avoid constant DB-Reading
	private ArrayList <TblSettlement> settlementList;
	
	//List with all Interval to avoid constant DB-Reading
	private ArrayList<TblInterval> intervalList;
	
	//List with all Incometype to avoid constant DB-Reading
	private ArrayList<TblIncometype> incometypeList;
	
	// List with all costcentres to avoid constant DB-Reading
	private ArrayList<TblCostcentre> costcentreList;
	
	// List with all Orders to avoid constant DB-Reading
	private ArrayList<TblOrder> orderList;
	
	// Variable to save current Settlement
	private TblSettlement curSettlement = new TblSettlement();
	
	//Variable to save current Interval
	private TblInterval curInterval = new TblInterval();
	
	//Variable to save current Incometype
	private TblIncometype curIncometype = new TblIncometype();
	
	// Variable to save current Costcentre
	private TblCostcentre curCostcentre = new TblCostcentre();
	
	private ArrayList<TblCostcentre> costcentres = new ArrayList<TblCostcentre>();
	private ArrayList<TblIncometype> incometypes = new ArrayList<TblIncometype>();
	
	// Variable to save current Order
	private TblOrder curOrder = new TblOrder();
	
	// Variable to save selected Interval
	private String interval;
	
	// Variable to save selected Incometype
	private String incometype;
	
	// Variable to save selected Order
	private String order;
	
	// Binding of SelectOneMenu with Interval
	private HtmlSelectOneMenu bindingInterval;
	
	// Binding of SelectOneMenu with Incometype
	private HtmlSelectOneMenu bindingIncometype;
	
	// Binding of SelectOneMenu with Order
	private HtmlSelectOneMenu bindingOrder;
	
	// Binding of SelectOneMenu with Costcentre
	private HtmlSelectOneMenu bindingCostcentre;
	
	// Variable to save Sum that has to be paid in interval
	private BigDecimal settlementSum;
	
	// Variable to save Startdate of settlement (when the contract starts)
	private Date settlementDate = new Date();
	
	/* Field declaration for Filter */
	private String settlementIDFrom;
	private String settlementIDTo;
	private String ordernumberFrom;
	private String ordernumberTo;
	private String runtimeFilter;
	private String estimationFrom;
	private String estimationTo;
	private String intervalFilter;
	private String incometypeFilter;
	private String costcentreFilter;
	
	//Field Declaration for Salesmanquery
	String username="";
	String join ="INNER JOIN t.tblOrder o INNER JOIN o.tblCustomer c INNER JOIN c.tblUser u";
	
	/**
	 * This functions returns a list with all Settlements
	 * @return settlementList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblSettlement> getSettlementList(){		
		
		/* Settlements are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (settlementList == null) {
			settlementList = new ArrayList<TblSettlement>();
			
			if(UserBean.userrole != 5){
				settlementList.addAll((ArrayList<TblSettlement>) 
					entityLister.getObjectList(TblSettlement.class));
			}
			else
			{
				UserBean user = (UserBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
				if(user!=null){
					username =user.getUsername();
				}
				
				settlementList.addAll((ArrayList<TblSettlement>)
						entityLister.getObjectListSalesman(TblSettlement.class,"TblSettlement", username , join));			
			}
		}
		
		/* Makes the user know which Settlement is selected (it is shaded) */
		if (curSettlement != null) {
			for(TblSettlement curItem : settlementList) {				
				if(curItem.getIdSettlement() ==  curSettlement.getIdSettlement()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return settlementList;		
	}	
	
	/**
	 * Method to filter by using the saved criteria
	 */
	@Override
	public void filterEntities() {
		/*ArrayList which managed the filter values*/
		ArrayList<String> werte = new ArrayList<String>();
		/*ArrayList which managed the filter columns*/
		ArrayList<String> spalte= new ArrayList<String>();
		/*Field with contains the Joinstatement*/
		String joinStatement="";
		
		if(UserBean.userrole==5){
			
			UserBean user = (UserBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
			if(user!=null){
				username =user.getUsername();
			}
			joinStatement=" "+join;
			werte.add(username);
			spalte.add("u.username");
		}
		
		/*Start Methods which check if the Fields are set and add them to the ArrayLists*/
		if(getSettlementIDFrom() != null && getSettlementIDFrom() != "" || getSettlementIDTo() != null && getSettlementIDTo() != ""){				
			
			/* Set a default Value if none is set */
			if(settlementIDFrom == "" ||settlementIDFrom == null) settlementIDFrom = "0";
			if(settlementIDTo == "" || settlementIDTo == null) settlementIDTo="999999";
			
			werte.add(settlementIDFrom + ":" + settlementIDTo);
			spalte.add("t.idSettlement");			
		}
		
		if(getordernumberFrom() != null && getordernumberFrom() != "" || getordernumberTo() != null && getordernumberTo() != ""){				
			
			/* Set a default Value if none is set */
			if(ordernumberFrom == "" || ordernumberFrom == null) ordernumberFrom = "0";
			if(ordernumberTo == "" || ordernumberTo == null) ordernumberTo="999999";
			
			werte.add(ordernumberFrom + ":" + ordernumberTo);
			spalte.add("o.ordernumber");
			
			if(UserBean.userrole!=5){
			/* Add a part to the Joinstatement */
			joinStatement += " INNER JOIN t.tblOrder o";
			}
		}
		
		if(getRuntimeFilter() != null && getRuntimeFilter() != ""){				
			werte.add(getRuntimeFilter());
			spalte.add("t.runtime");			
		}	
		
		if(getIntervalFilter() != null && getIntervalFilter() !=""){				
			werte.add(getIntervalFilter());
			spalte.add("int.descriptionIv");
			
			/* Add a part to the Joinstatement */
			joinStatement += " INNER JOIN t.tblInterval int";
		}
		
		if(getIncometypeFilter() != null && getIncometypeFilter() != ""){				
			werte.add(getIncometypeFilter());
			spalte.add("inc.descriptionIt");
			
			/* Add a part to the Joinstatement */
			joinStatement += " INNER JOIN t.tblIncometype inc";
		}
		
		if(getCostcentreFilter() != null && getCostcentreFilter() != ""){
			werte.add(getCostcentreFilter());
			spalte.add("cc.descriptionCc");
			
			joinStatement += " INNER JOIN t.tblCostcentre cc";
		}
		
		//if(getEstimationFrom() != null && getEstimationFrom() != "" || getEstimationTo() != null && getEstimationTo() != ""){
			
			/* Set a default Value if none is set */
			//if(estimationFrom == null) estimationFrom = "";
			//if(estimationTo == "" || estimationTo == null) estimationTo="999999";
			
			//werte.add(estimationFrom + ":" + estimationTo);
			//spalte.add("i.estimation");	
			
			/* Add a part to the Joinstatement */
			//joinStatement += " INNER JOIN t.tblInvoice i";
		//}
		
		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			settlementList.clear();
			
			settlementList.addAll((ArrayList<TblSettlement>) entityLister.getFilterList(TblSettlement.class, "TblSettlement", joinStatement, werte, spalte));
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		} catch (Exception e) {			
			init();
		}	
	}
	
	/**
	 * Method to reset filter
	 */
	public void resetFilter(){
		setSettlementIDFrom("");
		setSettlementIDTo("");
		setRuntimeFilter("");
		setIntervalFilter("");
		setIncometypeFilter("");
		setCostcentreFilter("");
	}

	/**
	 * Render Popup to create new Settlement
	 */
	@Override
	public void changePopupRenderNew() {
		/* New Settlement is initialized */
		setCurSettlement(new TblSettlement());
		/* ID of new settlement is set to 0 (important for EntityManager) */
		getCurSettlement().setIdSettlement(0);
		
		setCurInterval((TblInterval) entityLister.getSingleObject("SELECT * " +
										"FROM OMInterval WHERE rownum <= 1", TblInterval.class));
		setCurIncometype((TblIncometype) entityLister.getSingleObject("SELECT * FROM OMIncometype " +
										"WHERE rownum <=1", TblIncometype.class));
		
		setCurOrder((TblOrder) entityLister.getSingleObject("SELECT * FROM OMOrder WHERE rownum <= 1", TblOrder.class));
				
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
		if(curSettlement != null){			
			if(curSettlement.getIdSettlement() == settlementList.get(re.getRow()).getIdSettlement()){
				setCurSettlement(new TblSettlement());
				setVisible(false);
				settlementSum = new BigDecimal(0);
				settlementDate = new Date();
			}else {
				curSettlement = settlementList.get(re.getRow());				
				curIncometype = settlementList.get(re.getRow()).getTblIncometype();
				curInterval = settlementList.get(re.getRow()).getTblInterval();
				setCurOrder(getCurSettlement().getTblOrder());
				curCostcentre = settlementList.get(re.getRow()).getTblCostcentre();
				/* costcentres = new ArrayList<TblCostcentre>(settlementList.get(re.getRow()).getTblOrder().getTblCostcentres());
				incometypes = new ArrayList<TblIncometype>(settlementList.get(re.getRow()).getTblOrder().getTblIncometypes()); */
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
				try {
					settlementDate = dateFormat.parse("31.12.3000");
				} catch (ParseException e) {
				}
				
				for(TblInvoice curInvoice : getCurSettlement().getTblInvoices()){
					if(((TblInvoice) curInvoice).getDuedate().compareTo(getSettlementDate()) < 0){
						setSettlementSum(new BigDecimal(((TblInvoice) curInvoice).getEstimation().intValueExact()));
						settlementSum = new BigDecimal(curInvoice.getEstimation().intValueExact());
						settlementDate = curInvoice.getDuedate();
						setSettlementDate(curInvoice.getDuedate());
					}
				}
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(settlementDate);
				int month = cal.get(Calendar.MONTH);
				int year = cal.get(Calendar.YEAR);
				month -= getCurSettlement().getTblInterval().getMonths().intValueExact();
				while(month < 0){
					month += 12;
					year -= 1;
				} 
				
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.MONTH, month);
				cal.set(Calendar.YEAR, year);
				
				settlementDate = cal.getTime();
				
				resetCostcentreCombobox();
				resetIncometypeCombobox();
				resetOrderCombobox(); 
				setVisible(true);
			}
		}		
		
	}				

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curSettlement
	 */
	@Override
	public void init() {
		setCurSettlement(new TblSettlement());
		getCurSettlement().setIdSettlement(0);
		paginator.gotoFirstPage();
		refresh();
		resetFilter();
	}

	/**
	 * Lists with DB-content are set NULL to
	 * make sure they are read from DB
	 */
	@Override
	public void refresh() {
		visible = !(getCurSettlement().getIdSettlement() == 0);
		settlementList=null;
		popupRender = false;
		setDeletePopupRender(false);
		filterpopupRender=false;
	}

	/**
	 * Method to delete currently selected entity
	 */
	@Override
	public void deleteEntity() {
		try {
			/*ArrayList which managed the filter values*/
			ArrayList<String> werte = new ArrayList<String>();
			/*ArrayList which managed the filter columns*/
			ArrayList<String> spalte= new ArrayList<String>();
			werte.add(String.valueOf(getCurSettlement().getIdSettlement()));
			spalte.add("s.idSettlement");
			
			ArrayList<TblInvoice> invoices = (ArrayList<TblInvoice>) entityLister.getFilterList(TblInvoice.class, "TblInvoice", " INNER JOIN t.tblSettlement s", werte, spalte);
			for (TblInvoice curInvoice : invoices) {			
				entityLister.DeleteObject(curInvoice.getIdInvoice(), TblInvoice.class);
			}
		} catch (Exception e) {
			if (e.getCause() instanceof org.eclipse.persistence.exceptions.OptimisticLockException){
				handleOptimisticLockException();
			}
		}
		
		getCurSettlement().getTblInvoices().clear();
		try {
			entityLister.DeleteObject(getCurSettlement().getIdSettlement(), TblSettlement.class);
			insertProtocol(TblSettlement.class, getCurSettlement().getIdSettlement(), deleteAction);
		} catch (Exception e) {
			if (e.getCause() instanceof org.eclipse.persistence.exceptions.OptimisticLockException){
				handleOptimisticLockException();
			}
		}
		
		setVisible(false);
		refresh();
	}

	/**
	 * Method to update currently selected entity
	 */
	@Override
	public void updateEntity() {

		boolean entityNew = getCurSettlement().getIdSettlement() == 0;
		getCurSettlement().setTblInterval(getCurInterval());
		getCurSettlement().setTblIncometype(getCurIncometype());
		getCurSettlement().setTblOrder(getCurOrder());		
		getCurSettlement().setTblCostcentre(getCurCostcentre());
		
		int standardRuntime = 10;
		BigDecimal zero = new BigDecimal(0);
		
		if(getCurSettlement().getRuntime().equals(zero)){
			getCurSettlement().setRuntime(new BigDecimal(standardRuntime));
		}
		
		try {
			entityLister.UpdateObject(TblSettlement.class, getCurSettlement(), getCurSettlement().getIdSettlement());
			if(entityNew){
				insertProtocol(TblSettlement.class, getCurSettlement().getIdSettlement(), createAction);			
			}else {
				insertProtocol(TblSettlement.class, getCurSettlement().getIdSettlement(), updateAction);
			}
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
		
		if(getCurSettlement().getTblInvoices() != null) {
			BigDecimal estimation = new BigDecimal(0);
			try {
				estimation = ((TblInvoice) (getCurSettlement().getTblInvoices().toArray()[0])).getEstimation();
			} catch (Exception e) {
			}
			
			if(settlementSum != estimation){
				for (TblInvoice curInvoice : getCurSettlement().getTblInvoices()) {
					try {
						entityLister.DeleteObject(curInvoice.getIdInvoice(), TblInvoice.class);
					} catch (Exception e) {
						if (e.getCause() instanceof org.eclipse.persistence.exceptions.OptimisticLockException){
							/* Reaction to OptimisticLockException here in BackingBean
							 * Message for user is important to make him/her know what is going on 
							 * and why the selected entity is not deleted 
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
				}
				createInvoices();
			}
		}else {
			if(entityNew || getCurSettlement().getRuntime() != settlementList.get(settlementList.indexOf(getCurSettlement())).getRuntime()){
				if(!entityNew){
					for (TblInvoice curInvoice : getCurSettlement().getTblInvoices()) {
						try {
							entityLister.DeleteObject(curInvoice.getIdInvoice(), TblInvoice.class);
						} catch (Exception e) {
							if (e.getCause() instanceof org.eclipse.persistence.exceptions.OptimisticLockException){
								/* Reaction to OptimisticLockException here in BackingBean
								 * Message for user is important to make him/her know what is going on 
								 * and why the selected entity is not deleted 
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
					}
				}
				createInvoices();
			}
		}
		
		refresh();
	}

	public void setCurSettlement(TblSettlement curSettlement) {
		this.curSettlement = curSettlement;
	}

	public TblSettlement getCurSettlement() {
		return curSettlement;
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curInterval is set to the selected one
	 * @param vce
	 */
	public void changeInterval(ValueChangeEvent vce){
		if(!filterpopupRender){
			setCurInterval((TblInterval) entityLister.getSingleObject("SELECT * FROM OMInterval WHERE description_iv = '" +
					vce.getNewValue().toString() + "'", TblInterval.class));
		}else{
			setIntervalFilter(vce.getNewValue().toString());
		}
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curCostcentre is set to the selected one
	 * @param vce
	 */
	public void changeCostcentre(ValueChangeEvent vce){
		if(!filterpopupRender){
			setCurCostcentre((TblCostcentre) entityLister.getSingleObject("SELECT * FROM OMCostcentre WHERE description_cc ='" +
					vce.getNewValue().toString() + "'", TblCostcentre.class));
		}else{
			setCostcentreFilter(vce.getNewValue().toString());
		}
	}
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curSettlement is set to the selected one
	 * @param vce
	 */
	 public void changeOrder(ValueChangeEvent vce){
		if(!filterpopupRender){
			setCurOrder((TblOrder) entityLister.getSingleObject("SELECT * FROM OMOrder WHERE ordernumber = '" +
					vce.getNewValue().toString() + "'", TblOrder.class));
			resetIncometypeCombobox();
			resetCostcentreCombobox();
		}else{
			setIncometypeFilter(vce.getNewValue().toString());
		}
	} 
	
	/**
	 * Method that Listens to Change Event of a combobox
	 * if another element in the combobox is selected, the value in
	 * curOrder is set to the selected one
	 * @param vce
	 */
	public void changeIncometype(ValueChangeEvent vce){
		if(!filterpopupRender){
			setCurIncometype((TblIncometype) entityLister.getSingleObject("SELECT * FROM OMIncometype WHERE description_it = '" +
					vce.getNewValue().toString() + "'", TblIncometype.class));
		}else{
			setIncometypeFilter(vce.getNewValue().toString());
		}
	}
	
	/**
	 * Function to create SelectItems of all Interval
	 * Important for combobox (needs SelectItem, not objects of Interval)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getIntervalListDescription(){
		ArrayList<SelectItem> interval = new ArrayList<SelectItem>();
		if(filterpopupRender){
			interval.add(new SelectItem());
		}
		for (TblInterval item : getIntervalList()) {
			/* Description of each interval is added to SelectItem-List */
			interval.add(new SelectItem(item.getDescriptionIv()));
		}
		return interval;			
	}
	
	/**
	 * Function to create SelectItems of all Incometypes
	 * Important for combobox (needs SelectItem, not objects of Incometype)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getIncometypeListDescription(){
		ArrayList<SelectItem> incometype = new ArrayList<SelectItem>();
		if(filterpopupRender){
			incometype.add(new SelectItem());
		}
		for (TblIncometype item : getIncometypeList()) {
			/* Description of each incometype is added to SelectItem-List */
			incometype.add(new SelectItem(item.getDescriptionIt()));
		}
		return incometype;			
	}
	
	/**
	 * Function to create SelectItems of all Costcentres
	 * Important for combobox (needs SelectItem, not objects of TblCostcentre)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getCostcentreListDescription(){
		ArrayList<SelectItem> costcentre = new ArrayList<SelectItem>();
		if(filterpopupRender){
			costcentre.add(new SelectItem());
		}
		for (TblCostcentre item : getCostcentreList()) {
			/* Description of each incometype is added to SelectItem-List */
			costcentre.add(new SelectItem(item.getDescriptionCc()));
		}
		return costcentre;			
	}
	
	/**
	 * Function to create SelectItems of all Orders
	 * Important for combobox (needs SelectItem, not objects of Incometype)
	 * @return List of SelectItem 
	 */
	public ArrayList<SelectItem> getOrderListDescription(){
		ArrayList<SelectItem> order = new ArrayList<SelectItem>();
		if(filterpopupRender){
			order.add(new SelectItem());
		}
		for (TblOrder item : getOrderList()) {
			/* id of each order is added to SelectItem-List */
			order.add(new SelectItem(item.getOrdernumber()));
		}
		return order;			
	}
	
	/**
	 * This function returns a list with all Interval
	 * @return customerstateList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblInterval> getIntervalList(){
		
		/* Interval are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(intervalList == null){
			intervalList = new ArrayList<TblInterval>();
			intervalList.addAll((ArrayList<TblInterval>) 
					entityLister.getObjectList(TblInterval.class));
		}
		
		/* If no interval is selected, curInterval is set to selected
		 * otherwise the interval is unselected
		 */		
		if(getCurInterval() != null){
			for(TblInterval curItem : intervalList){				
				if(curItem.getIdInterval() == getCurInterval().getIdInterval()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurInterval(intervalList.get(0));
		}		
	
		return intervalList;
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
			if(getCurOrder() != null && getCurOrder().getTblIncometypes() != null && !filterpopupRender){
				incometypeList.addAll(getCurOrder().getTblIncometypes());
			}else {
				incometypeList.addAll((ArrayList<TblIncometype>) 
					entityLister.getObjectList(TblIncometype.class));
			}
		}
		
		/* If no incometype is selected, curIncometype is set to selected
		 * otherwise the incometype is unselected
		 */		
		if(getCurIncometype() != null){
			for(TblIncometype curItem : incometypeList){				
				if(curItem.getIdIncometype() == getCurIncometype().getIdIncometype()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurIncometype(incometypeList.get(0));
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
			if(getCurOrder() != null && getCurOrder().getTblCostcentres() != null && !filterpopupRender){
				costcentreList.addAll(getCurOrder().getTblCostcentres());
			}else {
				costcentreList.addAll((ArrayList<TblCostcentre>) 
					entityLister.getObjectList(TblCostcentre.class));
			}
		}
		
		/* If no incometype is selected, curIncometype is set to selected
		 * otherwise the incometype is unselected
		 */		
		if(getCurCostcentre() != null){
			for(TblCostcentre curItem : costcentreList){				
				if(curItem.getIdCostcentre() == getCurCostcentre().getIdCostcentre()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurCostcentre(costcentreList.get(0));
			//setCurIncometype(incometypeList.get(0));
		}		
	
		return costcentreList;
	}
	
	/**
	 * This function returns a list with all Orders
	 * @return orderList
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<TblOrder> getOrderList(){
		
		/* Orders are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */		
		if(orderList == null){
			orderList = new ArrayList<TblOrder>();
			orderList.addAll((ArrayList<TblOrder>) 
					entityLister.getObjectList(TblOrder.class));
			
			if(orderList.size() != 0 ){
				//setCurOrder(orderList.get(0));
			}
		}		
		
		/* If no order is selected, curOrder is set selected
		 * otherwise the order is unselected
		 */		
		if(getCurOrder() != null){
			for(TblOrder curItem : orderList){				
				if(curItem.getIdOrder() == getCurOrder().getIdOrder()){
					curItem.setSelected(true);
				}
			}
		}else{
			setCurOrder(orderList.get(0));
		}		
	
		return orderList;
	}
	
	public void setCurInterval(TblInterval curInterval) {
		this.curInterval = curInterval;
	}

	public TblInterval getCurInterval() {
		return curInterval;
	}
	
	public void setCurIncometype(TblIncometype curIncometype) {
		this.curIncometype = curIncometype;
	}

	public TblIncometype getCurIncometype() {
		return curIncometype;
	}

	public void setRuntimeFilter(String runtimeFilter) {
		this.runtimeFilter = runtimeFilter;
	}

	public String getRuntimeFilter() {
		return runtimeFilter;
	}

	public void setIntervalFilter(String intervalFilter) {
		this.intervalFilter = intervalFilter;
	}

	public String getIntervalFilter() {
		return intervalFilter;
	}

	public void setIncometypeFilter(String incometypeFilter) {
		this.incometypeFilter = incometypeFilter;
	}

	public String getIncometypeFilter() {
		return incometypeFilter;
	}
	
	public void setordernumberFrom(String ordernumberFrom) {
		this.ordernumberFrom = ordernumberFrom;
	}

	public String getordernumberFrom() {
		return ordernumberFrom;
	}
	
	public void setordernumberTo(String ordernumberTo) {
		this.ordernumberTo = ordernumberTo;
	}

	public String getordernumberTo() {
		return ordernumberTo;
	}

	public void setEstimationFrom(String estimationFrom) {
		this.estimationFrom = estimationFrom;
	}

	public String getEstimationFrom() {
		return estimationFrom;
	}
	
	public void setEstimationTo(String estimationTo) {
		this.estimationTo = estimationTo;
	}

	public String getEstimationTo() {
		return estimationTo;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getInterval() {
		return interval;
	}

	public void setIncometype(String incometype) {
		this.incometype = incometype;
	}

	public String getIncometype() {
		return incometype;
	}

	public void setSettlementIDFrom(String settlementIDFrom) {
		this.settlementIDFrom = settlementIDFrom;
	}

	public String getSettlementIDFrom() {
		return settlementIDFrom;
	}

	public void setSettlementIDTo(String settlementIDTo) {
		this.settlementIDTo = settlementIDTo;
	}

	public String getSettlementIDTo() {
		return settlementIDTo;
	}
	
	/**
	 * Method to reset IntervalCombobox
	 * Method is called from IntervalBackingBean
	 */
	public void resetIntervalCombobox(){
		getBindingInterval().getChildren().clear();
		intervalList = null;
		getIntervalListDescription();
	}
	
	/**
	 * Method to reset OrderCombobox
	 * Method is called from OrderBackingBean
	 */
	public void resetOrderCombobox(){
		getBindingOrder().getChildren().clear();
		orderList = null;
		getOrderListDescription();
	}
	
	/**
	 * Method to reset CostcentreCombobox
	 */
	public void resetCostcentreCombobox(){
		getBindingCostcentre().getChildren().clear();
		costcentreList = null;
		getCostcentreListDescription();
	}
	
	/**
	 * Method to reset IncomtypeCombobox
	 * Method is called from IncometypeBackingBean
	 */
	public void resetIncometypeCombobox(){
		getBindingIncometype().getChildren().clear();
		incometypeList = null;
		getIncometypeListDescription();
	}

	public void setBindingInterval(HtmlSelectOneMenu bindingInterval) {
		this.bindingInterval = bindingInterval;
	}

	public HtmlSelectOneMenu getBindingInterval() {
		return bindingInterval;
	}

	public void setBindingIncometype(HtmlSelectOneMenu bindingIncometype) {
		this.bindingIncometype = bindingIncometype;
	}

	public HtmlSelectOneMenu getBindingIncometype() {
		return bindingIncometype;
	}

	public void setSettlementSum(BigDecimal settlementSum) {
		this.settlementSum = settlementSum;
	}

	public BigDecimal getSettlementSum() {
		return settlementSum;
	}
	
	private void createInvoices(){
		
		BigDecimal runtime;		
		
		runtime = getCurSettlement().getRuntime();
		
		BigDecimal months = curSettlement.getTblInterval().getMonths();
		
		// Create new calender
		Calendar cal = Calendar.getInstance();
		//Set value of calender to current Date
		cal.setTime(getSettlementDate()); 
		// Save year in variable
		int year = cal.get(Calendar.YEAR); 
		int month = cal.get(Calendar.MONTH) + 1;
		int day = 1;
				
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				
		Date dueDate = new Date();
		
		for(int x = months.intValue(); x <= runtime.multiply(new BigDecimal(12)).intValue(); x += months.intValue()){
			month += months.intValue();
			if(month > 12){
				year++;
				month -= 12;
			}
			
			String strDueDate = year + "-" + month + "-" + day;
						
			try {
				dueDate = formatter.parse(strDueDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			TblInvoice curInvoice = new TblInvoice();
			curInvoice.setDuedate(dueDate);
			curInvoice.setEstimation(settlementSum);
			curInvoice.setIdInvoice(0);
			curInvoice.setTblSettlement(getCurSettlement());
			curInvoice.setTblInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM OMinvoicestate WHERE rownum <= 1", TblInvoicestate.class));
			curInvoice.setSum(new BigDecimal(0));
			curInvoice.setInvoicenumber("");
			
			try {
				entityLister.UpdateObject(TblInvoice.class, curInvoice, curInvoice.getIdInvoice());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}


	public void setBindingOrder(HtmlSelectOneMenu bindingOrder) {
		this.bindingOrder = bindingOrder;
	}

	public HtmlSelectOneMenu getBindingOrder() {
		return bindingOrder;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrder() {
		return order;
	}

	public void setCurOrder(TblOrder curOrder) {
		this.curOrder = curOrder;
	}

	public TblOrder getCurOrder() {
		return curOrder;
	}

	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Date getSettlementDate() {
		return settlementDate;
	}

	public void setCurCostcentre(TblCostcentre curCostcentre) {
		this.curCostcentre = curCostcentre;
	}

	public TblCostcentre getCurCostcentre() {
		return curCostcentre;
	}

	public void setBindingCostcentre(HtmlSelectOneMenu bindingCostcentre) {
		this.bindingCostcentre = bindingCostcentre;
	}

	public HtmlSelectOneMenu getBindingCostcentre() {
		return bindingCostcentre;
	}

	public void setCostcentreFilter(String costcentreFilter) {
		this.costcentreFilter = costcentreFilter;
	}

	public String getCostcentreFilter() {
		return costcentreFilter;
	}
}
