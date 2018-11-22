package com.base.sql;

import com.base.model.ModelHelper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author LiChong
 * @ClassName ${type_name}
 * @Description
 * @date 2018/1/22
 */
public class ModelRowMapper<T> implements RowMapper<T> {

    protected Class<T> clazz = null;

    public ModelRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }


    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= columnCount; i++) {
            String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
            Object obj = getColumnValue(rs, i);
            ModelHelper.set(t, key, obj);
        }
        return t;
    }

    /**
     * Determine the key to use for the given column in the column Map.
     *
     * @param columnName the column name as returned by the ResultSet
     * @return the column key to use
     * @see ResultSetMetaData#getColumnName
     */
    protected String getColumnKey(String columnName) {
        return columnName;
    }

    /**
     * Retrieve a JDBC object value for the specified column.
     * <p>The default implementation uses the {@code getObject} method.
     * Additionally, this implementation includes a "hack" to get around Oracle
     * returning a non standard object for their TIMESTAMP datatype.
     *
     * @param rs    is the ResultSet holding the data
     * @param index is the column index
     * @return the Object returned
     * @see JdbcUtils#getResultSetValue
     */
    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }
}
