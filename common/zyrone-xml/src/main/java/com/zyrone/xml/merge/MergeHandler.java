package com.zyrone.xml.merge;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface MergeHandler {
	
    void merge(Document doc1, Document doc2, List<Node> usedNodes) throws XPathExpressionException;
    
    int getPriority();
    
    void setPriority(int priority);
    
    String getName();
    
    void setName(String name);
    
    String getXPath();

    void setXPath(String xpath);
	
}
