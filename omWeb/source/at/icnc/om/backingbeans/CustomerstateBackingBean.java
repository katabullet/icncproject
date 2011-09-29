package at.icnc.om.backingbeans;


import java.util.ArrayList;

import javax.faces.context.FacesContext;

import at.icnc.om.entitybeans.TblConcern;
import at.icnc.om.entitybeans.TblCustomerstate;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of customerstateLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class CustomerstateBackingBean extends AbstractBean implements Filterable {

	// List with all Customerstates to avoid constant DB-Reading
	private ArrayList<TblCustomerstate> customerstateList;
	
	// Variable to save current Customerstate
	private TblCustomerstate curCustomerstate = new TblCustomerstate();
	
	/* Field declaration for Filter */
	private String descriptionCsFilter;
	
	/**
	 * This functions returns a list with all Customerstates
	 * @return customerstateList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblCustomerstate> getCustomerstateList(){		
		
		/* Customerstates are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (customerstateList == null) {
			customerstateList = new ArrayList<TblCustomerstate>();
			customerstateList.addAll((ArrayList<TblCustomerstate>) 
					entityLister.getObjectList(TblCustomerstate.class));
		}
		
		/* Makes the user know which customerstate is selected (it is shaded) */
		if (getCurCustomerstate() != null) {
			for(TblCustomerstate curItem : customerstateList) {				
				if(curItem.getIdCustomerstate() == getCurCustomerstate().getIdCustomerstate()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return customerstateList;
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
		if(getDescriptionCsFilter() != null && getDescriptionCsFilter() != ""){				
			werte.add(getDescriptionCsFilter());
			spalte.add("t.descriptionCs");			
		}		
		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			customerstateList.clear();
			
			customerstateList.addAll((ArrayList<TblCustomerstate>) entityLister.getFilterList(TblCustomerstate.class, "TblCustomerstate", "", werte, spalte));
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		} catch (Exception e) {
			init();
			
			/*Deletes the current Table*/
			customerstateList.clear();
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		}	
	}
	
	/**
	 * Method to reset filter
	 */
	public void resetFilter(){
		setDescriptionCsFilter("");
	}

	/**
	 * Render Popup to create new Customerstate
	 */
	@Override
	public void changePopupRenderNew() {
		setCurCustomerstate(new TblCustomerstate());
		getCurCustomerstate().setIdCustomerstate(0);
		
		changePopupRender();
	}

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {
		
		/* If no customerstate is selected, curCustomerstate is set to selected
		 * otherwise the customerstate is unselected
		 */
		if(getCurCustomerstate() != null){			
			if(getCurCustomerstate().getIdCustomerstate() == customerstateList.get(re.getRow()).getIdCustomerstate()){
				setCurCustomerstate(new TblCustomerstate());
				setVisible(false);
			}else {
				setCurCustomerstate(customerstateList.get(re.getRow()));
				setVisible(true);
			}
		}
	}

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curCustomerstate
	 */
	@Override
	public void init() {
		setCurCustomerstate(new TblCustomerstate());
		getCurCustomerstate().setIdCustomerstate(0);
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
		visible = !(getCurCustomerstate().getIdCustomerstate() == 0);
		customerstateList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}

	/**
	 * Method to delete currently selected entity
	 */
	@Override
	public void deleteEntity() {
		entityLister.DeleteObject(getCurCustomerstate().getIdCustomerstate(), TblCustomerstate.class);
		resetCustomerstateCombobox();
		refresh();
	}

	/**
	 * Method to update currently selected entity
	 */
	@Override
	public void updateEntity() {
		entityLister.UpdateObject(TblCustomerstate.class, getCurCustomerstate(), getCurCustomerstate().getIdCustomerstate());
		resetCustomerstateCombobox();
		refresh();
	}

	public void setCurCustomerstate(TblCustomerstate curCustomerstate) {
		this.curCustomerstate = curCustomerstate;
	}

	public TblCustomerstate getCurCustomerstate() {
		return curCustomerstate;
	}

	public void setDescriptionCsFilter(String descriptionCsFilter) {
		this.descriptionCsFilter = descriptionCsFilter;
	}

	public String getDescriptionCsFilter() {
		return descriptionCsFilter;
	}
	
	/**
	 * Method to reset CustomerstateCombobox used in customerpopup
	 */
	public void resetCustomerstateCombobox(){
		/* Reading values of customerLister out of requestMap (Map with all created Managed Beans */
		CustomerBackingBean customerLister = (CustomerBackingBean) 
											 FacesContext.getCurrentInstance()
											 .getExternalContext().getRequestMap().get("customerLister");

		if(customerLister != null){
			customerLister.resetCustomerstateCombobox();
		}	
	}
}