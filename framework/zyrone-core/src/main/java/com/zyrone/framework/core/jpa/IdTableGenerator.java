package com.zyrone.framework.core.jpa;

import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.type.Type;

/**
 *
 * @author Jason Phang
 */
public class IdTableGenerator extends TableGenerator {

    @Override
    public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
        if (params.get("table_name") == null) {
            params.put("table_name", "ZRN_SEQUENCE_GENERATOR");
        }

        if (params.get("segment_column_name") == null) {
            params.put("segment_column_name", "ID_NAME");
        }

        if (params.get("value_column_name") == null) {
            params.put("value_column_name", "ID_VAL");
        }

        if (params.get("increment_size") == null) {
            params.put("increment_size", 50);
        }
        super.configure(type, params, dialect);
    }

}
