package at.icnc.om.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import at.icnc.om.interfaces.PermissionLocal;

/**
 * Session Bean implementation class PermissionImpl
 */
@Stateless
public class PermissionImpl implements PermissionLocal {

    /**
     * Default constructor. 
     */
    public PermissionImpl() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public Integer setPermission(String username) {
		Integer userrole = 4;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = getSQLConnection("java:omDS");
		
		try {
			ps = con.prepareStatement("SELECT FK_USERROLE FROM TBL_USER WHERE USERNAME = ?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			while(rs.next()){
				userrole = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				
			}
		}
		
		return userrole;
	}
	
	private Connection getSQLConnection(String jndiName) {
		java.sql.Connection conn = null;
		try {
			Context initContext = new InitialContext();
			javax.sql.DataSource ds = (javax.sql.DataSource) initContext
					.lookup(jndiName);
			conn = ds.getConnection();
			return conn;
		} catch (javax.naming.NamingException nex) {
			nex.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
