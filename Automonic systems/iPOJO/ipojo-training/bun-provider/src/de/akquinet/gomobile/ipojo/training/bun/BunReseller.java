package de.akquinet.gomobile.ipojo.training.bun;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceController;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.gomobile.ipojo.training.service.Product;
import de.akquinet.gomobile.ipojo.training.service.ResellerService;

@Component(immediate=true)
@Provides
public class BunReseller implements ResellerService {

    private Logger m_logger = LoggerFactory.getLogger(BunReseller.class);

    @ServiceProperty(name=ResellerService.PRODUCT_TYPE)
    private String m_type = "bun";

    private int m_stock;

    @ServiceController
    protected boolean m_controller;

    public synchronized Product buy() {
        m_logger.info("Getting a " + m_type + " (" + m_stock + ")");
        // TODO Stock management
        return new Product(m_type);
    }

    public String getType() {
        return m_type;
    }

    @Property(name="stock", mandatory=true)
    void setStock(int s) {
        // TODO set the stock
    }

}
