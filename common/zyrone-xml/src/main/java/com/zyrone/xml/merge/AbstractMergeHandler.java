package com.zyrone.xml.merge;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractMergeHandler implements MergeHandler, Comparable<MergeHandler> {

	@Override
	public void merge(Document doc1, Document doc2, List<Node> usedNodes) throws XPathExpressionException {
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		
		String[] xPaths = getXPath().split(" ");
        List<Node> nodeList1 = new ArrayList<Node>();
        List<Node> nodeList2 = new ArrayList<Node>();
        for (String xPathVal : xPaths) {
            NodeList temp1 = (NodeList) xPath.evaluate(xPathVal, doc1, XPathConstants.NODESET);
            if (temp1 != null) {
                int length = temp1.getLength();
                for (int j=0;j<length;j++) {
                    nodeList1.add(temp1.item(j));
                }
            }
            NodeList temp2 = (NodeList) xPath.evaluate(xPathVal, doc2, XPathConstants.NODESET);
            if (temp2 != null) {
                int length = temp2.getLength();
                for (int j=0;j<length;j++) {
                    nodeList2.add(temp2.item(j));
                }
            }
        }
	    
	    if (nodeList1 != null && nodeList2 != null) {
	        merge(nodeList1, nodeList2, usedNodes);
	    }
	}
	
	protected abstract void merge(List<Node> nodeList1, List<Node> nodeList2, List<Node> usedNodes);

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority; 
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getXPath() {
		return xPath;
	}

	@Override
	public void setXPath(String xpath) {
		this.xPath = xpath;
	}
	
	@Override
	public int compareTo(MergeHandler o) {
		return priority - o.getPriority();
	}
	
	
	protected int priority;
	protected String xPath;
	protected String name;

}
