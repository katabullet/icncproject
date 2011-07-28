package at.icnc.om.backingbeans;

import java.util.ArrayList;

import javax.ejb.EJB;

import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.interfaces.TblOrderLocal;

public class TblOrderBackingBean {
	
	@EJB
	private TblOrderLocal tblOrder;
	
	public ArrayList<TblOrder> getOrderList(){
		ArrayList<TblOrder> result = new ArrayList<TblOrder>();

		result.addAll(tblOrder.getOrderList());
		
		return result;
	}	
}
