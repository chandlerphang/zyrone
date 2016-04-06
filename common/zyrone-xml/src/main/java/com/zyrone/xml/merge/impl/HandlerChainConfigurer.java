package com.zyrone.xml.merge.impl;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyrone.util.Assert;
import com.zyrone.xml.merge.MergeHandler;
import com.zyrone.xml.merge.XmlMergeHandlerChain;

public class HandlerChainConfigurer {
	
	private static final Logger LOG = LoggerFactory.getLogger(HandlerChainConfigurer.class);
	
	public static XmlMergeHandlerChain config(Properties props) {
		Assert.notNull(props);
		ArrayList<MergeHandler> handlers = new ArrayList<MergeHandler>();
        for (String key : props.stringPropertyNames()) {
            if (key.startsWith("handler.")) {
            	MergeHandler tempHandler;
				try {
					tempHandler = (MergeHandler) Class.forName(props.getProperty(key)).newInstance();
				} catch (InstantiationException e) {
					LOG.error("忽略合并处理器 {}, 因为无法加载该合并处理器类", props.getProperty(key));
					continue;
				} catch (IllegalAccessException e) {
                    LOG.error("忽略合并处理器 {}, 因为无法加载该合并处理器类", props.getProperty(key));
                    continue;
                } catch (ClassNotFoundException e) {
                    LOG.error("忽略合并处理器 {}, 因为无法加载该合并处理器类", props.getProperty(key));
                    continue;
                }
                String name = key.substring(8, key.length());
                tempHandler.setName(name);
                
                String priority = props.getProperty("priority." + name);
                if(!StringUtils.isBlank(priority)) {
                	tempHandler.setPriority(Integer.parseInt(priority));    
                } else {
                	LOG.warn("没有找到处理器 {} 的priority配置", name);
                }
                
                String xpath = props.getProperty("xpath." + name);
                if(!StringUtils.isBlank(xpath)) {
                	tempHandler.setXPath(xpath);
                } else {
                	LOG.warn("没有找到处理器 {} 的xpath配置", name);
                }
                
                handlers.add(tempHandler);
            }
        }
        
        return new XmlMergeHandlerChain(handlers.toArray(new MergeHandler[handlers.size()]));
	}

}
