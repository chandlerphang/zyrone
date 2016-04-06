package com.zyrone.xml.merge.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.zyrone.xml.merge.AbstractMergeHandler;

/**
 * 通过XPath找到的并且是元素的节点，如果同时存在于文档1和文档2中（有相同的id或name属性值），
 * 则用文档2的节点替换文档1中的相同节点。如果文档2有而文档1没有，则将文档2中的节点插入到文档2中
 * 
 * @author Jason Phang
 */
public class ReplaceOrInsertElement extends AbstractMergeHandler {

	@Override
	protected void merge(List<Node> nodeList1, List<Node> nodeList2, List<Node> usedNodes) {
		if(CollectionUtils.isEmpty(nodeList1) || CollectionUtils.isEmpty(nodeList2)) {
			return;
		}
		Element parentElement = (Element)nodeList1.get(0).getParentNode();
		for(Iterator<Node> it = nodeList2.iterator(); it.hasNext(); ) {
			Node node = it.next();
			if(node.getNodeType() == Node.ELEMENT_NODE && !isUsed(usedNodes, node)) {
				if(LOG.isDebugEnabled()) {
					StringBuilder sb = new StringBuilder();
	                sb.append("找到节点: ");
	                sb.append(node.getNodeName()).append(" (");
	                int attrLength = node.getAttributes().getLength();
	                for (int x = 0; x < attrLength; x++) {
	                    sb.append(node.getAttributes().item(x).getNodeName());
	                    sb.append("=");
	                    sb.append(node.getAttributes().item(x).getNodeValue());
	                    sb.append(", ");
	                }
	                if(attrLength > 0) {
	                	sb.delete(sb.length() - 2, sb.length());
	                }
	                sb.append(")");
	                LOG.debug(sb.toString());
				}
				// 先试试看是不是要进行替换，只有在没有发生替换的情况下才会将节点插到文档1中
				if(!tryToReplace(nodeList1, node, usedNodes) && !checkNode(nodeList1, node, usedNodes)) {
					if(LOG.isDebugEnabled()) {
						LOG.debug("插入文档2的 {} 到文档1 {} 中", node.getNodeName(), parentElement.getNodeName());
					}
					
					// 拷贝找到的节点并插到文档1中
					Node newNode = parentElement.getOwnerDocument().importNode(node, true);
					parentElement.appendChild(newNode);
					usedNodes.add(node);
				}
			}
		}
	}
	
	private boolean isUsed(List<Node> usedNodes, Node node) {
		for (Node usedNode : usedNodes) {
		    if (NODE_COMPARATOR.compare(usedNode, node) == 0) {
		        return true;
		    }
		}
		
		return false;
	}
	
	protected boolean checkNode(List<Node> nodeList1, Node testNode, List<Node> usedNodes) {
		// 相等的节点不要插入防止重复
		for(Iterator<Node> it = nodeList1.iterator(); it.hasNext(); ) {
			Node node = it.next();
			if (node.isEqualNode(testNode)) {
				if(LOG.isDebugEnabled()) {
					LOG.debug("跳过相同节点: {}", testNode.toString());
				}
                usedNodes.add(testNode);
                return true;
            }
		}
		return false;
	}
	
	protected boolean tryToReplace(List<Node> nodeList1, Node testNode, List<Node> usedNodes) {
		if(doReplace(nodeList1, testNode, "id", usedNodes)) {
			return true;
		}
		
		if(doReplace(nodeList1, testNode, "name", usedNodes)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 在nodeList1中找与testNode有相同属性attrName的节点，如果找到，则用testNode替换这个节点
	 */
	protected boolean doReplace(List<Node> nodeList1, Node testNode, String attrName, List<Node> usedNodes) {
		if(testNode.getAttributes().getNamedItem(attrName) == null) {
			return false;
		}
		
		String attrValue = testNode.getAttributes().getNamedItem(attrName).getNodeValue();
		for(Iterator<Node> it = nodeList1.iterator(); it.hasNext(); ) {
			Node node = it.next();
			if(node.getNodeType() == Node.ELEMENT_NODE && node.getAttributes().getNamedItem(attrName) != null) {
				if( attrValue.equals(node.getAttributes().getNamedItem(attrName).getNodeValue()) ) {
					if(LOG.isDebugEnabled()) {
						LOG.debug("节点 {} ({}={}) 匹配, 执行替换", testNode.getNodeName(), attrName, attrValue);
					}
					
					Node newNode = node.getOwnerDocument().importNode(testNode, true);
					node.getParentNode().replaceChild(newNode, node);
					// 记录testNode已经被处理过了
		            usedNodes.add(testNode);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private final static Logger LOG = LoggerFactory.getLogger(ReplaceOrInsertElement.class);
	private static final Comparator<Node> NODE_COMPARATOR = new Comparator<Node>() {
		public int compare(Node arg0, Node arg1) {
			int response = -1;
		    if (arg0.isSameNode(arg1)) {
		        response = 0;
		    }
		    if (response != 0) {
		    	boolean eof = false;
		    	Node parentNode = arg0;
		    	while (!eof) {
		    		parentNode = parentNode.getParentNode();
		    		if (parentNode == null) {
		    			eof = true;
		    		} else if (arg1.isSameNode(parentNode)) {
		    			response = 0;
		    			eof = true;
		    		}
		    	}
		    }
		    return response;
		}
	};
}
