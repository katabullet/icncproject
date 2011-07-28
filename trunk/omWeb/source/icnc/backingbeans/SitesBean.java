package icnc.backingbeans;

import java.util.ArrayList;

import at.icnc.om.entitybeans.TblUser;


public class SitesBean{
	
	private int role = 4;
	private  Integer width;
	Boolean render = false;
	private ArrayList<TblUser> User;
	
	
		public Integer getTabWidth(){
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
		
		public ArrayList<TblUser> getUser()
		{
			TblUser u = new TblUser();
			u.setIdUser(5);
			u.setPassword("Hallo");
			u.setUsername("Peter");
			User.add(u);
			User.add(u);
			
			return User;
		}
		
		
}
