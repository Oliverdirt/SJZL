package com.ctsi.ssdc.console;

import com.bstek.ureport.console.RequestHolder;
import com.bstek.ureport.console.ServletAction;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hx
 * @date 2022/11/15
 */
public class UreportServlet extends HttpServlet {
    private static final long serialVersionUID = 533049461276487971L;
    public static final String URL = "/ureport";
    private Map<String, ServletAction> actionMap = new HashMap<String, ServletAction>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        WebApplicationContext applicationContext = getWebApplicationContext(config);
        Collection<ServletAction> handlers = applicationContext.getBeansOfType(ServletAction.class).values();
        for (ServletAction handler : handlers) {
            String url = handler.url();
            if (actionMap.containsKey(url)) {
                throw new RuntimeException("Handler [" + url + "] already exist.");
            }
            actionMap.put(url, handler);
        }
    }

    protected WebApplicationContext getWebApplicationContext(ServletConfig config){
        return WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getContextPath() + URL;
        String uri = req.getRequestURI();
        String targetUrl = uri.substring(path.length());
        if (targetUrl.length() < 1) {
            outContent(resp, "Welcome to use ureport,please specify target url.");
            return;
        }
        int slashPos = targetUrl.indexOf("/", 1);
        if (slashPos > -1) {
            targetUrl = targetUrl.substring(0, slashPos);
        }
        ServletAction targetHandler = actionMap.get(targetUrl);
        if (targetHandler == null) {
            outContent(resp, "Handler [" + targetUrl + "] not exist.");
            return;
        }
        RequestHolder.setRequest(req);
        try{
            targetHandler.execute(req, resp);
        }catch(Exception ex){
            resp.setCharacterEncoding("UTF-8");
            ServletOutputStream os = resp.getOutputStream();
            Throwable e = buildRootException(ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorMsg = e.getMessage();
            if(StringUtils.isBlank(errorMsg)){
                errorMsg = e.getClass().getName();
            }
            os.write(errorMsg.getBytes(StandardCharsets.UTF_8));
            os.close();
            os.flush();
            throw new ServletException(ex);
        }finally{
            RequestHolder.clean();
        }
    }
    private Throwable buildRootException(Throwable throwable){
        if(throwable.getCause()==null){
            return throwable;
        }
        return buildRootException(throwable.getCause());
    }

    private void outContent(HttpServletResponse resp, String msg) throws IOException {
        resp.setContentType("text/html");
        ServletOutputStream os = resp.getOutputStream();
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<header><title>UReport Console</title></header>");
        sb.append("<body>");
        sb.append(msg);
        sb.append("</body>");
        sb.append("</html>");
        os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();

    }
}
