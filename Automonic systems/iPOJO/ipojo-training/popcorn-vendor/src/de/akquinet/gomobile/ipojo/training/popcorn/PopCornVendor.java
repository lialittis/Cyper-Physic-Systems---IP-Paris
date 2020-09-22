package de.akquinet.gomobile.ipojo.training.popcorn;


import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.gomobile.ipojo.training.corn.service.CornVendor;
import de.akquinet.gomobile.ipojo.training.service.Product;
import de.akquinet.gomobile.ipojo.training.service.VendorService;

// TODO Add the annotation to instantiate the component type and provide the VendorService.
@Component(immediate=true)
public class PopCornVendor implements VendorService {

    private Logger m_logger = LoggerFactory.getLogger(PopCornVendor.class);

    @ServiceProperty(name=VendorService.PRODUCT_TYPE)
    private String m_type = "pop-corn";

    @ServiceProperty(name=VendorService.VENDOR_NAME)
    private String m_name = "extra pop-corn";

    //TODO  Add the annotation to inject a CornVendor in this field.
    // It is a mandatory, scalar service dependency
    private CornVendor m_corn;

    public Product buy() {
        m_logger.info("Getting corn");

        // TODO Call the corn vendor to get corn.


        m_logger.info("Returning pop corn");
        return new Product(m_type, m_name);
    }

    public String getType() {
        return m_type;
    }

    public String getName() {
        return m_name;
    }

}
