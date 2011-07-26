package Login;

import inet.module.mdm.jsf.helpers.MessageBundleHelper;
import inet.module.mdm.security.InetCallbackHandler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginBean {
	
	private String username;
	private char[] password;
	
	
	private static final Logger logger = Logger.getLogger(LoginBean.class
			.getName());
	
	
	public void setPassword(String password){
		this.password = password.toCharArray();
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	
	public char[] getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String login() {
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
	
	public void wipePassword() {
		if (password != null) {
			for (int i = 0; i < password.length; i++) {
				password[i] = 32;
			}
		}
	}

}
