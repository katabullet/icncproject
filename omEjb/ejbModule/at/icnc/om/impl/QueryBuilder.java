package at.icnc.om.impl;

import java.sql.Date;
import java.util.ArrayList;

public class QueryBuilder {
	
	private static final String projectPrefix = "om";
	private static Date datum;
	
	public QueryBuilder(){
	}
	
	public static String CreateSelectStatement(String table, String Joins, ArrayList<String> filterColumn, 
									ArrayList<String> filterValue){
		String sqlStatement = "SELECT t FROM " + table + " t" + Joins;
		if(filterColumn.get(0) != ""){
			sqlStatement += " WHERE ";
			for(int x = 0; x< filterColumn.size(); x++){
				if(x !=0){
					sqlStatement += " AND ";
				}
				
				if(filterValue.get(x).contains(":")){
					String[] werte = (filterValue.get(x).split(":"));
					sqlStatement += filterColumn.get(x) + " BETWEEN '" + werte[0] + "' AND '" + werte[1] + "'";
				}
				else{
					sqlStatement += filterColumn.get(x) + " LIKE '%" + filterValue.get(x) + "%'";// " = '" + filterValue.get(x) +"'";
				}
			}
		}

		System.out.println("------------" + sqlStatement);
		return sqlStatement;
	}
	
	public static String CreateSelectStatement(Class<?> entityClass){
		String sqlStatement = "";
		String table = "";
		
		sqlStatement = "SELECT * FROM ";
		table = entityClass.getSimpleName();
		table = projectPrefix + table.substring(3);
		sqlStatement += table;
		
		return sqlStatement;
	}
}
