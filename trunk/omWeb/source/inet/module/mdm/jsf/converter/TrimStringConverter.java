package inet.module.mdm.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * A JSF Converter that removes all leading and trailing blanks from a String.<br>
 *
 * @author gbo80 05.04.2011
 * @version $Id: TrimStringConverter.java 4715 2011-04-05 15:16:51Z gbo80 $
 */
public class TrimStringConverter implements Converter {

	/**
	 * Conversion from presentation view to the model view.
	 * @param context the faces context
	 * @param component the UIComponent
	 * @param valueString the presentation view value String
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String valueString) {

		if (valueString == null) {
			return valueString;
		}

		return valueString.trim();
	}

	/**
	 * Conversion from the model view to the presentation view.
 	 * @param context the faces context
	 * @param component the UIComponent
	 * @param valueObject the model view value Object
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object valueObject) {

		if (valueObject == null) {
			return null;
		}

		return ((String)valueObject).trim();
	}
}
