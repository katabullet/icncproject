package at.icnc.om.backingbeans;

import java.util.ArrayList;

import javax.ejb.EJB;

import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.interfaces.TblIncometypeLocal;

public class IncometypeBackingBean {
	
	@EJB
	TblIncometypeLocal tblIncometype;
	
	public ArrayList<TblIncometype> getIncometypeList(){		
		ArrayList<TblIncometype> incometype = new ArrayList<TblIncometype>();
		incometype.addAll(tblIncometype.getIncometypeList());

		return incometype;
	}	

}
