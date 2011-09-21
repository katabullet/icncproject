package at.icnc.om.backingbeans;

import java.util.ArrayList;
import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of incometypeLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class IncometypeBackingBean extends AbstractBean implements Filterable {
	
	// Variable to save selected Incometype
	private TblIncometype curIncometype = new TblIncometype();
	
	// List with all Incometypes to avoid constant DB-Reading
	private ArrayList<TblIncometype> incometypeList;
	
	/**
	 * This functions returns a list with all incometypes
	 * @return incometypeList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblIncometype> getIncometypeList(){		
		
		/* Incometypes are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (incometypeList == null) {
			incometypeList = new ArrayList<TblIncometype>();
			incometypeList.addAll((ArrayList<TblIncometype>) 
					entityLister.getObjectList(TblIncometype.class));
		}
		
		/* Makes the user know which incometype is selected (it is shaded) */
		if (curIncometype != null) {
			for(TblIncometype curItem : incometypeList) {				
				if(curItem.getIdIncometype() == curIncometype.getIdIncometype()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return incometypeList;
	}		

	/*
	 * Filter
	 * @see at.icnc.om.interfaces.Filterable#filterEntities()
	 */
		private String descriptionIt;
		
		/* Setter of descriptionIt */
		public void setDescriptionIt(String descriptionIt) {
			this.descriptionIt = descriptionIt;
		}

		/* Getter of descriptionIt */
		public String getDescriptionIt() {
			return descriptionIt;
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
			if(descriptionIt != null && descriptionIt !=""){
				
				werte.add(descriptionIt);
				spalte.add("t.descriptionIt");
			}
			
			/*End Methods which check if the Fields are set and add them to the ArrayLists*/
			
			/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
			try {
				/*Deletes the current Table*/
				incometypeList.clear();
				
				incometypeList.addAll((ArrayList<TblIncometype>)entityLister.getFilterList(TblIncometype.class,"TblIncometype", joinStatement,werte, spalte));
				
				/*Method to close the Popup*/
				changeFilterPopupRender();
			} catch (Exception e) {
				init();
				
				/*Deletes the current Table*/
				incometypeList.clear();
				
				/*Method to close the Popup*/
				changeFilterPopupRender();
			}	
		}
		
		

	/**
	 * Method to open popup to create new Incometype
	 */
	@Override
	public void changePopupRenderNew() {
		curIncometype = new TblIncometype();
		curIncometype.setIdIncometype(0);
		changePopupRender();
	}

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {
		/* If no incometype is selected, curIncometype is set to selected
		 * otherwise the incometype is unselected
		 */
		if(curIncometype != null){			
			if(curIncometype.getIdIncometype() == incometypeList.get(re.getRow()).getIdIncometype()){
				curIncometype = new TblIncometype();
				setVisible(false);
			}else {
				curIncometype = incometypeList.get(re.getRow());
				setVisible(true);
			}
		}
	}

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curIncometype
	 */
	@Override
	public void init() {
		curIncometype = new TblIncometype();
		curIncometype.setIdIncometype(0);
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
		visible = !(curIncometype.getIdIncometype() == 0);
		incometypeList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}

	/**
	 * Deletes currently selected Incometype
	 */
	@Override
	public void deleteEntity() {
		entityLister.DeleteObject(curIncometype.getIdIncometype(), TblIncometype.class);
		curIncometype = new TblIncometype();
		curIncometype.setIdIncometype(0);
		refresh();
	}

	/**
	 * Updates currently selected Incometype
	 */
	@Override
	public void updateEntity() {
		entityLister.UpdateObject(TblIncometype.class, curIncometype, curIncometype.getIdIncometype());
		refresh();
	}
	
	/**
	 * Deletes the current filter
	**/
	public void clearFilter(){
		
	}	
	
	/* Setter of curIncometype */
	public void setCurIncometype(TblIncometype incometype){
		this.curIncometype = incometype;
	}
	
	/* Getter of curIncometype */
	public TblIncometype getCurIncometype(){
		return curIncometype;
	}
}
