package at.icnc.om.interfaces;

/**
 * Interface for all Backing beans that are refreshable
 * @author csh80
 *
 */
public interface Refreshable {
	
	/* initialization */
	public void init();
	
	/* refreshing of lists */
	public void refresh();
}
