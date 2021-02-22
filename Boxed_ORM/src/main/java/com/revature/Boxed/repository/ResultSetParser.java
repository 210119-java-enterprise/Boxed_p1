package com.revature.Boxed.repository;

import com.revature.Boxed.annotations.Column;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabrielle Luna
 */
public class ResultSetParser {

    public static String getSummary(ResultSet rs){
        //Validate
        if (rs == null) return "";

        StringBuilder resultSummary = new StringBuilder("");
        try {
            //Retrieve metaData
            ResultSetMetaData rsMd = rs.getMetaData();
            if (rsMd == null) return "";

            //Parse metaData
            int count = rsMd.getColumnCount();
            resultSummary.append("Column Count: ").append(count);
            for (int i = 1; i <= count; i++) {
                resultSummary.append("\n\tColumn ").append(i).append("\n\t\tname: ")
                        .append(rsMd.getColumnName(i)).append(", column type: ")
                        .append(rsMd.getColumnClassName(i)).append(", from table: ")
                        .append(rsMd.getTableName(i));
            }
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
    public static Object getObjFromResult(Object obj, List<Field> fields, ResultSet rs)
        throws SQLException, IllegalAccessException, IllegalArgumentException{
        if(fields == null)
        System.out.println("\n\n");

        Class<?> clazz = obj.getClass();

        for (Field field: fields) {
            //Allows access to private fields
            field.setAccessible(true);

            Column column = field.getAnnotation(Column.class);

            Object value = rs.getObject(column.columnName());
            Class<?> type = field.getType();
            if (isPrimitive(type)){
                Class<?> boxed = boxPrimitiveClass(type);
                if (boxed.getSimpleName().equals("Double")){
                    BigDecimal bd = (BigDecimal) value;
                    value = bd.doubleValue();
                }else
                    value = boxed.cast(value);
            }else if (value.getClass().getSimpleName().equals("Date")) {
                Date dateObj = (Date) value;
                value = dateObj.toString();
            }
            field.set(obj, value);
        }

        return obj;
    }

    public static List<String[]> getListFromResult(ResultSet rs){

        List<String[]> result = new ArrayList<>();

        String[] headers = getResultHeaders(rs);
        result.add(headers);

       try{
           while(rs.next()){
               String[] row  = new String[headers.length];
               int num = 0;
               for (String s: headers) {
                   row[num] = rs.getObject(s.split(":")[1]).toString();
                   num++;
               }
               result.add(row);
           }
       }catch(SQLException e){
           e.printStackTrace();
       }
        return result;
    }

    //Helper Classes ------------------------------------------------
    public static boolean isPrimitive(Class<?> type){
        return (type == int.class || type == long.class || type == double.class || type == float.class
                || type == boolean.class || type == byte.class || type == char.class || type == short.class);
    }

    public static Class<?> boxPrimitiveClass(Class<?> type){
        if (type == int.class) {
            return Integer.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == double.class) {
            return Double.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == char.class) {
            return Character.class;
        } else if (type == short.class) {
            return Short.class;
        } else {
            String string = "class '" + type.getName() + "' is not a primitive";
            throw new IllegalArgumentException(string);
        }
    }

    public static String[] getResultHeaders(ResultSet rs) {
        try {
            //Retrieve metadata
            ResultSetMetaData rsMd = rs.getMetaData();

            //Parse metadata
            int count = rsMd.getColumnCount();
            String[] headers = new String[count];

            for (int i = 0; i < count; i++) {
               headers[i] = rsMd.getTableName(i + 1) + ":" + rsMd.getColumnName(i + 1);
            }
            return headers;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
