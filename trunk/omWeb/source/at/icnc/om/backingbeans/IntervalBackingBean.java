package at.icnc.om.backingbeans;


import java.util.ArrayList;

import javax.faces.context.FacesContext;

import at.icnc.om.entitybeans.TblInterval;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of invoiceLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class IntervalBackingBean extends AbstractBean implements Filterable {
	
	// Variable to save selected Interval
	private TblInterval curInterval = new TblInterval();
	
	// List with all Intervals to avoid constant DB-Reading
	private ArrayList<TblInterval> intervalList;
	
	/**
	 * This functions returns a list with all invoices
	 * @return invoiceList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblInterval> getIntervalList(){		
		
		/* Invoices are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (intervalList == null) {
			intervalList = new ArrayList<TblInterval>();
			intervalList.addAll((ArrayList<TblInterval>) 
					entityLister.getObjectList(TblInterval.class));
		}
		
		/* Makes the user know which invoice is selected (it is shaded) */
		if (curInterval != null) {
			for(TblInterval curItem : intervalList) {				
				if(curItem.getIdInterval() == curInterval.getIdInterval()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return intervalList;
	}		

	/*
	 * Filter
	 * @see at.icnc.om.interfaces.Filterable#filterEntities()
	 */
		private String descriptionIv;
		private Long months;
		
		/* Setter of descriptionIv */
		public void setDescriptionIv(String descriptionIv) {
			this.descriptionIv = descriptionIv;
		}

		/* Getter of descriptionIv */
		public String getDescriptionIv() {
			return descriptionIv;
		}
		
		/* Setter of months */
		public void setMonths(Long months) {
			this.months = months;
		}

		/* Getter of months */
		public Long getMonths() {
			return months;
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
			if(descriptionIv != null && descriptionIv !=""){
				
				werte.add(descriptionIv);
				spalte.add("t.descriptionIv");
			}
			
			if(months != null && months != 0){
				
				werte.add(months.toString());
				spalte.add("t.months");
			}
			
			/*End Methods which check if the Fields are set and add them to the ArrayLists*/
			
			/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
			try {
				/*Deletes the current Table*/
				intervalList.clear();
				
				intervalList.addAll((ArrayList<TblInterval>)entityLister.getFilterList(TblInterval.class,"TblInterval", joinStatement,werte, spalte));
				
				/*Method to close the Popup*/
				changeFilterPopupRender();
			} catch (Exception e) {
				init();
				
				/*Deletes the current Table*/
				intervalList.clear();
				
				/*Method to close the Popup*/
				changeFilterPopupRender();
			}	
		}
		
		

	/**
	 * Method to open popup to create new Invoice
	 */
	@Override
	public void changePopupRenderNew() {
		curInterval = new TblInterval();
		curInterval.setIdInterval(0);
		changePopupRender();
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
		if(curInterval != null){			
			if(curInterval.getIdInterval() == intervalList.get(re.getRow()).getIdInterval()){
				curInterval = new TblInterval();
				setVisible(false);
			}else {
				curInterval = intervalList.get(re.getRow());
				setVisible(true);
			}
		}
	}

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curInterval
	 */
	@Override
	public void init() {
		curInterval = new TblInterval();
		curInterval.setIdInterval(0);
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
		visible = !(curInterval.getIdInterval() == 0);
		intervalList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}

	/**
	 * Deletes currently selected Interval
	 */
	@Override
	public void deleteEntity() {
		entityLister.DeleteObject(curInterval.getIdInterval(), TblInterval.class);
		curInterval = new TblInterval();
		curInterval.setIdInterval(0);
		refresh();
	}

	/**
	 * Updates currently selected Interval
	 */
	@Override
	public void updateEntity() {
		try {
			entityLister.UpdateObject(TblInterval.class, curInterval, curInterval.getIdInterval());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resetIntervalCombobox();
		refresh();
	}
	
	/**
	 * Deletes the current filter
	**/
	public void clearFilter(){
		
	}	
	
	/* Setter of curInterval */
	public void setCurInterval(TblInterval interval){
		this.curInterval = interval;
	}
	
	/* Getter of curInterval */
	public TblInterval getCurInterval(){
		return curInterval;
	}
	
	/**
	 * Method to reset IntervalCombobox used in settlementpopup
	 */
	public void resetIntervalCombobox(){
		/* Reading values of settlementLister out of requestMap (Map with all created Managed Beans */
		SettlementBackingBean settlementLister = (SettlementBackingBean) 
											 FacesContext.getCurrentInstance()
											 .getExternalContext().getRequestMap().get("settlementLister");

		if(settlementLister != null){
			settlementLister.resetIntervalCombobox();
		}	
	}
}