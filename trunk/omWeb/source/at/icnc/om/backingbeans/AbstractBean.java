package at.icnc.om.backingbeans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import at.icnc.om.entitybeans.TblProtocol;
import at.icnc.om.entitybeans.TblUser;
import at.icnc.om.interfaces.EntityListerLocal;
import at.icnc.om.interfaces.Refreshable;

import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.faces.component.ext.RowSelectorEvent;

/**
 * This class has methods that are used in all backing beans.
 * General getter and setter are also included
 * 
 * @author csh80
 *
 */
public abstract class AbstractBean implements Refreshable {
	
	// Session Bean of EntityLister to Read and Write Entities
	@EJB
	protected EntityListerLocal entityLister;
	
	// Variable to set Delete- and Edit-Button visible 
	protected Boolean visible = false;
	
	// Variable to make Filter-Popup visible and rendered
	protected Boolean filterpopupRender = false;
	
	// Variable to make Edit-Popup visible and rendered
	protected Boolean popupRender = false;
	
	// Variable to make Delete-Popup visible and rendered
	protected Boolean deletePopupRender = false;
	
	// DataPaginator for auto pagination and resetting to first page
	protected DataPaginator paginator = new DataPaginator();
	
	// Variable to write Error-Messages
	protected String errorMessage;
	
	/*
	 * Delete and Edit-Button visible
	 */
		/* Getter for Delete- and Edit-Button visible */
		public Boolean getVisible(){		
			return visible;
		}
		
		/* Setter for Delete- and Edit-Button visible */
		protected void setVisible(Boolean visible){
			this.visible = visible;
		}
	
	/*
	 * Filter-Popup
	 */
		/* Getter for FilterPopupRender */
		public Boolean getFilterPopupRender(){
			return filterpopupRender;
		}	
		
		/**
		 * Changes render value of Filter-Popup
		 */
		public void changeFilterPopupRender(){
			filterpopupRender = !filterpopupRender;		
		}	
	
	/*
	 * Edit-Popup
	 */
		/**
		 * Changes render value of Invoice-Popup
		 */
		public void changePopupRender(){
			popupRender = !popupRender;		
		}
		
		/* Getter for PopupRender */
		public Boolean getPopupRender(){
			return popupRender;
		}
		
		/**
		 * Method to open popup to create new Entity
		 */
		public abstract void changePopupRenderNew();
	
	/*
	 * Delete-Popup
	 */
		/* Setter of DeletePopup Render */
		protected void setDeletePopupRender(Boolean deletePopupRender) {
			this.deletePopupRender = deletePopupRender;
		}
	
		/* Getter of DeletePopup Render */
		public Boolean getDeletePopupRender() {
			return deletePopupRender;
		}
		
		/**
		 * Changes render value of Delete-Popup
		 */
		public void changeDeletePopupRender(){
			setDeletePopupRender(true);
		}
	
	/*
	 * All Popups
	 */
		/**
		 * All Popups can be closed via this method
		 */
		public void closePopup(){
			refresh();
		}
		
	/*
	 * Paginator
	 */
		/* Setter of Paginator */
		public void setPaginator(DataPaginator paginator) {
			this.paginator = paginator;
		}
		
		/* Getter of paginator */
		public DataPaginator getPaginator() {
			return paginator;
		}
	
	/*
	 * RowSelector in DataTable
	 */
		/**
		 * This method captures a RowSelectorEvent
		 * It is called when clicked on element in DataTable
		 * @param re
		 */
		public abstract void rowEvent(RowSelectorEvent re);
	
	/*
	 * Refreshable Bean	
	 */
		/*
		 * Initialization 
		 */
		public abstract void init();
	
		/*
		 * Refreshing
		 */
		public abstract void refresh();
		
	/*
	 * Error-Message
	 */
		/* Setter of errorMessage */
		public void setErrorMessage(String errorMessage){
			this.errorMessage = errorMessage;
		}
		
		/* Getter of errorMessage */
		public String getErrorMessage(){
			return this.errorMessage;
		}
		
	/*
	 * Entity-Operations
	 */
		/* Method to delete the currently selected entity */
		public abstract void deleteEntity();
		
		/* Method to update the currently selected entity 
		 * or save a new one on database
		 */
		public abstract void updateEntity();
		
		protected void insertProtocol(String message){
			TblProtocol protocol = new TblProtocol();

			protocol.setIdProtocol(0);
			protocol.setChange(message);
			protocol.setDate(new Date());
			
			UserBean user = getUserBean();
			if(user != null){
				
				System.out.println("***************************************************************************** asdf ");
				/*TblUser test = new TblUser();
				test.setIdUser(1);
				protocol.setTblUser(test);*/
				protocol.setTblUser((TblUser) entityLister.getSingleObject("SELECT * FROM OMUser WHERE username = '" 
														+ user.getUsername() + "'", TblUser.class));		
			}
			
			entityLister.UpdateObject(TblProtocol.class, protocol, protocol.getIdProtocol());						
		}
		
		protected UserBean getUserBean(){
			UserBean result = (UserBean) FacesContext.getCurrentInstance()
												.getExternalContext().getSessionMap().get("user");			
			return result;
		}
		
}