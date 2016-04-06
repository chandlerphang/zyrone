package com.zyrone.xml.merge.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

/**
 * 去掉spring配置中的schemalocation的版本号
 * 
 * @author Jason Phang
 */
public class SpringSchemaLocationValueMerge extends MergeNodeValueHandler {

    @Override
    protected Set<String> getMergedNodeValues(Node node1, Node node2) {
        String node1Values = getSanitizedValue(node1.getNodeValue());
        String node2Values = getSanitizedValue(node2.getNodeValue());
        
        Set<String> finalItems = new LinkedHashSet<String>();
        for (String node1Value : node1Values.split(getRegEx())) {
            finalItems.add(node1Value.trim());
        }
        for (String node2Value : node2Values.split(getRegEx())) {
        	finalItems.add(node2Value.trim());
        }
        return finalItems;
    }
    
    @Override 
    public String getDelimiter() {
        return " ";
    }

    protected String getSanitizedValue(String attributeValue) {
        Pattern springVersionPattern = Pattern.compile("(spring-\\w*-[0-9]\\.[0-9]\\.xsd)");
        Matcher versionMatcher = springVersionPattern.matcher(attributeValue);
        while (versionMatcher.find()) {
            String match = versionMatcher.group();
            String replacement = match.replaceAll("-[0-9]\\.[0-9]", "");
            attributeValue = attributeValue.replaceAll(match, replacement);
            
            LOG.debug("找到 {}, 替换为 {}", match, replacement);
        }
        return attributeValue;
    }
    
    private final static Logger LOG = LoggerFactory.getLogger(SpringSchemaLocationValueMerge.class);
    
}
