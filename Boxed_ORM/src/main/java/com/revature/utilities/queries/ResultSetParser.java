package com.revature.utilities.queries;

import com.revature.annotations.Column;
import com.revature.annotations.Column_PK;

import java.lang.reflect.Field;
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
    public static Object getObjFromResult(Object obj, ResultSet rs)
        throws SQLException, IllegalAccessException, IllegalArgumentException{
        System.out.println("\n\n");

        Class<?> clazz = obj.getClass();

        for (Field field: clazz.getDeclaredFields()) {
            System.out.println("DeclaredFields: "+ field.getName());
            //Allows access to private fields
            field.setAccessible(true);    //?

            Column column = field.getAnnotation(Column.class);
            System.out.print("\t" + column.columnName());

            Object value = rs.getObject(column.columnName());
            Class<?> type = field.getType();
            System.out.print( ", " + value + ", " + type + "\n");

            if (isPrimitive(type)){
                System.out.println(" is primitive adjusting ... ");
                Class<?> boxed = boxPrimitiveClass(type);
                value = boxed.cast(value);
                System.out.println(" new value: " + value);
            }

            field.set(obj, value);
            System.out.println("Final representation: " + field.get(obj) + "\n");
        }

        return obj;
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
}
