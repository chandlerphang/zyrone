package com.zyrone.framework.core.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

/**
 * 持久化单元后置处理器，在这里为PersistenceUnitInfo填充连接信息
 *
 * @author Jason Phang
 */
public class JPAPropertiesPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {

    protected Map<String, String> persistenceUnitProperties = new HashMap<String, String>();
    /** 自定义点 */
    protected Map<String, String> overrideProperties = new HashMap<String, String>();

    @Value("${zrnPU.hibernate.hbm2ddl.auto}")
    protected String zyronePUHibernateHbm2ddlAuto;
    @Value("${zrnPU.hibernate.dialect}")
    protected String zyronePUHibernateDialect;
    @Value("${zrnPU.hibernate.show_sql}")
    protected String zyronePUHibernateShow_sql;
    @Value("${zrnPU.hibernate.cache.use_second_level_cache}")
    protected String zyronePUHibernateCacheUse_second_level_cache;
    @Value("${zrnPU.hibernate.cache.use_query_cache}")
    protected String zyronePUHibernateCacheUse_query_cache;
    @Value("${zrnPU.hibernate.hbm2ddl.import_files}")
    protected String zyronePUHibernateHbm2ddlImport_files;
    @Value("${zrnPU.hibernate.hbm2ddl.import_files_sql_extractor}")
    protected String zyronePUHibernateHbm2ddlImport_files_sql_extractor;

    @PostConstruct
    public void populatePresetProperties() {
        if (!zyronePUHibernateHbm2ddlAuto.startsWith("${")) {
        	persistenceUnitProperties.put("zrnPU.hibernate.hbm2ddl.auto", zyronePUHibernateHbm2ddlAuto);
        }
        if (!zyronePUHibernateDialect.startsWith("${")) {
        	persistenceUnitProperties.put("zrnPU.hibernate.dialect", zyronePUHibernateDialect);
        }
        if (!zyronePUHibernateShow_sql.startsWith("${")) {
        	persistenceUnitProperties.put("zrnPU.hibernate.show_sql", zyronePUHibernateShow_sql);
        }
        if (!zyronePUHibernateCacheUse_second_level_cache.startsWith("${")) { 
        	persistenceUnitProperties.put("zrnPU.hibernate.cache.use_second_level_cache", zyronePUHibernateCacheUse_second_level_cache);
        }
        if (!zyronePUHibernateCacheUse_query_cache.startsWith("${")) {
        	persistenceUnitProperties.put("zrnPU.hibernate.cache.use_query_cache", zyronePUHibernateCacheUse_query_cache);
        }
        if (!zyronePUHibernateHbm2ddlImport_files.startsWith("${")) {
        	persistenceUnitProperties.put("zrnPU.hibernate.hbm2ddl.import_files", zyronePUHibernateHbm2ddlImport_files);
        }
        if (!zyronePUHibernateHbm2ddlImport_files_sql_extractor.startsWith("${")) {
        	persistenceUnitProperties.put("zrnPU.hibernate.hbm2ddl.import_files_sql_extractor", zyronePUHibernateHbm2ddlImport_files_sql_extractor);
        }

        persistenceUnitProperties.putAll(overrideProperties);
    }
    
    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        if (persistenceUnitProperties != null) {
            String puName = pui.getPersistenceUnitName() + ".";
            Set<String> keys = persistenceUnitProperties.keySet();
            Properties props = pui.getProperties();

            for (String key : keys) {
                if (key.startsWith(puName)){
                    String value = persistenceUnitProperties.get(key);
                    String newKey = key.substring(puName.length());
                    if ("null".equalsIgnoreCase(value)){
                        props.remove(newKey);
                    } else if (!StringUtils.isBlank(value)) {
                        props.put(newKey, value);
                    }
                }
            }
            pui.setProperties(props);
        }
    }
    
    public void setPersistenceUnitProperties(Map<String, String> properties) {
        this.overrideProperties = properties;
    }
}
