package com.zyrone.xml.merge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 针对xml文档中特定的节点，程序中会有特定的合并处理器来对该节点进行处理，
 * 许许多多的合并处理器依据优先级的先后关系组成一个处理器链，最终完成对
 * 两个文档进行合并的任务。
 * 
 * @author Jason Phang
 */
public class XmlMergeHandlerChain {
    
	private static final Logger LOG = LoggerFactory.getLogger(XmlMergeHandlerChain.class);
	private MergeHandler[] handlers;
	private List<MergeHandler> handlerList;
	
	public XmlMergeHandlerChain(MergeHandler... handlers) {
		this.handlers = handlers;
		if(this.handlers != null) {
			Arrays.sort(this.handlers, HANDLER_COMPARATOR);
		}
    }
	
	public void addMergeHandler(MergeHandler handler) {
		if(handler != null) {
			initHandlerList().add(handler);
		}
	}
	
	public void addMergeHandler(MergeHandler... handlers) {
		if (handlers != null && handlers.length > 0) {
			initHandlerList().addAll(Arrays.asList(handlers));
		}
	}
	
	private List<MergeHandler> initHandlerList() {
		if (this.handlerList == null) {
			this.handlerList = new ArrayList<MergeHandler>();
			if (this.handlers != null) {
				this.handlerList.addAll(Arrays.asList(this.handlers));
			}
		}
		this.handlers = null;
		return this.handlerList;
	}
	
	public MergeHandler[] getHandlers() {
		if (this.handlers == null && this.handlerList != null) {
			Collections.sort(this.handlerList, HANDLER_COMPARATOR);
			this.handlers = this.handlerList.toArray(new MergeHandler[this.handlerList.size()]);
		}
		return this.handlers;
	}

	private Comparator<MergeHandler> HANDLER_COMPARATOR = new Comparator<MergeHandler>() {
		@Override
		public int compare(MergeHandler o1, MergeHandler o2) {
			return o1.getPriority() - o2.getPriority();
		}
	};
    
    /**
     * 将文档2的内容合并到文档1，然后将修改后的文档1返回。每个节点的具体合并行为依赖于合并处理器的配置。
     * 注意！合并的过程中，文档2的内容也可能发生修改
     * 
     * @param doc1 待合并的目标文档
     * @param doc2 提供合并内容的文档
     * @return 返回对文档1的引用，即doc1
     * @throws XmlMergeException 任一合并处理器在处理过程中发生异常都会抛出该异常
     */
    public Document merge(Document doc1, Document doc2) throws XmlMergeException {
    	 try {
    		 // 用来记录doc2中已经被处理过的节点
             List<Node> usedNodes = new ArrayList<Node>();
             for (MergeHandler handler : getHandlers()) {
                 if (LOG.isDebugEnabled()) {
                     LOG.debug("使用处理器: " + handler.getXPath());
                 }
                 
                 handler.merge(doc1, doc2, usedNodes);
             }
             
             return doc1;
         } catch (Exception e) {
             throw new XmlMergeException(e);
         }
    }
}
