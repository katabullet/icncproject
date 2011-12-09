package at.icnc.om.backingbeans;


import java.util.ArrayList;

import javax.faces.context.FacesContext;

import at.icnc.om.entitybeans.TblConcern;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of concernLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class ConcernBackingBean extends AbstractBean implements Filterable {

	// List with all Concerns to avoid constant DB-Reading
	private ArrayList<TblConcern> concernList;
	
	// Variable to save current Concern
	private TblConcern curConcern = new TblConcern();
	
	/* Field Declaration for Filter */
	private String concernnameFilter;
	private String concernmatchcodeFilter;
	
	/**
	 * This functions returns a list with all invoices
	 * @return invoiceList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblConcern> getConcernList(){		
		
		/* Concerns are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (concernList == null) {
			concernList = new ArrayList<TblConcern>();
			concernList.addAll((ArrayList<TblConcern>) 
					entityLister.getObjectList(TblConcern.class));
		}
		
		/* Makes the user know which concern is selected (it is shaded) */
		if (getCurConcern() != null) {
			for(TblConcern curItem : concernList) {				
				if(curItem.getIdConcern() == getCurConcern().getIdConcern()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return concernList;
	}	
	
	/**
	 * Render Popup to create new Concern
	 */
	@Override
	public void changePopupRenderNew() {
		setCurConcern(new TblConcern());
		getCurConcern().setIdConcern(0);
		
		changePopupRender();
	}

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {
		
		/* If no concern is selected, curConcern is set to selected
		 * otherwise the concern is unselected
		 */
		if(getCurConcern() != null){			
			if(getCurConcern().getIdConcern() == concernList.get(re.getRow()).getIdConcern()){
				setCurConcern(new TblConcern());
				setVisible(false);
			}else {
				setCurConcern(concernList.get(re.getRow()));
				setVisible(true);
			}
		}
	}

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curConcern
	 */
	@Override
	public void init() {
		setCurConcern(new TblConcern());
		getCurConcern().setIdConcern(0);
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
		visible = !(getCurConcern().getIdConcern() == 0);
		concernList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}

	/**
	 * Method to reset Filter
	 */
	public void resetFilter() {
		setConcernmatchcodeFilter("");
		setConcernnameFilter("");
	}

	/**
	 * Method to delete currently selected entity
	 */
	@Override
	public void deleteEntity() {
		entityLister.DeleteObject(getCurConcern().getIdConcern(), TblConcern.class);
		insertProtocol(TblConcern.class, getCurConcern().getIdConcern(), deleteAction);
		setCurConcern(new TblConcern());
		getCurConcern().setIdConcern(0);
		refresh();
	}

	/**
	 * Method to update currently selected entity
	 */
	@Override
	public void updateEntity() {
		boolean entityNew = (getCurConcern().getIdConcern() == 0);
		try {
			entityLister.UpdateObject(TblConcern.class, getCurConcern(), getCurConcern().getIdConcern());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(entityNew){
			insertProtocol(TblConcern.class, getCurConcern().getIdConcern(), createAction);
		}else {
			insertProtocol(TblConcern.class, getCurConcern().getIdConcern(), updateAction);
		}	
		
		resetCustomerstateCombobox();
		refresh();		
	}

	public void setCurConcern(TblConcern curConcern) {
		this.curConcern = curConcern;
	}

	public TblConcern getCurConcern() {
		return curConcern;
	}

	public void setConcernnameFilter(String concernnameFilter) {
		this.concernnameFilter = concernnameFilter;
	}

	public String getConcernnameFilter() {
		return concernnameFilter;
	}

	public void setConcernmatchcodeFilter(String concernmatchcodeFilter) {
		this.concernmatchcodeFilter = concernmatchcodeFilter;
	}

	public String getConcernmatchcodeFilter() {
		return concernmatchcodeFilter;
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
		
		/*Start Methods which check if the Fields are set and add them to the ArrayLists*/
		if(getConcernnameFilter() != null && getConcernnameFilter() != ""){				
			werte.add(getConcernnameFilter());
			spalte.add("t.concernname");			
		}
		
		if(getConcernmatchcodeFilter() != null && getConcernmatchcodeFilter() != ""){				
			werte.add(getConcernmatchcodeFilter());
			spalte.add("t.concernmatchcode");			
		}
		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			concernList.clear();
			
			concernList.addAll((ArrayList<TblConcern>) entityLister.getFilterList(TblConcern.class, "TblConcern", "", werte, spalte));
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		} catch (Exception e) {
			init();
			
			/*Deletes the current Table*/
			concernList.clear();
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		}	
	}
	
	/**
	 * Method to reset ConcernCombobox used in customerpopup
	 */
	public void resetCustomerstateCombobox(){
		/* Reading values of customerLister out of requestMap (Map with all created Managed Beans */
		CustomerBackingBean customerLister = (CustomerBackingBean) 
											 FacesContext.getCurrentInstance()
											 .getExternalContext().getRequestMap().get("customerLister");

		if(customerLister != null){
			customerLister.resetConcernCombobox();
		}	
	}
}