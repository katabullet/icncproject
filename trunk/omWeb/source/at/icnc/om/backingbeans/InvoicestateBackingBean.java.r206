package at.icnc.om.backingbeans;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblInvoicestate;
import at.icnc.om.interfaces.EntityListerLocal;
import at.icnc.om.interfaces.TblInvoicestateLocal;

public class InvoicestateBackingBean {
	
		@EJB
		private TblInvoicestateLocal tblInvoicestate;
		@EJB
		private EntityListerLocal entityLister;
		
		ArrayList<TblInvoicestate> invoicestates;
		private TblInvoicestate curInvoicestate;
		
		@SuppressWarnings("unchecked")
		public ArrayList<TblInvoicestate> getInvoicestateList(){
			/*invoicestates.clear();
			invoicestates.addAll(tblInvoicestate.getInvoicsestateList());
			return invoicestates;*/
			invoicestates.clear();
			invoicestates.addAll((Collection<? extends TblInvoicestate>) 
					entityLister.getObjectList(TblInvoicestate.class));
			if(curInvoicestate != null){
				for(TblInvoicestate curItem : invoicestates){				
					if(curItem.getIdInvoicestate() == curInvoicestate.getIdInvoicestate()){
						curItem.setSelected(true);
					}
				}
			}
			
			return invoicestates;
		}	
		
		public ArrayList<SelectItem> getInvoicestateListDescription(){
			ArrayList<SelectItem> invoicsestates = new ArrayList<SelectItem>();
			for (TblInvoicestate item : tblInvoicestate.getInvoicsestateList()) {
				invoicsestates.add(new SelectItem(item.getDescriptionIs()));
			}
			return invoicsestates;			
		}
		
		public void setInvoicestateListDescription(SelectItem selectedItem){
			setCurInvoicestate((TblInvoicestate) entityLister.getSingleObject("SELECT * FROM OMinvoicestate WHERE description_is = '" + 
					selectedItem.getValue() + "'", TblInvoicestate.class));				
		}

		public void setCurInvoicestate(TblInvoicestate curInvoicestate) {
			this.curInvoicestate = curInvoicestate;
		}

		public TblInvoicestate getCurInvoicestate() {
			return curInvoicestate;
		}
}
