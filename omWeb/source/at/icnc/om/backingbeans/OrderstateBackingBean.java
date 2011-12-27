package at.icnc.om.backingbeans;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import at.icnc.om.entitybeans.TblOrderstate;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of orderstateLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class OrderstateBackingBean extends AbstractBean implements Filterable {
	
	// Variable to save selected Orderstate
	private TblOrderstate curOrderstate = new TblOrderstate();
	
	// List with all Orderstates to avoid constant DB-Reading
	private ArrayList<TblOrderstate> orderstateList;
	
	/**
	 * This functions returns a list with all orderstates
	 * @return orderstateList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblOrderstate> getOrderstateList(){		
		
		/* Orderstates are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (orderstateList == null) {
			orderstateList = new ArrayList<TblOrderstate>();
			orderstateList.addAll((ArrayList<TblOrderstate>) 
					entityLister.getObjectList(TblOrderstate.class));
		}
		
		/* Makes the user know which orderstate is selected (it is shaded) */
		if (curOrderstate != null) {
			for(TblOrderstate curItem : orderstateList) {				
				if(curItem.getIdOrderstate() == curOrderstate.getIdOrderstate()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return orderstateList;
	}		

	/*
	 * Filter
	 * @see at.icnc.om.interfaces.Filterable#filterEntities()
	 */
		private String descriptionOs;
		
		/* Setter of descriptionOs */
		public void setDescriptionOs(String descriptionOs) {
			this.descriptionOs = descriptionOs;
		}

		/* Getter of descriptionOs */
		public String getDescriptionOs() {
			return descriptionOs;
		}

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
			if(descriptionOs != null && descriptionOs !=""){
				
				werte.add(descriptionOs);
				spalte.add("t.descriptionOs");
			}
			
			/*End Methods which check if the Fields are set and add them to the ArrayLists*/
			
			/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
			try {
				/*Deletes the current Table*/
				orderstateList.clear();
				
				orderstateList.addAll((ArrayList<TblOrderstate>)entityLister.getFilterList(TblOrderstate.class,"TblOrderstate", joinStatement,werte, spalte));
				
				/*Method to close the Popup*/
				changeFilterPopupRender();
			} catch (Exception e) {
				init();
			}	
		}
		
		

	/**
	 * Method to open popup to create new Orderstate
	 */
	@Override
	public void changePopupRenderNew() {
		curOrderstate = new TblOrderstate();
		curOrderstate.setIdOrderstate(0);
		changePopupRender();
	}

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {
		/* If no orderstate is selected, curOrderstate is set to selected
		 * otherwise the orderstate is unselected
		 */
		if(curOrderstate != null){			
			if(curOrderstate.getIdOrderstate() == orderstateList.get(re.getRow()).getIdOrderstate()){
				curOrderstate = new TblOrderstate();
				setVisible(false);
			}else {
				curOrderstate = orderstateList.get(re.getRow());
				setVisible(true);
			}
		}
	}

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curOrderstate
	 */
	@Override
	public void init() {
		curOrderstate = new TblOrderstate();
		curOrderstate.setIdOrderstate(0);
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
		visible = !(curOrderstate.getIdOrderstate() == 0);
		orderstateList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}

	/**
	 * Deletes currently selected Orderstate
	 */
	@Override
	public void deleteEntity() {
		try {
			entityLister.DeleteObject(curOrderstate.getIdOrderstate(), TblOrderstate.class);
			insertProtocol(TblOrderstate.class, getCurOrderstate().getIdOrderstate(), deleteAction);
		} catch (Exception e) {
			if (e.getCause() instanceof org.eclipse.persistence.exceptions.OptimisticLockException){
				handleOptimisticLockException();
			}
		}
		curOrderstate = new TblOrderstate();
		curOrderstate.setIdOrderstate(0);
		refresh();
	}

	/**
	 * Updates currently selected Orderstate
	 */
	@Override
	public void updateEntity() {
		boolean entityNew = false;
		entityNew = (getCurOrderstate().getIdOrderstate() == 0);
		try {
			entityLister.UpdateObject(TblOrderstate.class, curOrderstate, curOrderstate.getIdOrderstate());
			if(entityNew){
				insertProtocol(TblOrderstate.class, getCurOrderstate().getIdOrderstate(), createAction);
			}else {
				insertProtocol(TblOrderstate.class, getCurOrderstate().getIdOrderstate(), updateAction);
			}
		} catch (Exception e) {
			if (e.getCause() instanceof org.eclipse.persistence.exceptions.OptimisticLockException){
				handleOptimisticLockException();
			}
		}
		refresh();
	}
	
	/**
	 * Deletes the current filter
	**/
	public void clearFilter(){
		
	}	
	
	/* Setter of curOrderstate */
	public void setCurOrderstate(TblOrderstate orderstate){
		this.curOrderstate = orderstate;
	}
	
	/* Getter of curOrderstate */
	public TblOrderstate getCurOrderstate(){
		return curOrderstate;
	}
	
	/**
	 * Method to reset OrderstateCombobox used in orderpopup
	 */
	public void resetCustomerCombobox(){
		/* Reading values of orderlister out of requestMap (Map with all created Managed Beans */
		OrderBackingBean orderLister = (OrderBackingBean) 
											 FacesContext.getCurrentInstance()
											 .getExternalContext().getRequestMap().get("orderLister");

		if(orderLister != null){
			orderLister.resetOrderstateCombobox();
		}	
	}
}
