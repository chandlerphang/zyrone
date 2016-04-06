package com.zyrone.xml.merge.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.w3c.dom.Node;

import com.zyrone.xml.merge.AbstractMergeHandler;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jason Phang
 */
public class MergeNodeValueHandler extends AbstractMergeHandler {

    @Override
    protected void merge(List<Node> nodeList1, List<Node> nodeList2, List<Node> usedNodes) {
        if (CollectionUtils.isEmpty(nodeList1) || CollectionUtils.isEmpty(nodeList2)) {
            return;
        }
        
        Node node1 = nodeList1.get(0);
        Node node2 = nodeList2.get(0);
        
        Set<String> finalItems = getMergedNodeValues(node1, node2);
        StringBuilder sb = new StringBuilder();
        Iterator<String> itr = finalItems.iterator();
        while (itr.hasNext()) {
            sb.append(itr.next());
            if (itr.hasNext()) {
                sb.append(getDelimiter());
            }
        }
        node1.setNodeValue(sb.toString());
        
        usedNodes.addAll(nodeList2);
    }

    /** 合并node1和node2的节点值 */
    protected Set<String> getMergedNodeValues(Node node1, Node node2) {
        String[] items1 = node1.getNodeValue().split(getRegEx());
        String[] items2 = node2.getNodeValue().split(getRegEx());
        Set<String> finalItems = new LinkedHashSet<String>();
        for (String anItems1 : items1) {
            finalItems.add(anItems1.trim());
        }
        for (String anItems2 : items2) {
            finalItems.add(anItems2.trim());
        }
        return finalItems;
    }

    public String getDelimiter() {
        return " ";
    }

    public String getRegEx() {
        return "[\\s\\n\\r]+";
    }
}
