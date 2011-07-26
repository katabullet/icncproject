package inet.module.mdm.jsf.helpers;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Helper class that should be used from jsf-beans for getting translated
 * strings.
 *
 * @author fre80
 * @version $Id: MessageBundleHelper.java 5132 2011-05-02 10:42:26Z dka80 $
 *
 */
public class MessageBundleHelper {

	private static ClassLoader getCurrentClassLoader(Object defaultObject)  {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null) {
			loader = defaultObject.getClass().getClassLoader();
		}

		return loader;
	}

	/**
	 * Gets a translated version of the String given in the key
	 *
	 * @param bundleName
	 *            The name of the {@link ResourceBundle} to use
	 * @param key
	 *            The messageKey
	 * @param params
	 *            An optional parameter list for the message.
	 * @param locale
	 *            The target locale.
	 * @return String
	 */
	public static String getMessageResourceString(String bundleName, String key, Object[] params, Locale locale) {

		String text = null;
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(params));

		try {
			text = bundle.getString(key);
		} catch (MissingResourceException e) {
			text = "?? key " + key + " not found ??";
		}

		if (params != null) {
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}

		return text;
	}

	/**
	 * Existence check for an message key
	 *
	 * @param bundleName
	 *            The name of the {@link ResourceBundle} to use
	 * @param key
	 *            The messageKey
	 * @param locale
	 *            The target locale.
	 * @return true Message is setted
	 */
	public static boolean hasKey(String bundleName, String key, Locale locale) {

		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);

		return bundle.containsKey(key);
	}

	/**
	 * Gets a translated version of the String given in the key.<br>
	 * This method uses the applications message bundle and the locale of the viewRoot.
	 *
	 * @param key    the key from the message bundle
	 * @param params the parameters
	 * @return       the translated text
	 */
	public static String getMessage(String key, Object... params) {

		FacesContext context = FacesContext.getCurrentInstance();
		String bundle = context.getApplication().getMessageBundle();
		Locale locale = context.getViewRoot().getLocale();

		String message = getMessageResourceString(bundle, key, params, locale);
		return message;
	}
}
