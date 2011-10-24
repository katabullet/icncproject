package at.icnc.om.backingbeans;

import java.awt.Color;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.faces.context.FacesContext;

import at.icnc.om.entitybeans.TblCustomer;
import at.icnc.om.entitybeans.TblInvoice;
import at.icnc.om.entitybeans.TblOrder;
import at.icnc.om.entitybeans.TblSettlement;

import com.icesoft.faces.component.ext.RowSelectorEvent;



/**
 * Implementation of concernLister
 * 
 * @author csh80, nkn80, cma80
 *
 */
public class EvaluationBackingBean extends AbstractBean {
	
	//list of the Labels for the x axis of the chart
    private static List labels = new ArrayList(Arrays.asList(
            new String[]{"2000", "2001"}));

    //The list of the legend label for the chart
    private static final List legendLabels = new ArrayList(Arrays.asList(
            new String[]{"Umsatz"}));

    //The list of the data used by the chart
    private static List data = new ArrayList(
            Arrays.asList(new double[][]{new double[]{350},
                                         new double[]{45}}));

    //The list of the colors used by the chart
    private static final List paints =
            new ArrayList(Arrays.asList(new Color(106, 209, 150)));


    public List getLabels() {

    	
        return labels;
    }


    public List getLegendLabels() {

        return legendLabels;
    }


    public List getData_() {

        return data;
    }


    public List getPaints() {

        return paints;
    }
	    
	@SuppressWarnings("unchecked")
	public List getData(){
    	
    	ArrayList<TblCustomer> customerList = new ArrayList<TblCustomer>();
    	
    	customerList = (ArrayList<TblCustomer>) entityLister.getObjectList(TblCustomer.class);
			
		int x = customerList.size();
		double[] sum = new double[x];
		String[] customer = new String[x];
		x = 0;
		for(TblCustomer curCustomer : customerList){
			for(TblOrder curOrder : curCustomer.getTblOrders()){
				for(TblSettlement curSettlement : curOrder.getTblSettlements()){
					for(TblInvoice curInvoice : curSettlement.getTblInvoices()){						
						sum[x] += curInvoice.getSum().doubleValue();
					}
				}
			}
			customer[x] = curCustomer.getCustomername();
			x++;
		}
		
		for(int y = sum.length - 1; y > 0; y--){
			for(int z = 0; z < y; z++){
				if(sum[z] < sum[z+1]){
					double bigger = sum[z];
					double smaller = sum[z+1];
					String biggerCustomer = customer[z];
					String smallerCustomer = customer[z+1];
					
					customer[z] = smallerCustomer;
					customer[z+1] = biggerCustomer;
					sum[z] = smaller;
					sum[z+1] = bigger;
				}
			}
		}
		
		
						
		labels = new ArrayList(Arrays.asList(customer));		
		
		double[][] sums = new double[sum.length][1];
		for(int y = 0; y < sum.length; y++){
			sums[y] = new double[] { sum[y] };
		}
		
		data = new ArrayList(Arrays.asList(sums));
    	return data;
    } 

	@Override
	public void changePopupRenderNew() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rowEvent(RowSelectorEvent re) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEntity() {
		// TODO Auto-generated method stub
		
	}
}