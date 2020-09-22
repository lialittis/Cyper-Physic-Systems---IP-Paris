package de.akquinet.gomobile.ipojo.training.shop;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.gomobile.ipojo.training.service.Product;
import de.akquinet.gomobile.ipojo.training.service.VendorService;

@Component(immediate=true)
@Instantiate
public class ShopServlet extends HttpServlet {

    /**
     * UUID.
     */
    private static final long serialVersionUID = 1L;

    private static final String ALIAS = "/shop";
    private static final String ALIAS_RES = "/shop/res";


    private Logger m_logger = LoggerFactory.getLogger(ShopServlet.class);

    @Requires
    private HttpService m_http;

    @Requires(optional=true)
    private VendorService[] m_vendors;

    private String m_port;

    private BundleContext m_context;

    public ShopServlet(BundleContext context) {
        m_context = context;
    }

    @Validate
    public void start() throws ServletException, NamespaceException {
        m_logger.info("Publishing the Shop Servlet");
        m_http.registerServlet(ALIAS, this, null, null);
        m_http.registerResources(ALIAS_RES, "web", null);

        m_port = m_context.getProperty("org.osgi.service.http.port");
    }

    @Invalidate
    public void stop() throws ServletException, NamespaceException {
        if (m_http != null) {
            m_http.unregister(ALIAS);
            m_http.unregister(ALIAS_RES);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        m_logger.info("DoGet : " + req.getRequestURL());
        String buy = req.getParameter("buy");
        Writer writer = resp.getWriter();
        StringBuffer buffer = new StringBuffer();
        writeHeader(buffer);
        writeFrame(buffer, buy);
        writeTable(buffer);
        writeFooter(buffer);
        writer.append(buffer);
        writer.flush();
    }

    private void writeFrame(StringBuffer buffer, String buy) {
        if (buy == null) {
            buffer.append("<div class=\"welcome\">" + "Welcome to the snack bar </div>");
        } else {
            VendorService vendor = getVendor(buy);
            if (vendor == null) {
                buffer.append("<div class=\"error\">" + " Oh ! Cannot buy stuff from <b>" + buy + "<b>  </div>");
            } else {
                Product product = vendor.buy();
                buffer.append("<div class=\"ok\">" + " Just bought an amazing <b>" + product.getName() + "</b> from <b>" + product.getVendor() + "</b> </div>");
            }
        }
    }

    private VendorService getVendor(String name) {
        for (VendorService vendor : m_vendors) {
            if (name.equals(vendor.getName())) {
                return vendor;
            }
        }
        return null;
    }

    private void writeHeader(StringBuffer buffer) {
        buffer.append("<html>");
        buffer.append("<head>");
        buffer.append("<title>Snack Bar</title>");

        buffer.append("<style type=\"text/css\">");
        buffer.append("@import url(\"http://localhost:"+ m_port + ALIAS_RES + "/style.css\");");
        buffer.append("</style>");
        buffer.append("</head>");
        buffer.append("<body>");
    }

    private void writeTable(StringBuffer buffer) {
        if (m_vendors.length == 0) {
            buffer.append("<h3>No vendors available</h3>");
        } else {
            buffer.append("<table id=\"vendors\" >");
            buffer.append("<thead>");
            buffer.append("<tr>");
            buffer.append("<th>Vendor Name</th>");
            buffer.append("<th>Product Type</th>");
            buffer.append("<th>Buy</th>");
            buffer.append("</tr>");
            buffer.append("</thead>");
            buffer.append("</tbody>");
            for (VendorService vendor : m_vendors) {
               buffer.append("<tr>");
               buffer.append("<td>" + vendor.getName() + "</td>");
               buffer.append("<td>" + vendor.getType() + "</td>");
               buffer.append("<td><a href=\"http://localhost:" + m_port + ALIAS + "?buy=" + vendor.getName() + "\">buy</a>" + "</td>");
                buffer.append("</tr>");
            }
            buffer.append("</tbody>");
            buffer.append("</table>");
        }
    }

    private void writeFooter(StringBuffer buffer) {
        buffer.append("</body>");
        buffer.append("</html>");
    }

}
