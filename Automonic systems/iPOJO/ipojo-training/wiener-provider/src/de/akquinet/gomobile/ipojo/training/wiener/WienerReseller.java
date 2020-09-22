package de.akquinet.gomobile.ipojo.training.wiener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.gomobile.ipojo.training.service.Product;
import de.akquinet.gomobile.ipojo.training.service.ResellerService;

//TODO Add the annotations here
public class WienerReseller implements ResellerService {

    private Logger m_logger = LoggerFactory.getLogger(WienerReseller.class);

    // TODO Declare a service property
    private String m_type = "wiener";

    public Product buy() {
        m_logger.info("Getting a " + m_type);
        return new Product(m_type);
    }

    public String getType() {
        return m_type;
    }

}
