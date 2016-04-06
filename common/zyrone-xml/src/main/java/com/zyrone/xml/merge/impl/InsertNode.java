package com.zyrone.xml.merge.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.zyrone.xml.merge.AbstractMergeHandler;

/**
 * 将文档2的节点插入到文档1中， 节点可以是属性，元素和文本等。
 * 
 * @author Jason Phang
 */
public class InsertNode extends AbstractMergeHandler {

	@Override
	protected void merge(List<Node> nodeList1, List<Node> nodeList2, List<Node> usedNodes) {
		if (CollectionUtils.isEmpty(nodeList1) || CollectionUtils.isEmpty(nodeList2)) {
            return;
        }
		
		Node parentElement = nodeList1.get(0).getParentNode();
        for (Node aNodeList2 : nodeList2) {
        	Node tempNode = parentElement.getOwnerDocument().importNode(aNodeList2, true);
        	if (LOG.isDebugEnabled()) {
                StringBuffer sb = new StringBuffer();
                sb.append("找到待插入的节点: ");
                sb.append(tempNode.getNodeName()).append(" (");
                int attrLength = tempNode.getAttributes().getLength();
                for (int x = 0; x < attrLength; x++) {
                    sb.append(tempNode.getAttributes().item(x).getNodeName());
                    sb.append("=");
                    sb.append(tempNode.getAttributes().item(x).getNodeValue());
                    sb.append(", ");
                }
                if(attrLength > 0) {
                	sb.delete(sb.length() - 2, sb.length());
                }
                sb.append(")");
                LOG.debug(sb.toString());
            }
            if (LOG.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("插入到父节点: ");
                sb.append(parentElement.getNodeName()).append(" (");
                int attrLength = parentElement.getAttributes().getLength();
                for (int x = 0; x < attrLength; x++) {
                    sb.append(parentElement.getAttributes().item(x).getNodeName());
                    sb.append("=");
                    sb.append(parentElement.getAttributes().item(x).getNodeValue());
                    sb.append(", ");
                }
                if(attrLength > 0) {
                	sb.delete(sb.length() - 1, sb.length());
                }
                sb.append(")");
                LOG.debug(sb.toString());
            }
            parentElement.appendChild(tempNode);
        }

        usedNodes.addAll(nodeList2);
        usedNodes.add(nodeList2.get(0).getParentNode());
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(InsertNode.class);
}
