package at.icnc.om.backingbeans;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.interfaces.TblOrderLocal;

public class TblOrderBackingBean {
	
	@EJB
	private TblOrderLocal tblOrder;
	
	ArrayList<TblOrder> orders = new ArrayList<TblOrder>();
	
	public ArrayList<TblOrder> getOrderList(){
		orders.clear();
		orders.addAll(tblOrder.getOrderList());
		return orders;
	}	
	
	public ArrayList<SelectItem> getOrderListNumbers(){
		ArrayList<SelectItem> numbers = new ArrayList<SelectItem>();
		for (TblOrder item : tblOrder.getOrderList()) {
			numbers.add(new SelectItem(item.getOrdernumber()));
		}
		return numbers;
	}
}
