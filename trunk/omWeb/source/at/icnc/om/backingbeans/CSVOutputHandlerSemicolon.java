package at.icnc.om.backingbeans;

import java.io.FileWriter;
import java.io.IOException;

import com.icesoft.faces.component.dataexporter.OutputTypeHandler;

/**
 * This class is copied from a standard component
 * 
 * the changes: 
 * all commas have been replaced by semicolons
 * this was to better show csv-file in excel
 * 
 * @author csh80
 *
 */
public class CSVOutputHandlerSemicolon extends OutputTypeHandler{
	
	StringBuffer buffer;
	int rowIndex = 0;

	public CSVOutputHandlerSemicolon(String path) {
		super(path);
		buffer = new StringBuffer();
		this.mimeType = "text/csv";
	}
	
	public void flushFile() {
		buffer.deleteCharAt(buffer.lastIndexOf(";"));		
		try{
			FileWriter fw = new FileWriter(getFile());
			fw.write(buffer.toString());
			fw.close();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}

	public void writeCell(Object output, int col, int row) {
		if( row != rowIndex ){
			buffer.deleteCharAt(buffer.lastIndexOf(";"));
			buffer.append("\n");
			rowIndex ++;
		}
		buffer.append(output.toString() + ";");
		
	}

	public void writeHeaderCell(String text, int col) {
		//do nothing, no header to write for csv		
	}
}