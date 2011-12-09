package at.icnc.om.backingbeans;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

public class DataExporter {
	private Effect changeEffect;
    private String type;
    // Variable to set and get if DataExporter is rendered
    private Boolean rendered = false;
    
    public DataExporter() {
        changeEffect = new Highlight("#fda505");
        //changeEffect.setFired(true);
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Effect getChangeEffect() {
        return changeEffect;
    }

    public void setChangeEffect(Effect changeEffect) {
        this.changeEffect = changeEffect;
    }
    
    /**
     * Function to return a CSVOutputtypehandler
     * This Outputtypehandler uses semicolons instead of commas 
     * @return CSVOutputHandlerSemicolon
     */
    public CSVOutputHandlerSemicolon getCsvHandler(){
    	CSVOutputHandlerSemicolon csvHandler;
    	
    	ServletContext context = ((HttpSession) FacesContext
                .getCurrentInstance().getExternalContext().getSession(false))
                .getServletContext();

		String pathWithoutExt = context.getRealPath("/export") + "/export_"
            + new Date().getTime();
		
		csvHandler = new CSVOutputHandlerSemicolon(pathWithoutExt + ".csv");
    	
    	return csvHandler; 
    }    

	public void setRendered(Boolean rendered) {
		this.rendered = rendered;
	}

	public Boolean getRendered() {
		return rendered;
	}
}
