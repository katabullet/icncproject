package at.icnc.om.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import at.icnc.om.entitybeans.TblOrder;

@Local
public interface TblOrderLocal {
	public ArrayList<TblOrder> getOrderList();
}
