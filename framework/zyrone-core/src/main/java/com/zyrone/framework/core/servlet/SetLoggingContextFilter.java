package com.zyrone.framework.core.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zyrone.framework.core.util.SetLoggingContextHelper;

/**
 * 通过SLF4J MDC来记录用户和请求的信息。
 * <p>
 * 建议在log4j的配置文件中，设置如下pattern layout：
 * </p>
 * <p/>
 * <pre>
 * &lt;layout class="org.apache.log4j.PatternLayout"&gt;
 *     &lt;param name="ConversionPattern" value="%-4r [%d{yyyy-MM-dd HH:mm:ss}] - %X{remoteAddr} %X{requestURI} %X{referrer} %X{userAgent} %X{cookie.名称} - %m%n" /&gt;
 * &lt;/layout&gt;
 * </pre>
 * <p>
 * 下面是logback版本：
 * </p>
 * <p/>
 * <pre>
 * &lt;layout class="ch.qos.logback.classic.PatternLayout"&gt;
 *     &lt;pattern&gt;%-4r [%d{yyyy-MM-dd HH:mm:ss}] - %X{remoteAddr} %X{requestURI} %X{referrer} %X{userAgent} %X{cookie.名称} - %m%n&lt;/pattern&gt;
 * &lt;/layout&gt;
 * </pre>
 *
 * @author Michael Zhou
 * @see com.alibaba.citrus.webx.util.SetLoggingContextHelper
 */
public class SetLoggingContextFilter extends FilterBean {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SetLoggingContextHelper helper = new SetLoggingContextHelper(request);

        try {
            helper.setLoggingContext();

            chain.doFilter(request, response);
        } finally {
            helper.clearLoggingContext();
        }
    }
}
