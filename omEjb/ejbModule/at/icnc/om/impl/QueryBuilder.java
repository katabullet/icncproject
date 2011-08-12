package at.icnc.om.impl;

public class QueryBuilder {
	
	public String StatementCreater(String table, String[] filterColumn, 
									String[] filterValue){
		String sqlStatement = "SELECT * FROM " + table;
		if(filterColumn != null && filterValue != null){
			sqlStatement += " WHERE ";
			for(int x = 0; x< filterColumn.length; x++){
				sqlStatement += " AND ";
				sqlStatement += filterColumn[x] + " LIKE '%" + filterValue[x] + "%'";
			}
		}
		sqlStatement = sqlStatement.substring(0, sqlStatement.length() - 4);
		return sqlStatement;
	}
	
}