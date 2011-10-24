package at.icnc.om.backingbeans;

import java.util.ArrayList;
import java.util.Date;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.ext.RowSelectorEvent;

import at.icnc.om.entitybeans.TblConcern;
import at.icnc.om.entitybeans.TblCostcentre;
import at.icnc.om.entitybeans.TblCustomerstate;
import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblInterval;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblSettlement;
import at.icnc.om.entitybeans.TblUser;
import at.icnc.om.interfaces.Filterable;

public class SettlementBackingBean extends AbstractBean implements Filterable {
	
	// List with all Settlements to avoid constant DB-Reading
	private ArrayList <TblSettlement> settlementList;
	
	//List with all Interval to avoid constant DB-Reading
	private ArrayList<TblInterval> intervalList;
	
	//List with all Incometype to avoid constant DB-Reading
	private ArrayList<TblIncometype> incometypeList;
	
	// Variable to save current Settlement
	private TblSettlement curSettlement = new TblSettlement();
	
	//Variable to save current Interval
	private TblInterval curInterval = new TblInterval();
	
	//Variable to save current Incometype
	private TblIncometype curIncometype = new TblIncometype();
	
	// Variable to save selected Interval
	private String interval;
	
	// Variable to save selected Incometype
	private String incometype;
	
	// Binding of SelectOneMenu with Interval
	private HtmlSelectOneMenu bindingInterval;
	
	// Binding of SelectOneMenu with Incometype
	private HtmlSelectOneMenu bindingIncometype;
	
	/* Field declaration for Filter */
	private String settlementIDFrom;
	private String settlementIDTo;
	private String runtimeFilter;
	private String estimationFrom;
	private String estimationTo;
	private String intervalFilter;
	private String incometypeFilter;
	
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
			settlementList.addAll((ArrayList<TblSettlement>) 
					entityLister.getObjectList(TblSettlement.class));
		}
		
		/* Makes the user know which Settlement is selected (it is shaded) */
		if (getCurSettlement() != null) {
			for(TblSettlement curItem : settlementList) {				
				if(curItem.getIdSettlement() ==  getCurSettlement().getIdSettlement()) {
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
		
		/*Start Methods which check if the Fields are set and add them to the ArrayLists*/
		if(getSettlementIDFrom() != null && getSettlementIDFrom() != "" || getSettlementIDTo() != null && getSettlementIDTo() != ""){				
			
			/* Set a default Value if none is set */
			if(settlementIDFrom == null) settlementIDFrom = "0";
			if(settlementIDTo == "" || settlementIDTo == null) settlementIDTo="999999";
			
			werte.add(settlementIDFrom + ":" + settlementIDTo);
			spalte.add("t.idSettlement");			
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
			
			/*Deletes the current Table*/
			settlementList.clear();
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
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
		
		getCurSettlement().setTblInterval((TblInterval) entityLister.getSingleObject("SELECT * FROM OMInterval WHERE rownum <= 1", TblInterval.class));
		
		getCurSettlement().setTblIncometype((TblIncometype) entityLister.getSingleObject("SELECT * FROM OMIncometype WHERE rownum <=1", TblIncometype.class));
		
		
		insertProtocol("Neuer Datensatz angelegt");
		changePopupRender();
	}

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {
		
		/* If no settlement is selected, curSettlement is set to selected
		 * otherwise the settlement is unselected
		 */
		if(getCurSettlement() != null){			
			if(getCurSettlement().getIdSettlement() == settlementList.get(re.getRow()).getIdSettlement()){
				setCurSettlement(new TblSettlement());
				setVisible(false);
			}else {
				setCurSettlement(settlementList.get(re.getRow()));
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
		settlementList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}

	/**
	 * Method to delete currently selected entity
	 */
	@Override
	public void deleteEntity() {
		entityLister.DeleteObject(getCurSettlement().getIdSettlement(), TblSettlement.class);
		insertProtocol(TblSettlement.class, getCurSettlement().getIdSettlement(), deleteAction);
		refresh();
	}

	/**
	 * Method to update currently selected entity
	 */
	@Override
	public void updateEntity() {
		boolean entityNew = (getCurSettlement().getIdSettlement() == 0);
		getCurSettlement().setTblInterval(curInterval);
		getCurSettlement().setTblIncometype(curIncometype);
		
		if(entityNew){
			insertProtocol(TblSettlement.class, getCurSettlement().getIdSettlement(), createAction);
		}else {
			insertProtocol(TblSettlement.class, getCurSettlement().getIdSettlement(), updateAction);
		}
		
		entityLister.UpdateObject(TblSettlement.class, getCurSettlement(), getCurSettlement().getIdSettlement());
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
	 * curSettlement is set to the selected one
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
	 * curSettlement is set to the selected one
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
			incometypeList.addAll((ArrayList<TblIncometype>) 
					entityLister.getObjectList(TblIncometype.class));
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
}
