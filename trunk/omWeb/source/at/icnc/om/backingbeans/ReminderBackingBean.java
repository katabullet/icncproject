package at.icnc.om.backingbeans;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblUser;
import at.icnc.om.entitybeans.TblUserrole;
import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of invoiceLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class ReminderBackingBean extends AbstractBean {
	
	// List with all Reminders to avoid constant DB-Reading
	private ArrayList<TblInvoice> reminderList;

	/*
	 * ______________________________________________________________________________
	 * EntityLister functions
	 */
	
	/**
	 * This functions returns a list with all reminders
	 * @return reminderList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblInvoice> getReminderList(){	
		String status = "Erledigt";
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> Spalte = new ArrayList<String>();
		ArrayList<String> Werte = new ArrayList<String>();
		
		Spalte.add("t.duedate");
		Spalte.add("i.descriptionIs");
		
		Werte.add(format.format(new Date()));
		Werte.add(status);
		
		/*Try and Catch: Try to call the "getFilterList" Method and get the FilterList or catch to call the "init" Method*/
		try {
			reminderList = new ArrayList<TblInvoice>();		
			reminderList.addAll((ArrayList<TblInvoice>)entityLister.getReminderList(Werte, Spalte));

		} catch (Exception e) {

			init();			
		}	
		
		return reminderList;
	}	
	
	/**
	 * init Method
	 * resets all lists with db-content
	 * closes all popups
	 * goes back to first page
	 * resets curInvoice
	 */
	@Override
	public void init() {		
		paginator.gotoFirstPage();
		refresh();
	}

	/**
	 * Lists with DB-content are set NULL to
	 * make sure they are read from DB
	 */	
	@Override
	public void refresh() {
		reminderList = new ArrayList<TblInvoice>();
	}
	
	@Override
	public void changePopupRenderNew() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rowEvent(RowSelectorEvent re) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEntity() {
		// TODO Auto-generated method stub
		
	}	
	
}