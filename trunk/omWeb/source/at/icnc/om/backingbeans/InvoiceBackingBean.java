package at.icnc.om.backingbeans;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.faces.component.datapaginator.PaginatorActionEvent;
import com.icesoft.faces.component.ext.RowSelectorEvent;

import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblSettlement;
import at.icnc.om.interfaces.EntityListerLocal;
import at.icnc.om.interfaces.Refreshable;
import at.icnc.om.interfaces.TblIncometypeLocal;
import at.icnc.om.interfaces.TblInvoiceLocal;
import at.icnc.om.interfaces.TblInvoicestateLocal;
import at.icnc.om.interfaces.TblOrderLocal;

public class InvoiceBackingBean implements Refreshable{
	@EJB
	TblInvoiceLocal tblInvoice;
	@EJB
	TblIncometypeLocal tblIncometype;
	@EJB
	EntityListerLocal entityLister;
	@EJB
	TblOrderLocal tblOrder;
	@EJB
	TblInvoicestateLocal tblInvoicestate;

	
	private TblInvoice curInvoice = new TblInvoice();
	private String filterColumn = "";
	private String filterValue = "";
	
	private DataPaginator paginator;
	
	Boolean visible = false;
	Boolean popupRender = false;
	Boolean filterpopupRender = false;
	private ArrayList<TblInvoice> invoiceList;
	
	private Date datum;
	
	
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public Date getDatum() {
		return datum;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<TblInvoice> getInvoiceList(){		
		//ArrayList<TblInvoice> invoices = new ArrayList<TblInvoice>();
		/*invoices.addAll(tblInvoice.getInvoiceList());
		//invoices.addAll(tblInvoice.getInvoiceList("Sum","5"));
		*/
		if(invoiceList == null){
			invoiceList = new ArrayList<TblInvoice>();
			invoiceList.addAll((ArrayList<TblInvoice>) 
					entityLister.getObjectList("SELECT * FROM OMinvoice", 
							TblInvoice.class));
		}
		
		if(curInvoice != null){
			for(TblInvoice curItem : invoiceList){				
				if(curItem.getIdInvoice() == curInvoice.getIdInvoice()){
					curItem.setSelected(true);
				}
			}
		}
		
		/* invoices.addAll((ArrayList<TblInvoice>) 
				entityLister.getObjectList("SELECT * FROM OMinvoice", 
						TblInvoice.class));
		
		if(curInvoice != null){
			for(TblInvoice curItem : invoices){				
				if(curItem.getIdInvoice() == curInvoice.getIdInvoice()){
					curItem.setSelected(true);
					//setCurInvoice(curItem);
				}
			}
		}*/
		
		//return invoices;
		return invoiceList;
	}	

	public void rowEvent(RowSelectorEvent re) {		
		/*if(getCurInvoice() != null){			
			if(getCurInvoice().getIdInvoice() == tblInvoice.getInvoiceList().get(re.getRow()).getIdInvoice()){
				setCurInvoice(new TblInvoice());
				visible = false;
			}else {
				setCurInvoice(tblInvoice.getInvoiceList().get(re.getRow()));
				visible = true;
			}
		}*/
		
		if(getCurInvoice() != null){			
			if(getCurInvoice().getIdInvoice() == invoiceList.get(re.getRow()).getIdInvoice()){
				setCurInvoice(new TblInvoice());
				visible = false;
			}else {
				setCurInvoice(invoiceList.get(re.getRow()));
				visible = true;
			}
		}
	}
	
	public Boolean getVisible(){		
		return visible;
	}


	public void setCurInvoice(TblInvoice curInvoice) {
		this.curInvoice = curInvoice;
	}

	public TblInvoice getCurInvoice() {
		return curInvoice;
	}	
	
	public Boolean getPopupRender(){
		return popupRender;
	}
	
	public Boolean getFilterPopupRender(){
		return filterpopupRender;
	}

	public void PopupRendernaendernNew(){
		/* setCurInvoice(new TblInvoice());

		//getCurInvoice().setIdInvoice(entityLister.NextID(TblInvoice.class));
		getCurInvoice().setDuedate(new Date());
		getCurInvoice().setTblSettlement((TblSettlement) entityLister.getSingleObject("SELECT * FROM OMSETTLEMENT WHERE id_settlement = 1", TblSettlement.class));
		getCurInvoice().getTblSettlement().setTblOrder((TblOrder) entityLister.getSingleObject("SELECT * FROM OMorder WHERE id_order = 1", TblOrder.class));
		getCurInvoice().setTblInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM OMinvoicestate WHERE id_invoicestate = 1", TblInvoicestate.class));
		
		getCurInvoice().getTblSettlement().setTblIncometype((TblIncometype) entityLister.getSingleObject("SELECT * FROM OMincometype WHERE id_incometype = 2", TblIncometype.class));

		getCurInvoice().setTblSettlement(new TblSettlement());
		TblIncometype income = new TblIncometype();
		List<TblIncometype> defaultWertIncometype = (List<TblIncometype>)entityLister.getObjectList("SELECT  * FROM OMINCOMETYPE", TblIncometype.class);
		income.setDescriptionIt(defaultWertIncometype.get(0).getDescriptionIt());
		getCurInvoice().getTblSettlement().setTblIncometype(income);
		//Defaultwert für Auftragsnummer setzen
		TblOrder order = new TblOrder();
		List<TblOrder> defaultListeOrder =(List<TblOrder>) entityLister.getObjectList("SELECT  * FROM OMorder", TblOrder.class);
		TblOrder defaultWertOrder = defaultListeOrder.get(defaultListeOrder.size()-1);
		order.setOrdernumber(defaultWertOrder.getOrdernumber());
		getCurInvoice().getTblSettlement().setTblOrder(order);
		//Defaultwert für Rechnungstatus setzen
		TblInvoicestate invoicestate = new TblInvoicestate();
		List<TblInvoicestate> defaultWertInvoicestate = (List<TblInvoicestate>)entityLister.getObjectList("SELECT  * FROM OMinvoicestate", TblInvoicestate.class);
		invoicestate.setDescriptionIs(defaultWertInvoicestate.get(0).getDescriptionIs());
		getCurInvoice().setTblInvoicestate(invoicestate);*/
		
		
		setCurInvoice(new TblInvoice());
		getCurInvoice().setIdInvoice(0);
		getCurInvoice().setTblSettlement((TblSettlement) entityLister.getSingleObject("SELECT * FROM omsettlement WHERE rownum <= 1", TblSettlement.class));
		getCurInvoice().setDuedate(new Date());
		getCurInvoice().setTblInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM ominvoicestate WHERE rownum <= 1", TblInvoicestate.class));
		getCurInvoice().getTblSettlement().setTblOrder((TblOrder) entityLister.getSingleObject("SELECT * FROM omorder WHERE rownum <= 1", TblOrder.class));
		
		
		PopupRendernaendern();
	}
	
	public void PopupRendernaendern(){
		popupRender = !popupRender;		
	}
	
	public void FilterpopupRendernaendern(){
		filterpopupRender = !filterpopupRender;		
	}
	
	public void DeleteInvoice(){
		//tblInvoice.DeleteInvoice(curInvoice.getIdInvoice());
		entityLister.DeleteObject(curInvoice.getIdInvoice(), TblInvoice.class);
		init();
	}
	
	public void UpdateInvoice(){
		curInvoice.setTblInvoicestate(curInvoicestate);
		entityLister.UpdateObject(TblInvoice.class, curInvoice, curInvoice.getIdInvoice());
		init();	
	}

	public void setFilterColumn(String filterColumn) {
		this.filterColumn = filterColumn;
	}

	public String getFilterColumn() {
		return filterColumn;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getFilterValue() {
		return filterValue;
	}
	
	// __________________________________________________________________________
	
	
	ArrayList<TblInvoicestate> invoicestates;
	private TblInvoicestate curInvoicestate;
	
	@SuppressWarnings("unchecked")
	private ArrayList<TblInvoicestate> getInvoicestateList(){
		/*invoicestates.clear();
		invoicestates.addAll(tblInvoicestate.getInvoicsestateList());
		return invoicestates;*/
		if(invoicestates == null){
			invoicestates = new ArrayList<TblInvoicestate>();
			invoicestates.addAll((Collection<? extends TblInvoicestate>) 
					entityLister.getObjectList("SELECT * FROM OMinvoicestate", 
							TblInvoicestate.class));
		}
		
		if(curInvoicestate != null){
			for(TblInvoicestate curItem : invoicestates){				
				if(curItem.getIdInvoicestate() == curInvoicestate.getIdInvoicestate()){
					curItem.setSelected(true);
				}
			}
		}else{
			curInvoicestate = invoicestates.get(0);
		}		
	
		return invoicestates;
	}	
	
	public ArrayList<SelectItem> getInvoicestateListDescription(){
		ArrayList<SelectItem> invoicsestates = new ArrayList<SelectItem>();
		for (TblInvoicestate item : getInvoicestateList()) {
			invoicsestates.add(new SelectItem(item.getDescriptionIs()));
		}
		return invoicsestates;			
	}
	
	public void InvoicestateChange(ValueChangeEvent vce){
		setCurInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM OMinvoicestate WHERE description_is = '" + 
				vce.getNewValue().toString() + "'", TblInvoicestate.class));				
	}

	public void setCurInvoicestate(TblInvoicestate curInvoicestate) {
		this.curInvoicestate = curInvoicestate;
	}

	public TblInvoicestate getCurInvoicestate() {
		return curInvoicestate;
	}
	
	public void DateChangeListener(ValueChangeEvent vce){
		getCurInvoice().setDuedate((Date) vce.getNewValue());
	}
	
	//___________________________________________________________________________
	
	@Override
	public void init() {
		refresh();
		setCurInvoice(new TblInvoice());
		getCurInvoice().setIdInvoice(0);
		visible = false;
		popupRender = false;
		filterpopupRender = false;
		paginator.gotoFirstPage();
	}

	@Override
	public void refresh() {
		invoiceList = null;
		invoicestates = null;	
	}

	public void setPaginator(DataPaginator paginator) {
		this.paginator = paginator;
	}

	public DataPaginator getPaginator() {
		return paginator;
	}
	
	public void ClosePopup(){
		init();
	}
	
	//___________________________________________________________________________
	//Filterpopup Methoden

	private String invoicenumber;
	private String sum;
	private String estimatate;
	private Date duedate;
	private String invoicestate;
	private String ordernumber;
	private String incometype;
	private String settlementnumber;
	
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public String getInvoicenumber() {
		return invoicenumber;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getSum() {
		return sum;
	}

	public void setEstimatate(String estimatate) {
		this.estimatate = estimatate;
	}

	public String getEstimatate() {
		return estimatate;
	}

	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}

	public Date getDuedate() {
		return duedate;
	}

	public void setInvoicestate(String invoicestate) {
		this.invoicestate = invoicestate;
	}

	public String getInvoicestate() {
		return invoicestate;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setIncometype(String incometype) {
		this.incometype = incometype;
	}

	public String getIncometype() {
		return incometype;
	}

	public void setSettlementnumber(String settlementnumber) {
		this.settlementnumber = settlementnumber;
	}

	public String getSettlementnumber() {
		return settlementnumber;
	}
	public ArrayList<SelectItem> getorderListDescription(){
		ArrayList<SelectItem> ordernumber = new ArrayList<SelectItem>();
	
		ArrayList<TblOrder> order = new ArrayList<TblOrder>();
		order.addAll((Collection<? extends TblOrder>) 
				entityLister.getObjectList("SELECT * FROM OMorder", 
						TblOrder.class));
		
		for (TblOrder item : order) {
			ordernumber.add(new SelectItem(item.getOrdernumber()));
		}
		return ordernumber;			
	}
	
	public ArrayList<SelectItem> getincometypeListDescription(){
		ArrayList<SelectItem> incometypeDescription = new ArrayList<SelectItem>();
	
		ArrayList<TblIncometype> incometype = new ArrayList<TblIncometype>();
		incometype.addAll((Collection<? extends TblIncometype>) 
				entityLister.getObjectList("SELECT * FROM OMincometype", 
						TblIncometype.class));
		
		for (TblIncometype item : incometype) {
			incometypeDescription.add(new SelectItem(item.getDescriptionIt()));
		}
		return incometypeDescription;			
	}
	
	public ArrayList<SelectItem> getsettlementListDescription(){
		ArrayList<SelectItem> settlementID = new ArrayList<SelectItem>();
	
		ArrayList<TblSettlement> settlement = new ArrayList<TblSettlement>();
		settlement.addAll((Collection<? extends TblSettlement>) 
				entityLister.getObjectList("SELECT * FROM OMsettlement", 
						TblSettlement.class));
		
		for (TblSettlement item : settlement) {
			settlementID.add(new SelectItem(item.getIdSettlement()));
		}
		return settlementID;			
	}
	
	public void InvoicestateChangeFilter(ValueChangeEvent vce){
		setInvoicestate(vce.getNewValue().toString());				
	}
	
	public void OrderChangeFilter(ValueChangeEvent vce){
		setOrdernumber(vce.getNewValue().toString());				
	}
	
	public void IncometypeChangeFilter(ValueChangeEvent vce){
		setIncometype(vce.getNewValue().toString());			
	}
	
	public void SettlementChangeFilter(ValueChangeEvent vce){
		setSettlementnumber(vce.getNewValue().toString());				
	}
}