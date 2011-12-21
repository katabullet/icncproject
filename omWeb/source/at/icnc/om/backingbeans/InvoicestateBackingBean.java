package at.icnc.om.backingbeans;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.ext.RowSelectorEvent;

import at.icnc.om.entitybeans.TblCustomerstate;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.entitybeans.TblUser;
import at.icnc.om.interfaces.EntityListerLocal;
import at.icnc.om.interfaces.Filterable;
import at.icnc.om.interfaces.TblInvoicestateLocal;

public class InvoicestateBackingBean extends AbstractBean implements Filterable {
	
	// List with all Invoicestates to avoid constant DB-Reading
	private ArrayList<TblInvoicestate> invoicestateList;
	
	// Variable to save current Invoicestate
	private TblInvoicestate curInvoicestate = new TblInvoicestate();
	
	/* Field declaration for Filter */
	private String descriptionIsFilter;
	
	/**
	 * This functions returns a list with all Invoicestates
	 * @return invoicestateList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblInvoicestate> getInvoicestateList(){		
		
		/* Invoicestates are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (invoicestateList == null) {
			invoicestateList = new ArrayList<TblInvoicestate>();
			invoicestateList.addAll((ArrayList<TblInvoicestate>) 
					entityLister.getObjectList(TblInvoicestate.class));
		}
		
		/* Makes the user know which invoicestate is selected (it is shaded) */
		if (getCurInvoicestate() != null) {
			for(TblInvoicestate curItem : invoicestateList) {				
				if(curItem.getIdInvoicestate() ==  getCurInvoicestate().getIdInvoicestate()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return invoicestateList;
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
		if(getDescriptionIsFilter() != null && getDescriptionIsFilter() != ""){				
			werte.add(getDescriptionIsFilter());
			spalte.add("t.descriptionIs");			
		}		
		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			invoicestateList.clear();
			
			invoicestateList.addAll((ArrayList<TblInvoicestate>) entityLister.getFilterList(TblInvoicestate.class, "TblInvoicestate", "", werte, spalte));
			
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
		setDescriptionIsFilter("");
	}

	/**
	 * Render Popup to create new Invoicestate
	 */
	@Override
	public void changePopupRenderNew() {
		setCurInvoicestate(new TblInvoicestate());
		getCurInvoicestate().setIdInvoicestate(0);
		
		changePopupRender();
	}

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {
		
		/* If no invoicestate is selected, curInvoicestate is set to selected
		 * otherwise the invoicestate is unselected
		 */
		if(getCurInvoicestate() != null){			
			if(getCurInvoicestate().getIdInvoicestate() == invoicestateList.get(re.getRow()).getIdInvoicestate()){
				setCurInvoicestate(new TblInvoicestate());
				setVisible(false);
			}else {
				setCurInvoicestate(invoicestateList.get(re.getRow()));
				setVisible(true);
			}
		}
	}

	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curInvoicestate
	 */
	@Override
	public void init() {
		setCurInvoicestate(new TblInvoicestate());
		getCurInvoicestate().setIdInvoicestate(0);
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
		visible = !(getCurInvoicestate().getIdInvoicestate() == 0);
		invoicestateList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}

	/**
	 * Method to delete currently selected entity
	 */
	@Override
	public void deleteEntity() {
		entityLister.DeleteObject(getCurInvoicestate().getIdInvoicestate(), TblInvoicestate.class);
		insertProtocol(TblInvoicestate.class, getCurInvoicestate().getIdInvoicestate(), deleteAction);
		refresh();
	}

	/**
	 * Method to update currently selected entity
	 */
	@Override
	public void updateEntity() {
		boolean entityNew = (getCurInvoicestate().getIdInvoicestate() == 0);
		try {
			entityLister.UpdateObject(TblInvoicestate.class, getCurInvoicestate(), getCurInvoicestate().getIdInvoicestate());
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
			insertProtocol(TblInvoicestate.class, getCurInvoicestate().getIdInvoicestate(), createAction);
		}else {
			insertProtocol(TblInvoicestate.class, getCurInvoicestate().getIdInvoicestate(), updateAction);
		}	
		
		resetInvoicestateCombobox();
		refresh();
	}

	public void setCurInvoicestate(TblInvoicestate curInvoicestate) {
		this.curInvoicestate = curInvoicestate;
	}

	public TblInvoicestate getCurInvoicestate() {
		return curInvoicestate;
	}

	public void setDescriptionIsFilter(String descriptionIsFilter) {
		this.descriptionIsFilter = descriptionIsFilter;
	}

	public String getDescriptionIsFilter() {
		return descriptionIsFilter;
	}
	
	/**
	 * Method to reset InvoicestateCombobox used in invoicepopup
	 */
	public void resetInvoicestateCombobox(){
		/* Reading values of invoiceLister out of requestMap (Map with all created Managed Beans */
		InvoiceBackingBean invoiceLister = (InvoiceBackingBean) 
											 FacesContext.getCurrentInstance()
											 .getExternalContext().getRequestMap().get("invoiceLister");

		if(invoiceLister != null){
			invoiceLister.resetInvoicestateCombobox();
		}	
	}
}
