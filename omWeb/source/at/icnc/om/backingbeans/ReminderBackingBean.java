package at.icnc.om.backingbeans;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.interfaces.Filterable;

import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * Implementation of invoiceLister
 * 
 * @author csh80, nkn80, cma80
 * 
 */
public class ReminderBackingBean extends AbstractBean implements Filterable {

	// Variable to save selected Reminder
	private TblInvoice curReminder = new TblInvoice();

	// List with all Reminders to avoid constant DB-Reading
	private ArrayList<TblInvoice> reminderList;

	// List with all invoicestates
	ArrayList<TblInvoicestate> invoicestates;

	// Variable to save selected invoicestate
	private TblInvoicestate curInvoicestate;

	// Binding of SelectOneMenu with Invoicestate
	private HtmlSelectOneMenu bindingInvoicestate;
	
	private Integer anzahl=0;

	/*
	 * ______________________________________________________________________________
	 * EntityLister functions
	 */

	/**
	 * This functions returns a list with all reminders
	 * 
	 * @return reminderList
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TblInvoice> getReminderList() {
		String status = "Erledigt";
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> Spalte = new ArrayList<String>();
		ArrayList<String> Werte = new ArrayList<String>();

		Spalte.add("t.duedate");
		Spalte.add("i.descriptionIs");

		Werte.add(format.format(new Date()));
		Werte.add(status);

		/*
		 * Try and Catch: Try to call the "getFilterList" Method and get the
		 * FilterList or catch to call the "init" Method
		 */
		try {
			if (reminderList == null) {
				reminderList = new ArrayList<TblInvoice>();
				reminderList.addAll((ArrayList<TblInvoice>) entityLister
						.getReminderList(Werte, Spalte));
			}

		} catch (Exception e) {

			init();
		}

		
		return reminderList;
	}

	/**
	 * init Method resets all lists with db-content closes all popups goes back
	 * to first page resets curInvoice
	 */
	@Override
	public void init() {
		paginator.gotoFirstPage();
		resetFilter();
		refresh();
	}

	/**
	 * Lists with DB-content are set NULL to make sure they are read from DB
	 */
	@Override
	public void refresh() {
		reminderList = null;
		invoicestate = null;
		filterpopupRender = false;
		popupRender = false;
		setDeletePopupRender(false);
		visible = false;

	}

	// Fields for the Filter
	private Integer invoicenumberFrom;
	private Integer invoicenumberTo;
	private String customername;
	private Date duedateFrom;
	private Date duedateTo;
	private String invoicestate;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Format format = new SimpleDateFormat("yyyy-MM-dd");

	public void setInvoicenumberFrom(Integer invoicenumberFrom) {
		this.invoicenumberFrom = invoicenumberFrom;
	}

	public Integer getInvoicenumberFrom() {
		return invoicenumberFrom;
	}

	public void setInvoicenumberTo(Integer invoicenumberTo) {
		this.invoicenumberTo = invoicenumberTo;
	}

	public Integer getInvoicenumberTo() {
		return invoicenumberTo;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomername() {
		return customername;
	}

	public void setDuedateFrom(Date duedateFrom) {
		this.duedateFrom = duedateFrom;
	}

	public Date getDuedateFrom() {
		return duedateFrom;
	}

	public void setDuedateTo(Date duedateTo) {
		this.duedateTo = duedateTo;
	}

	public Date getDuedateTo() {
		return duedateTo;
	}

	public void setInvoicestate(String invoicestate) {
		this.invoicestate = invoicestate;
	}

	public String getInvoicestate() {
		return invoicestate;
	}

	@Override
	public void filterEntities() {
		getReminderList();

		// Filter List
		ArrayList<TblInvoice> reminderFilterList = new ArrayList<TblInvoice>();

		/*
		 * Start Methods which check if the Fields are set and add them to the
		 * ArrayLists
		 */
		if (invoicenumberFrom != null && invoicenumberFrom != 0
				|| invoicenumberTo != null && invoicenumberTo != 0) {

			/* Start - Set a default Value if none is set */
			if (invoicenumberFrom == null)
				invoicenumberFrom = 0;
			if (invoicenumberTo == null)
				invoicenumberTo = 999999;
			/* End - Set a default Value if none is set */

			for (TblInvoice item : reminderList) {

				if (Integer.parseInt(item.getInvoicenumber()) >= invoicenumberFrom
						&& Integer.parseInt(item.getInvoicenumber()) <= invoicenumberTo) {
					reminderFilterList.add(item);
				}
			}
			reminderList.clear();
			reminderList.addAll(reminderFilterList);
		}

		if (customername != null && customername != "" && customername != "*") {

			if (!reminderFilterList.isEmpty()) {
				reminderFilterList.clear();
			}

			for (TblInvoice item : reminderList) {

				if (customername.contains("*")) {
					if (customername.startsWith("*")
							&& !customername.endsWith("*")) {
						customername = customername.substring(1,
								customername.length());
						if (item.getTblSettlement().getTblOrder()
								.getTblCustomer().getCustomername()
								.endsWith(customername)) {
							reminderFilterList.add(item);
						}
						customername = "*" + customername;
					} else if (customername.endsWith("*")
							&& !customername.startsWith("*")) {
						customername = customername.substring(0,
								customername.length() - 1);
						if (item.getTblSettlement().getTblOrder()
								.getTblCustomer().getCustomername()
								.startsWith(customername)) {
							reminderFilterList.add(item);
						}
						customername = customername + "*";
					} else if (customername.endsWith("*")
							&& customername.startsWith("*")) {
						customername = customername.substring(1,
								customername.length() - 1);
						if (item.getTblSettlement().getTblOrder()
								.getTblCustomer().getCustomername()
								.contains(customername)) {
							reminderFilterList.add(item);
						}
						customername = "*" + customername + "*";

					} else {
						String[] filtervalues = new String[2];
						filtervalues[0] = customername.substring(0,
								customername.indexOf("*"));
						filtervalues[1] = customername.substring(
								customername.indexOf("*") + 1,
								customername.length());

						if (item.getTblSettlement().getTblOrder()
								.getTblCustomer().getCustomername()
								.startsWith(filtervalues[0])
								&& item.getTblSettlement().getTblOrder()
										.getTblCustomer().getCustomername()
										.endsWith(filtervalues[1])) {
							reminderFilterList.add(item);
						}
					}
				} else {
					if (item.getTblSettlement().getTblOrder().getTblCustomer()
							.getCustomername().contains(customername)) {
						reminderFilterList.add(item);
					}

				}

			}
			reminderList.clear();
			reminderList.addAll(reminderFilterList);

		}

		if (duedateFrom != null || duedateTo != null) {

			if (!reminderFilterList.isEmpty()) {
				reminderFilterList.clear();
			}

			/* Start - Set a default Value if none is set */
			if (duedateFrom == null)
				try {
					duedateFrom = dateFormat.parse("1999-01-01");
				} catch (ParseException e) {
				}
			if (duedateTo == null)
				try {
					duedateTo = dateFormat.parse("3000-01-01");
				} catch (ParseException e) {
				}

			for (TblInvoice item : reminderList) {
				if ((item.getDuedate().after(duedateFrom) && item.getDuedate()
						.before(duedateTo))
						|| item.getDuedate().compareTo(duedateFrom) == 0
						|| item.getDuedate().compareTo(duedateTo) == 0) {
					reminderFilterList.add(item);

				}
			}
			reminderList.clear();
			reminderList.addAll(reminderFilterList);
		}

		if (invoicestate != null && invoicestate != "") {

			if (!reminderFilterList.isEmpty()) {
				reminderFilterList.clear();
			}

			for (TblInvoice item : reminderList) {
				if (item.getTblInvoicestate().getDescriptionIs()
						.contains(invoicestate)) {
					reminderFilterList.add(item);
				}
			}
			reminderList.clear();
			reminderList.addAll(reminderFilterList);
		}
		changeFilterPopupRender();

	}

	/**
	 * Function to create SelectItems of all Incometypes plus an empty one
	 * Important for combobox (needs SelectItem, not objects of incometype)
	 * 
	 * @return List of SelectItem
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getinvoicestateListDescription() {
		ArrayList<SelectItem> invoicestateDescription = new ArrayList<SelectItem>();

		ArrayList<TblInvoicestate> invoicestate = new ArrayList<TblInvoicestate>();
		invoicestate
				.addAll((Collection<? extends TblInvoicestate>) entityLister
						.getObjectList(TblInvoicestate.class));
		/* An empty Item is added to SelectItem-List */
		invoicestateDescription.add(new SelectItem());
		for (TblInvoicestate item : invoicestate) {
			/* Description of each invoicestate is added to SelectItem-List */
			invoicestateDescription
					.add(new SelectItem(item.getDescriptionIs()));
		}
		return invoicestateDescription;
	}

	/**
	 * Function to create SelectItems of all Invoicestates plus an empty one
	 * Important for combobox (needs SelectItem, not objects of invoicestates)
	 * 
	 * @return List of SelectItem
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SelectItem> getinvoicestateListDescriptionFilter() {
		ArrayList<SelectItem> invoicestate = new ArrayList<SelectItem>();

		ArrayList<TblInvoicestate> iState = new ArrayList<TblInvoicestate>();
		iState.addAll((ArrayList<TblInvoicestate>) entityLister
				.getObjectList(TblInvoicestate.class));
		/* An empty Item is added to SelectItem-List */
		invoicestate.add(new SelectItem());
		for (TblInvoicestate item : iState) {
			/* Description of each invoicestate is added to SelectItem-List */
			invoicestate.add(new SelectItem(item.getDescriptionIs()));
		}
		return invoicestate;
	}

	/**
	 * Method that Listens to Change Event of a combobox if another element in
	 * the combobox is selected, the value in curInvoicestate is set to the
	 * selected one
	 * 
	 * @param vce
	 */
	public void changeInvoicestate(ValueChangeEvent vce) {
		setCurInvoicestate((TblInvoicestate) entityLister.getSingleObject(
				"SELECT * FROM OMinvoicestate WHERE description_is = '"
						+ vce.getNewValue().toString() + "'",
				TblInvoicestate.class));
	}

	/**
	 * Method that Listens to Change Event of a combobox if another element in
	 * the combobox is selected, the value in curInvoicestate is set to the
	 * selected one
	 * 
	 * @param vce
	 */
	public void changeInvoicestateFilter(ValueChangeEvent vce) {
		setCurInvoicestate((TblInvoicestate) entityLister.getSingleObject(
				"SELECT * FROM OMinvoicestate WHERE description_is = '"
						+ vce.getNewValue().toString() + "'",
				TblInvoicestate.class));
	}

	/* Setter of curInvoicestate */
	public void setCurInvoicestate(TblInvoicestate curInvoicestate) {
		this.curInvoicestate = curInvoicestate;
	}

	/* Getter of curInvoicestate */
	public TblInvoicestate getCurInvoicestate() {
		return curInvoicestate;
	}

	@Override
	public void changePopupRenderNew() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rowEvent(RowSelectorEvent re) {

		/*
		 * If no invoices is selected, curInvoice is set to selected otherwise
		 * the invoice is unselected
		 */
		if (getCurReminder() != null) {
			if (getCurReminder().getIdInvoice() == reminderList
					.get(re.getRow()).getIdInvoice()) {
				setCurReminder(new TblInvoice());
				setVisible(false);
			} else {
				setCurReminder(reminderList.get(re.getRow()));
				setVisible(true);
			}
		}

	}

	@Override
	public void deleteEntity() {

	}

	@Override
	public void updateEntity() {
		curReminder.setTblInvoicestate(curInvoicestate);
		try {
			entityLister.UpdateObject(TblInvoice.class, curReminder, curReminder.getIdInvoice());
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
		insertProtocol(TblInvoice.class, getCurReminder().getIdInvoice(), updateAction);
		refresh();
	}

	public void resetFilter() {
		try {
			invoicenumberFrom = null;
			invoicenumberTo = null;
			customername = "";
			duedateFrom = dateFormat.parse("1999-01-01");
			duedateTo = dateFormat.parse("3000-01-01");
			invoicestate = "";
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setCurReminder(TblInvoice curReminder) {
		this.curReminder = curReminder;
	}

	public TblInvoice getCurReminder() {
		return curReminder;
	}

	public void setBindingInvoicestate(HtmlSelectOneMenu bindingInvoicestate) {
		this.bindingInvoicestate = bindingInvoicestate;
	}

	public HtmlSelectOneMenu getBindingInvoicestate() {
		return bindingInvoicestate;
	}

	public void setAnzahl(Integer anzahl) {
		this.anzahl = anzahl;
	}

	public Integer getAnzahl() {
		
		String status = "Erledigt";
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		ArrayList<String> Spalte = new ArrayList<String>();
		ArrayList<String> Werte = new ArrayList<String>();

		Spalte.add("t.duedate");
		Spalte.add("i.descriptionIs");

		Werte.add(format.format(new Date()));
		Werte.add(status);
		
		try {
				ArrayList<TblInvoice>reminderListanzahl = new ArrayList<TblInvoice>();
				reminderListanzahl.addAll((ArrayList<TblInvoice>) entityLister
						.getReminderList(Werte, Spalte));
				anzahl = reminderListanzahl.size();

		} catch (Exception e) {
		}
		
		return anzahl;
	}
}