package at.icnc.om.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import at.icnc.om.entitybeans.TblInvoice;

@Local
public interface TblInvoiceLocal {
	public ArrayList<TblInvoice> getInvoiceList();
	public void DeleteInvoice(Long invoice);
}
