package at.icnc.om.backingbeans;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import at.icnc.om.entitybeans.TblCostcentre;
import at.icnc.om.entitybeans.TblUser;
import at.icnc.om.interfaces.Filterable;

public class CostcentreBackingBean extends AbstractBean implements Filterable {
	
	// List with all Costcenters to avoid constant DB-Reading
	private ArrayList<TblCostcentre> costcentreList;
	
	// Variable to save current Costcentre
	private TblCostcentre curCostcentre = new TblCostcentre();
	
	/* Field declaration for Filter */
	private String descriptionCcFilter;
	private String costcentrecodeFilter;
	
	/**
	 * This functions returns a list with all Costcentres
	 * @return costcentreList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblCostcentre> getCostcentreList(){		
		
		/* Costcentres are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (costcentreList == null) {
			costcentreList = new ArrayList<TblCostcentre>();
			costcentreList.addAll((ArrayList<TblCostcentre>) 
					entityLister.getObjectList(TblCostcentre.class));
		}
		
		/* Makes the user know which Costcentre is selected (it is shaded) */
		if (getCurCostcentre() != null) {
			for(TblCostcentre curItem : costcentreList) {				
				if(curItem.getIdCostcentre() ==  getCurCostcentre().getIdCostcentre()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return costcentreList;
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
		if(getCostcentrecodeFilter() != null && getCostcentrecodeFilter() != ""){				
			werte.add(getCostcentrecodeFilter());
			spalte.add("t.costcentrecode");			
		}
		
		if(getDescriptionCcFilter() != null && getDescriptionCcFilter() != ""){				
			werte.add(getDescriptionCcFilter());
			spalte.add("t.descriptionCc");			
		}		
		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			costcentreList.clear();
			
			costcentreList.addAll((ArrayList<TblCostcentre>) entityLister.getFilterList(TblCostcentre.class, "TblCostcentre", "", werte, spalte));
			
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
		setDescriptionCcFilter("");
		setCostcentrecodeFilter("");
	}

	/**
	 * Render Popup to create new Costcentre
	 */
	@Override
	public void changePopupRenderNew() {
		setCurCostcentre(new TblCostcentre());
		getCurCostcentre().setIdCostcentre(0);
		
		changePopupRender();
	}

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {
		
		/* If no costcentre is selected, curCostcentre is set to selected
		 * otherwise the costcentre is unselected
		 */
		if(getCurCostcentre() != null){			
			if(getCurCostcentre().getIdCostcentre() == costcentreList.get(re.getRow()).getIdCostcentre()){
				setCurCostcentre(new TblCostcentre());
				setVisible(false);
			}else {
				setCurCostcentre(costcentreList.get(re.getRow()));
				setVisible(true);
			}
		}
	}

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curCostcentre
	 */
	@Override
	public void init() {
		setCurCostcentre(new TblCostcentre());
		getCurCostcentre().setIdCostcentre(0);
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
		visible = !(getCurCostcentre().getIdCostcentre() == 0);
		costcentreList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}

	/**
	 * Method to delete currently selected entity
	 */
	@Override
	public void deleteEntity() {
		entityLister.DeleteObject(getCurCostcentre().getIdCostcentre(), TblCostcentre.class);
		insertProtocol(TblCostcentre.class, getCurCostcentre().getIdCostcentre(), deleteAction);
		refresh();
	}

	/**
	 * Method to update currently selected entity
	 */
	@Override
	public void updateEntity() {
		boolean entityNew = (getCurCostcentre().getIdCostcentre() == 0);
		try {
			entityLister.UpdateObject(TblCostcentre.class, getCurCostcentre(), getCurCostcentre().getIdCostcentre());
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
			insertProtocol(TblCostcentre.class, getCurCostcentre().getIdCostcentre(), createAction);
		}else {
			insertProtocol(TblCostcentre.class, getCurCostcentre().getIdCostcentre(), updateAction);
		}
		refresh();
	}

	public void setCurCostcentre(TblCostcentre curCostcentre) {
		this.curCostcentre = curCostcentre;
	}

	public TblCostcentre getCurCostcentre() {
		return curCostcentre;
	}

	public void setDescriptionCcFilter(String descriptionCcFilter) {
		this.descriptionCcFilter = descriptionCcFilter;
	}

	public String getDescriptionCcFilter() {
		return descriptionCcFilter;
	}

	public void setCostcentrecodeFilter(String costcentrecodeFilter) {
		this.costcentrecodeFilter = costcentrecodeFilter;
	}

	public String getCostcentrecodeFilter() {
		return costcentrecodeFilter;
	}
}
