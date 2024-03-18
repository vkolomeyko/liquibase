package liquibase.ext.bigquery.datatype.core;

import liquibase.change.core.LoadDataChange;
import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.core.VarcharType;
import liquibase.ext.bigquery.database.BigqueryDatabase;

import static liquibase.ext.bigquery.database.BigqueryDatabase.BIGQUERY_PRIORITY_DATABASE;


@DataTypeInfo(
        name = "string",
        minParameters = 0,
        maxParameters = 0,
        priority = BIGQUERY_PRIORITY_DATABASE,
        aliases = { "varchar", "clob", "java.lang.String" }
)
public class StringDataTypeBigQuery extends VarcharType {
    public StringDataTypeBigQuery() {
    }

    @Override
    public boolean supports(Database database) {
        return database instanceof BigqueryDatabase;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof BigqueryDatabase) {

            DatabaseDataType type = new DatabaseDataType("STRING", this.getParameters());
            if (this.getParameters().length == 0) {
                type.setType("STRING");
            } else {
                String firstParameter = String.valueOf(this.getParameters()[0]);
                int stringSize = Integer.parseInt(firstParameter);
                if (stringSize == 65535) {
                    type.setType("STRING");
                }
            }
            return type;
        } else {
            return super.toDatabaseDataType(database);
        }

    }

    @Override
    public String objectToSql(Object value, Database database) {
        String ret =  super.objectToSql(value, database);
        if (ret.contains("\n")) {
            return "''" + ret + "''";
        } else {
            return ret;
        }
    }

    public LoadDataChange.LOAD_DATA_TYPE getLoadTypeName() {
        return LoadDataChange.LOAD_DATA_TYPE.STRING;
    }
}
