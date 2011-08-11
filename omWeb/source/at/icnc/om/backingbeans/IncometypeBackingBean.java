package at.icnc.om.backingbeans;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import at.icnc.om.entitybeans.TblIncometype;
import at.icnc.om.interfaces.EntityListerLocal;
import at.icnc.om.interfaces.TblIncometypeLocal;

public class IncometypeBackingBean {
	
	@EJB
	TblIncometypeLocal tblIncometype;	
	@EJB
	EntityListerLocal entityLister;
	ArrayList<TblIncometype> incometypes;
	
	@SuppressWarnings("unchecked")
	public ArrayList<TblIncometype> getIncometypeList(){	
		if(incometypes == null){
			//incometypes.addAll(tblIncometype.getIncometypeList());
			incometypes.addAll((Collection<? extends TblIncometype>) entityLister.getObjectList("SELECT * FROM OMincometype", TblIncometype.class));
		}
		return incometypes;
	}
	
	public ArrayList<SelectItem> getIncometypeListDescriptions(){
		ArrayList<SelectItem> descriptions_it = new ArrayList<SelectItem>();
		for (TblIncometype item : tblIncometype.getIncometypeList()) {
			descriptions_it.add(new SelectItem(item.getDescriptionIt()));
		}
		return descriptions_it;
	}
	
	public void setIncometypeListDescriptions(SelectItem selectedItem){
		
	}

}
