package at.icnc.om.impl;

public class QueryBuilder {
	
	private static final String projectPrefix = "om";
	
	public QueryBuilder(){
	}
	
	public static String CreateSelectStatement(String table, String[] filterColumn, 
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
