package at.icnc.om.backingbeans;


import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import at.icnc.om.entitybeans.TblConcern;
import at.icnc.om.entitybeans.TblContactperson;
import at.icnc.om.entitybeans.TblCustomer;
import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblSettlement;
import at.icnc.om.interfaces.EntityListerLocal;
import at.icnc.om.interfaces.Filterable;
import at.icnc.om.interfaces.Refreshable;

import com.icesoft.faces.component.datapaginator.DataPaginator;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of contactpersonLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class ContactpersonBackingBean extends AbstractBean implements Filterable {

	// List with all Contactpersons to avoid constant DB-Reading
	private ArrayList<TblContactperson> contactpersonList;
	
	// Variable to save currently selected Contactperson
	private TblContactperson curContactperson = new TblContactperson();
	
	/* Field declaration for Filter */
	private String firstnameFilter;
	private String lastnameFilter;
	private String telephoneFilter;
	private String emailFilter;
	
	/**
	 * This functions returns a list with all contactpersons
	 * @return contactpersonList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblContactperson> getContactpersonList(){		
		
		/* Contactpersons are only read out of DB if they were not read before 
		 * avoids unnecessary data traffic 
		 */
		if (contactpersonList == null) {
			contactpersonList = new ArrayList<TblContactperson>();
			contactpersonList.addAll((ArrayList<TblContactperson>) 
					entityLister.getObjectList(TblContactperson.class));
		}
		
		/* Makes the user know which contactperson is selected (it is shaded) */
		if (getCurContactperson() != null) {
			for(TblContactperson curItem : contactpersonList) {				
				if(curItem.getIdContactperson() == getCurContactperson().getIdContactperson()) {
					curItem.setSelected(true);
				}
			}
		}		
		
		return contactpersonList;
	}	
	
	/**
	 * Method to filter Contactpersons
	 */
	@Override
	public void filterEntities() {
		/*ArrayList which managed the filter values*/
		ArrayList<String> werte = new ArrayList<String>();
		/*ArrayList which managed the filter columns*/
		ArrayList<String> spalte= new ArrayList<String>();
		
		/*Start Methods which check if the Fields are set and add them to the ArrayLists*/
		if(getFirstnameFilter() != null && getFirstnameFilter() != ""){				
			werte.add(getFirstnameFilter());
			spalte.add("t.firstname");			
		}
		
		if(getLastnameFilter() != null && getLastnameFilter() != ""){				
			werte.add(getLastnameFilter());
			spalte.add("t.lastname");			
		}
		
		if(getTelephoneFilter() != null && getTelephoneFilter() != ""){				
			werte.add(getTelephoneFilter());
			spalte.add("t.telephone");			
		}
		
		if(getEmailFilter() != null && getEmailFilter() != ""){				
			werte.add(getEmailFilter());
			spalte.add("t.email");			
		}
		/*End Methods which check if the Fields are set and add them to the ArrayLists*/
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			/*Deletes the current Table*/
			contactpersonList.clear();
			
			contactpersonList.addAll((ArrayList<TblContactperson>) entityLister.getFilterList(TblContactperson.class, "TblContactperson", "", werte, spalte));
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		} catch (Exception e) {
			init();
			
			/*Deletes the current Table*/
			contactpersonList.clear();
			
			/*Method to close the Popup*/
			changeFilterPopupRender();
		}	
	}
	
	/**
	 * Method to reset filter
	 */
	public void resetFilter(){
		setFirstnameFilter("");
		setLastnameFilter("");
		setTelephoneFilter("");
		setEmailFilter("");
	}

	/**
	 * Method to open popup to create new contactperson
	 */
	@Override
	public void changePopupRenderNew() {
		setCurContactperson(new TblContactperson());
		getCurContactperson().setIdContactperson(0);
		
		changePopupRender();
	}

	/**
	 * This method captures a RowSelectorEvent
	 * It is called when clicked on element in DataTable
	 * @param re
	 */
	@Override
	public void rowEvent(RowSelectorEvent re) {
		
		/* If no contactperson is selected, curContactperson is set to selected
		 * otherwise the contactperson is unselected
		 */
		if(getCurContactperson() != null){			
			if(getCurContactperson().getIdContactperson() == contactpersonList.get(re.getRow()).getIdContactperson()){
				setCurContactperson(new TblContactperson());
				setVisible(false);
			}else {
				setCurContactperson(contactpersonList.get(re.getRow()));
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
		setCurContactperson(new TblContactperson());
		getCurContactperson().setIdContactperson(0);
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
		visible = !(getCurContactperson().getIdContactperson() == 0);
		contactpersonList = null;
		popupRender = false;
		filterpopupRender = false;
		setDeletePopupRender(false);
	}

	/**
	 * Method to delete currently selected entity
	 */
	@Override
	public void deleteEntity() {
		entityLister.DeleteObject(getCurContactperson().getIdContactperson(), TblContactperson.class);
		refresh();
	}

	/**
	 * Method to update currently selected entity
	 */
	@Override
	public void updateEntity() {
		entityLister.UpdateObject(TblContactperson.class, getCurContactperson(), getCurContactperson().getIdContactperson());
		refresh();
	}

	public void setCurContactperson(TblContactperson curContactperson) {
		this.curContactperson = curContactperson;
	}

	public TblContactperson getCurContactperson() {
		return curContactperson;
	}

	public void setFirstnameFilter(String firstnameFilter) {
		this.firstnameFilter = firstnameFilter;
	}

	public String getFirstnameFilter() {
		return firstnameFilter;
	}

	public void setLastnameFilter(String lastnameFilter) {
		this.lastnameFilter = lastnameFilter;
	}

	public String getLastnameFilter() {
		return lastnameFilter;
	}

	public void setTelephoneFilter(String telephoneFilter) {
		this.telephoneFilter = telephoneFilter;
	}

	public String getTelephoneFilter() {
		return telephoneFilter;
	}

	public void setEmailFilter(String emailFilter) {
		this.emailFilter = emailFilter;
	}

	public String getEmailFilter() {
		return emailFilter;
	}

}