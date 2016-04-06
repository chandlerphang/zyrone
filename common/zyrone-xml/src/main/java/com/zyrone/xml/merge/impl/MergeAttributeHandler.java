package com.zyrone.xml.merge.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.zyrone.xml.merge.AbstractMergeHandler;

/**
 * 合并某元素的属性
 * 
 * @author Jason Phang
 */
public class MergeAttributeHandler extends AbstractMergeHandler {

	@Override
	protected void merge(List<Node> nodeList1, List<Node> nodeList2, List<Node> usedNodes) {
		if (CollectionUtils.isEmpty(nodeList1) || CollectionUtils.isEmpty(nodeList2)) {
            return;
        }
		
        Node node1 = nodeList1.get(0);
        Node node2 = nodeList2.get(0);
        NamedNodeMap attributes2 = node2.getAttributes();
        Comparator<Node> nameCompare = new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return o1.getNodeName().compareTo(o2.getNodeName());
			}
        };
        Node[] tempNodes = new Node[usedNodes.size()];
        tempNodes = usedNodes.toArray(tempNodes);
        Arrays.sort(tempNodes, nameCompare);
        int length = attributes2.getLength();
        for (int j = 0; j < length; j++) {
            Node temp = attributes2.item(j);
            int pos = Arrays.binarySearch(tempNodes, temp, nameCompare);
            if (pos < 0) {
                Attr clone = (Attr) temp.cloneNode(true);
                ((Element) node1).setAttributeNode((Attr) node1.getOwnerDocument().importNode(clone, true));
            }
        }
        
        usedNodes.addAll(nodeList2);
	}

}
