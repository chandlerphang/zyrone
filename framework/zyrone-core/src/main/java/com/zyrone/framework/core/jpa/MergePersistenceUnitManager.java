package com.zyrone.framework.core.jpa;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;

/**
 * 提供两个配置点zyronePersistenceXmlLocations和zyroneDataSources
 * 
 * @author Jason Phang
 */
public class MergePersistenceUnitManager extends DefaultPersistenceUnitManager {

    //private static final Logger LOG = LoggerFactory.getLogger(MergePersistenceUnitManager.class);

    protected HashMap<String, PersistenceUnitInfo> mergedPus = new HashMap<String, PersistenceUnitInfo>();

    @Resource(name="zrnPersistenceXmlLocations")
    protected Set<String> zyronePersistenceXmlLocations;

    @Resource(name="zrnDataSources")
    protected Map<String, DataSource> zyroneDataSources;
    
    @PostConstruct
    public void configureMergedItems() {
        String[] tempLocations;
        try {
            Field persistenceXmlLocations = DefaultPersistenceUnitManager.class.getDeclaredField("persistenceXmlLocations");
            persistenceXmlLocations.setAccessible(true);
            tempLocations = (String[]) persistenceXmlLocations.get(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (String location : tempLocations) {
            if (!location.endsWith("/persistence.xml")) {
                zyronePersistenceXmlLocations.add(location);
            }
        }
        setPersistenceXmlLocations(zyronePersistenceXmlLocations.toArray(new String[zyronePersistenceXmlLocations.size()]));
        if (!zyroneDataSources.isEmpty()) {
            setDataSources(zyroneDataSources);
        }
    }

    protected MutablePersistenceUnitInfo getMergedUnit(String persistenceUnitName, MutablePersistenceUnitInfo newPU) {
        if (!mergedPus.containsKey(persistenceUnitName)) {
            mergedPus.put(persistenceUnitName, newPU);
        }
        return (MutablePersistenceUnitInfo) mergedPus.get(persistenceUnitName);
    }

    @Override
    public void preparePersistenceUnitInfos() {
    	super.preparePersistenceUnitInfos();
    }
    
    @Override
    protected boolean isPersistenceUnitOverrideAllowed() {
        return true;
    }
    
    @Override
    protected void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo newPU) {
        super.postProcessPersistenceUnitInfo(newPU);
        String persistenceUnitName = newPU.getPersistenceUnitName();
        MutablePersistenceUnitInfo pui = getMergedUnit(persistenceUnitName, newPU);

        List<String> managedClassNames = newPU.getManagedClassNames();
        for (String managedClassName : managedClassNames){
            if (!pui.getManagedClassNames().contains(managedClassName)) {
                pui.addManagedClassName(managedClassName);
            }
        }
        List<String> mappingFileNames = newPU.getMappingFileNames();
        for (String mappingFileName : mappingFileNames) {
            if (!pui.getMappingFileNames().contains(mappingFileName)) {
                pui.addMappingFileName(mappingFileName);
            }
        }
        pui.setExcludeUnlistedClasses(newPU.excludeUnlistedClasses());
        for (URL url : newPU.getJarFileUrls()) {
            if (!pui.getJarFileUrls().contains(url) && !pui.getPersistenceUnitRootUrl().equals(url)) {
                pui.addJarFileUrl(url);
            }
        }
        if (pui.getProperties() == null) {
            pui.setProperties(newPU.getProperties());
        } else {
            Properties props = newPU.getProperties();
            if (props != null) {
                for (Object key : props.keySet()) {
                    pui.getProperties().put(key, props.get(key));
                }
            }
        }
        
        if (newPU.getJtaDataSource() != null) {
            pui.setJtaDataSource(newPU.getJtaDataSource());
        }
        if (newPU.getNonJtaDataSource() != null) {
            pui.setNonJtaDataSource(newPU.getNonJtaDataSource());
        }
        
        pui.setTransactionType(newPU.getTransactionType());
        if (newPU.getPersistenceProviderClassName() != null) {
            pui.setPersistenceProviderClassName(newPU.getPersistenceProviderClassName());
        }
        if (newPU.getPersistenceProviderPackageName() != null) {
            pui.setPersistenceProviderPackageName(newPU.getPersistenceProviderPackageName());
        }
    }

    @Override
    public PersistenceUnitInfo obtainPersistenceUnitInfo(String persistenceUnitName) {
        return mergedPus.get(persistenceUnitName);
    }

    @Override
    public PersistenceUnitInfo obtainDefaultPersistenceUnitInfo() {
        throw new IllegalStateException("不支持默认的PersistenceUnitInfo. 必须指定持久化单元的名字.");
    }

}
