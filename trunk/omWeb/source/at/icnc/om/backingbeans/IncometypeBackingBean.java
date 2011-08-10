package at.icnc.om.backingbeans;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.interfaces.TblIncometypeLocal;

public class IncometypeBackingBean {
	
	@EJB
	TblIncometypeLocal tblIncometype;	
	ArrayList<TblIncometype> incometypes = new ArrayList<TblIncometype>();
	
	public ArrayList<TblIncometype> getIncometypeList(){	
		incometypes.clear();
		incometypes.addAll(tblIncometype.getIncometypeList());
		return incometypes;
	}
	
	public ArrayList<SelectItem> getIncometypeListDescriptions(){
		ArrayList<SelectItem> descriptions_it = new ArrayList<SelectItem>();
		for (TblIncometype item : tblIncometype.getIncometypeList()) {
			descriptions_it.add(new SelectItem(item.getDescriptionIt()));
		}
		return descriptions_it;
	}

}
