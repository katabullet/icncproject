package inet.module.mdm.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class PassiveCallbackHandler implements CallbackHandler {

	private String username;
	private char[] password;


	public PassiveCallbackHandler(String user, String pass) {
		this.username = user;
		this.password = pass.toCharArray();

	}


	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback ) {
				((NameCallback)callbacks[i]).setName(username);
			} else if (callbacks[i] instanceof PasswordCallback ) {
				((PasswordCallback)callbacks[i]).setPassword(password);
			} else {
				throw new UnsupportedCallbackException(callbacks[i],"Callback class not supported");
			}

		}

	}

	public void clearPassword() {
		if (password != null) {
			for (int i = 0; i < password.length; i++) {
				password[i] = ' ';
			}
			password = null;
		}
	}


	}
