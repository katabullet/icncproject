package inet.module.mdm.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * {@link CallbackHandler} for login.
 * 
 * @author fre80
 * @version $Id: InetCallbackHandler.java 4071 2011-03-03 14:09:00Z fre80 $
 * 
 */
public class InetCallbackHandler implements CallbackHandler {

	private String name;
	private char[] password;

	public InetCallbackHandler(String name, char[] password) {

		this.name = name;
		this.password = password;
	}

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				NameCallback nameCallback = (NameCallback) callbacks[i];
				nameCallback.setName(name);
			} else if (callbacks[i] instanceof PasswordCallback) {
				PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];
				passwordCallback.setPassword(password);
			} else {
				throw new UnsupportedCallbackException(callbacks[i],
						"The submitted Callback is unsupported");
			}
		}
	}

}
