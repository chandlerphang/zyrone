package com.zyrone.xml.merge.impl;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

public class NonEmptyReplaceOrInsertElement extends ReplaceOrInsertElement {

    @Override
    protected boolean doReplace(List<Node> nodeList1, Node testNode, String attrName, List<Node> usedNodes) {
        if (testNode.getAttributes().getNamedItem(attrName) == null) {
            return false;
        }
        
        String attrValue = testNode.getAttributes().getNamedItem(attrName).getNodeValue();
		for(Iterator<Node> it = nodeList1.iterator(); it.hasNext(); ) {
			Node node = it.next();
			if(node.getNodeType() == Node.ELEMENT_NODE && node.getAttributes().getNamedItem(attrName) != null) {
				if( attrValue.equals(node.getAttributes().getNamedItem(attrName).getNodeValue()) ) {
					evaluate:{
						if (!testNode.hasChildNodes()) {
		                    break evaluate;
		                }
						Node newNode = node.getOwnerDocument().importNode(testNode, true);
						node.getParentNode().replaceChild(newNode, node);
		            }
		            usedNodes.add(testNode);
		            return true;
				}
			}
		}
		
		return false;
    }
}
