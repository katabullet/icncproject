package icnc.backingbeans;

/*import gw.basis.security.GWCryptoException;*/
import inet.module.mdm.jsf.helpers.MessageBundleHelper;
/*import inet.module.mdm.security.CryptoHelper;
import inet.module.mdm.security.DatabasePolicy;*/
import inet.module.mdm.security.InetCallbackHandler;
/*import inet.module.mdm.security.SecurityHelper;*/

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Backing bean encapsulating all user handling logic.
 *
 * @author fre80
 * @version $Id: UserBean.java 4995 2011-04-21 10:31:50Z aku80 $
 *
 */
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserBean.class
			.getName());

	private String username;
	private char[] password;
	/*private PropertiesBean propertiesBean;*/

	public String getPassword() {
		return "";
	}

	public String getUsername() {
		return username;
	}

	/*public boolean isDefinePermissionIdsAllowed() {
		return SecurityHelper.hasPermission(DEFINE_PERMISSION_IDS_PERMISSION);
	}

	public boolean isDefineRolesAllowed() {
		return SecurityHelper.hasPermission(DEFINE_ROLES_PERMISSION);
	}

	public boolean isDefineUsersAllowed() {
		return SecurityHelper.hasPermission(DEFINE_USERS_PERMISSION);
	}

	public boolean isDefineValidationRulesAllowed() {
		return SecurityHelper.hasPermission(DEFINE_VALIDATION_RULES_PERMISSION);
	}

	public boolean isLinkToOldMMAllowed() {
		return SecurityHelper.hasPermission(OLD_MM);
	}

	public boolean isEditHelpTextsAllowed() {
		return SecurityHelper.hasPermission(EDIT_HELP_TEXTS_PERMISSION);
	}*/

	public String login() throws IOException {
		try {
			LoginContext loginContext = new LoginContext("InetJaas",
					new InetCallbackHandler(username, password));
			loginContext.login();
			Subject subject = loginContext.getSubject();
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext()
					.getSession(true);
			session.setAttribute("FILTER_SUBJECT", subject);
		
		
			// WebAuthentication webAuthentication = new WebAuthentication();
			// if (!webAuthentication.login(username, password)) {
			// FacesContext context = FacesContext.getCurrentInstance();
			// String message = MessageBundleHelper.getMessageResourceString(
			// "messages", "userInvalidCredentials", null, context
			// .getExternalContext().getRequestLocale());
			// context.addMessage(null, new FacesMessage(
			// FacesMessage.SEVERITY_ERROR, message, message));
			// return "failure";
			// }
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
			response.sendRedirect(context.getExternalContext()
					.encodeResourceURL("index.jspx"));
			context.responseComplete();
			return "success";	
			
		} catch (LoginException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			String message = MessageBundleHelper.getMessageResourceString(
					"messages", "userInvalidCredentials", null, context
							.getExternalContext().getRequestLocale());
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, message, message));
			return "failure";
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not redirect!", e);
			throw new RuntimeException(e);
		} finally {
			wipePassword();
		}		
	}

	public String logout() throws IOException {
		String tmpUsername = this.username;
		FacesContext context = FacesContext.getCurrentInstance();
		String contextPath = context.getExternalContext()
				.getRequestContextPath();
		context.getExternalContext().redirect(contextPath + "/login.jspx");
		username = null;
		context.responseComplete();
		/*DatabasePolicy.removeEntityPermissionCacheEntry(tmpUsername);*/
		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		session.setAttribute("FILTER_SUBJECT", null);
		// Session will be invalidate in the loggedOut.jsp page
		return "logout";
	}

	public void setPassword(String password) {
		this.password = password.toCharArray();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void wipePassword() {
		if (password != null) {
			for (int i = 0; i < password.length; i++) {
				password[i] = 32;
			}
		}
	}

	/*public String getLoginToken(String sKey) {
		String sToken = makeStringToken('|', getUsername(),
				String.valueOf(System.currentTimeMillis() + (60 * 60 * 1000)));
		try {
			return CryptoHelper.encryptToUrlEncoded(sToken, sKey);
		} catch (GWCryptoException e) {
			logger.log(Level.SEVERE, e.getStackTraceAsString());
			return null;
		}
	}

	public String getLinkToOldMdm() {
		linkToOldMdm = propertiesBean.getMDMUrl() + "/md/pages/Start.jspx?LoginToken="
			+ getLoginToken(propertiesBean.getSecurityCipherDESedeKey());
		return linkToOldMdm;
	}

	/**
	 * Hängt mehrere Strings mit einem Trennzeichen zusammen.
	 *
	 * @param cDelim
	 *            das Trennzeichen
	 * @param asString
	 *            die Strings
	 * @return der zusammengehängte String
	 */
	public static String makeStringToken(char cDelim, String... asString) {
		StringBuilder oSB = new StringBuilder();
		int i = 0;

		for (String sString : asString) {
			oSB.append(i > 0 ? cDelim : "").append(sString);
			i++;
		}

		return oSB.toString();
	}

	/**
	 * @return the properties
	 */
	/*public PropertiesBean getPropertiesBean() {
		return propertiesBean;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	/*public void setPropertiesBean(PropertiesBean propertiesBean) {
		this.propertiesBean = propertiesBean;
	}*/
}
