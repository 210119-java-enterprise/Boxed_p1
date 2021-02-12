package com.revature.utilities.queries;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Gabrielle Luna
 */
public class ResultSetParser {

    public static String getSummary(ResultSet rs){
        StringBuilder resultSummary = new StringBuilder("");
        try {
            ResultSetMetaData rsMd = rs.getMetaData();
            int count = rsMd.getColumnCount();
            resultSummary.append("Column Count: ").append(count);
            for (int i = 1; i <= count; i++) {
                resultSummary.append("\n\tColumn ").append(i).append("\n\t\tname: ")
                        .append(rsMd.getColumnName(i)).append(", column type: ")
                        .append(rsMd.getColumnClassName(i)).append(", from table: ")
                        .append(rsMd.getTableName(i));
            }
            int numRows = 0;
            while(rs.next()){
                numRows++;
            }
            resultSummary.append("\nEntry count: " + numRows);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultSummary.toString();
    }

    /**
     * always returns entity related to first column in resultSet
     *
     * @param rs
     * @return
     */
    public static String getEntityName(ResultSet rs){
        try{
            ResultSetMetaData rsMd = rs.getMetaData();
            int count = rsMd.getColumnCount();
            if (count == 0)
                return null;
            return rsMd.getTableName(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
