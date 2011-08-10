package at.icnc.om.entitybeans;

import javax.persistence.Transient;

public class Selectable {
	protected boolean selected;
	
	@Transient
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Transient
	public boolean isSelected() {
		return selected;
	}	
}
