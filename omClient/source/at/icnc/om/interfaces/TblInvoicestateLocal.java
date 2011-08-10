package at.icnc.om.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import at.icnc.om.entitybeans.TblInvoicestate;

@Local
public interface TblInvoicestateLocal {
	public ArrayList<TblInvoicestate> getInvoicsestateList();
}