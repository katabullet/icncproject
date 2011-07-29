package at.icnc.om.interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import at.icnc.om.entitybeans.TblIncometype;

@Local
public interface TblIncometypeLocal {
	public ArrayList<TblIncometype> getIncometypeList();
}
