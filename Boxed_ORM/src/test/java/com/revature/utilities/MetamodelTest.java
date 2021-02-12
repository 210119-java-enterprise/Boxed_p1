package com.revature.utilities;

import java.util.List;

public class MetamodelTest {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        for (Metamodel<?> metamodel : config.getMetamodels()) {
            System.out.printf("Printing metamodel for class: %s\n", metamodel.getClassName());
            PrimaryKeyField pkField = metamodel.getPrimaryKey();
            List<ColumnField> columnFields = metamodel.getColumns();
            List<ForeignKeyField> foreignFields = metamodel.getForeignKeys();

            System.out.printf("\tFound a primary key field named %s of type %s, which maps to the column with the name %s\n",
                    pkField.getName(), pkField.getType(), pkField.getColumnName());

            for (ColumnField columnField : columnFields) {
                System.out.printf("\tFound a column field named: %s of type %s, which maps to the column with the name: %s\n",
                        columnField.getName(), columnField.getType(), columnField.getColumnName());
            }

            for (ForeignKeyField foreignKeyField : foreignFields) {
                System.out.printf("\tFound a foreign key field named %s of type %s, which maps to the column with the name: %s\n",
                        foreignKeyField.getName(), foreignKeyField.getType(), foreignKeyField.getColumnName());
            }

            System.out.println();
        }
    }
}
