package com.zyrone.framework.core.ehcache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.zyrone.xml.merge.XmlMergeHandlerChain;
import com.zyrone.xml.merge.XmlMerger;
import com.zyrone.xml.merge.impl.HandlerChainConfigurer;
import com.zyrone.xml.merge.support.NamedInputStream;

import net.sf.ehcache.CacheManager;

public class EhcacheManagerFactoryBean extends org.springframework.cache.ehcache.EhCacheManagerFactoryBean implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    private static final String DEFAULT_XML_MERGE_HANDLER_CHAIN_PATH = "EhcacheMergeHandlerChain.properties";
    private static final Properties defaultXmlMergeHandlerChain;

    static {
		try {
			ClassPathResource resource = new ClassPathResource(DEFAULT_XML_MERGE_HANDLER_CHAIN_PATH, EhcacheManagerFactoryBean.class);
			defaultXmlMergeHandlerChain = PropertiesLoaderUtils.loadProperties(resource);
		} catch(IOException ex) {
			throw new IllegalStateException("failed to load file 'EhcacheMergeHandlerChain.properties': " + ex.getMessage());
		}
	}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @javax.annotation.Resource(name="zrnCacheConfigLocations")
    protected Set<String> distCacheConfigLocations;

    protected List<Resource> cacheConfigLocations;

    @Override
    public void destroy() {
        super.destroy();
        try {
            CacheManager cacheManager = getObject();
            Field cacheManagerTimer = CacheManager.class.getDeclaredField("cacheManagerTimer");
            cacheManagerTimer.setAccessible(true);
            Object failSafeTimer = cacheManagerTimer.get(cacheManager);
            Field timer = failSafeTimer.getClass().getDeclaredField("timer");
            timer.setAccessible(true);
            Object time = timer.get(failSafeTimer);
            Field thread = time.getClass().getDeclaredField("thread");
            thread.setAccessible(true);
            Thread item = (Thread) thread.get(time);
            item.setContextClassLoader(Thread.currentThread().getContextClassLoader().getParent());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void configureMergedItems() {
        List<Resource> temp = new ArrayList<Resource>();
        if (distCacheConfigLocations != null && !distCacheConfigLocations.isEmpty()) {
            for (String location : distCacheConfigLocations) {
                temp.add(applicationContext.getResource(location));
            }
        }
        if (cacheConfigLocations != null && !cacheConfigLocations.isEmpty()) {
            for (Resource resource : cacheConfigLocations) {
                temp.add(resource);
            }
        }
        try {
            NamedInputStream[] sources = new NamedInputStream[temp.size()];
            int j=0;
            for (Resource resource : temp) {
                sources[j] = new NamedInputStream(resource.getInputStream(), resource.getURL().toString());
                j++;
            }
            
            XmlMergeHandlerChain chain = HandlerChainConfigurer.config(defaultXmlMergeHandlerChain);
            XmlMerger xmlMerger = new XmlMerger(chain);
            NamedInputStream nis = xmlMerger.merge(sources);
            
            setConfigLocation(toResource(nis));
        } catch (Exception e) {
            throw new FatalBeanException("failed to merge cache configuration files", e);
        }
    }
    
    private Resource toResource(NamedInputStream nis) throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean eof = false;
		while (!eof) {
		     int b = nis.read();
		     if (b == -1) {
		         eof = true;
		     } else {
		         baos.write(b);
		     }
		}
		
		return new ByteArrayResource(baos.toByteArray());
    }

    public void setCacheConfigLocations(List<Resource> cacheConfigLocations) throws BeansException {
        this.cacheConfigLocations = cacheConfigLocations;
    }
}
