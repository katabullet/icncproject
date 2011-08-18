package at.icnc.om.validators;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.validator.*;

public class InvoiceValidator implements Validator{

	public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException{
		
		FacesMessage message = new FacesMessage();
		message.setSummary("Falsches Eingabeformat");
		
		try {
			BigDecimal enteredSum = (BigDecimal) object;	
			
		} catch (ValidatorException e) {
			throw new ValidatorException(message);
		} catch (ConverterException e){
			throw new ValidatorException(message);
		}
	}
}
