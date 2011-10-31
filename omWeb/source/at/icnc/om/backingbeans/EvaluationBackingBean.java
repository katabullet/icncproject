package at.icnc.om.backingbeans;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static List labels;

    //The list of the legend label for the chart
    private static final List legendLabels = new ArrayList(Arrays.asList(
            new String[]{"Umsatz"}));

    //The list of the data used by the chart
    private static List data;

    //The list of the colors used by the chart
    private static final List paints =
            new ArrayList(Arrays.asList(new Color(106, 209, 150)));
    
    // List with all Customers and their turnover
    // Important to create chart
    private ArrayList<TblCustomer> customerList;
    
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
    	
		// Customerlist is initialized
		customerList = new ArrayList<TblCustomer>();
		
		// All Customers are read out of DB
		customerList = (ArrayList<TblCustomer>) entityLister.getObjectList(TblCustomer.class);

		// Number of customers is saved
		int x = customerList.size();
		
		// One Arry for all Sums (turnovers) and one for all customers
		double[] sum = new double[x];
		String[] customer = new String[x];
		
		// Variable-Reset
		x = 0;
		
		// All Customers in customerlist are read
		for(TblCustomer curCustomer : customerList){
			// All Orders that belong to one customer are read 
			for(TblOrder curOrder : curCustomer.getTblOrders()){
				// All Settlements of a specific order are read
				for(TblSettlement curSettlement : curOrder.getTblSettlements()){
					// All Invoices of each settlement is read
					for(TblInvoice curInvoice : curSettlement.getTblInvoices()){
						// Sum of invoice is added to sum-Array
						sum[x] += curInvoice.getSum().doubleValue();
					}
				}
			}
			// Customername is set 
			customer[x] = curCustomer.getCustomername();
			// Next Customer
			x++;
		}
		
		/* Bubble sort
		 * Biggest sum is set to first place (user sees "best" customer first)
		 */		
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
					
		// Labels of chart are set
		labels = new ArrayList(Arrays.asList(customer));		
		
		// All Sums are added to list that is used by chart
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
		customerList = null;
		data = null;
		
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