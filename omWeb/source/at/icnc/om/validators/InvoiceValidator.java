package at.icnc.om.validators;

import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.validator.*;

import org.eclipse.persistence.exceptions.ValidationException;

public class InvoiceValidator implements Validator{

	public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException{
		
		FacesMessage message = new FacesMessage();
		message.setSummary("Falsches Eingabeformat");		
		
		try {
			BigDecimal enteredSum = (BigDecimal) object;
						
		} catch (ValidatorException e) {
			facesContext.addMessage("invoiceForm:errSum", message);
			throw new ValidatorException(message);
		} catch (ConverterException e){
			facesContext.addMessage("invoiceForm:errSum", message);
			throw new ValidatorException(message);
		} catch (Exception e){
			facesContext.addMessage("invoiceForm:errSum", message);
			throw new ValidatorException(message);
		}
	}
}
