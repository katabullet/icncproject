package at.icnc.om.backingbeans;

import at.icnc.om.entitybeans.TblOrder;

public class SitesBean{
	
	private int role = 4;
	private  Integer width;
	Boolean render = false;
	
		public Integer getTabWidth(){

			role = UserBean.userrole;
			switch (role) {
				case 1:
				{
					width = 120;
				}
					break;
				case 2:
				{
					width = 148;
				}				
					break;
				case 3:
				{
					width = 188;
				}				
					break;
				case 4:
				{
					width = 248;
				}				
					break;
			}
			return  width;
		}
		
		public Boolean getRendered5(){
			render = role>3 ? false:true;
			
			return render;
		}
		public Boolean getRendered6(){
			render = role>2 ? false:true;
			
			return render;
		}
		public Boolean getRendered7(){
			render = role>1 ? false:true;
			
			return render;
		}
		TblOrder o;
		
		public Boolean getButtonRendered()
		{
			render = role>3 ? false:true;
			return render;
		}
		
		public Integer getInvoiceTabWidth(){
			return 524;
		}

		public Integer getOrderTabWidth(){
			return 179;
		}
		
		public Integer getCustomerTabWidth(){
			return 237;
		}
}
