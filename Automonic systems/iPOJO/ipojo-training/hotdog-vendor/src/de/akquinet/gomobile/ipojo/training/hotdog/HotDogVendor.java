package de.akquinet.gomobile.ipojo.training.hotdog;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.gomobile.ipojo.training.service.Product;
import de.akquinet.gomobile.ipojo.training.service.ResellerService;
import de.akquinet.gomobile.ipojo.training.service.VendorService;

@Component(immediate=true)
//TODO Add annotations to declare the component type
public class HotDogVendor implements VendorService {

    private Logger m_logger = LoggerFactory.getLogger(HotDogVendor.class);

    @ServiceProperty(name=VendorService.PRODUCT_TYPE)
    private String m_type = "hotdog";

    @ServiceProperty(name=VendorService.VENDOR_NAME)
    private String m_name = "Hot Dog !";

    //TODO Set the requirement to get bun
    // Filter used the LDAP syntax : filter="(product.type=bun)"
    private ResellerService m_bun;

    //TODO Set the requirement to get wiener
    private ResellerService m_wiener;

    public Product buy() {
        m_logger.info("Getting bun");
        // TODO Buy a bun and a wiener


        m_logger.info("Returning a hotdog");
        return new Product(m_type, m_name);
    }

    public String getType() {
        return m_type;
    }

    public String getName() {
        return m_name;
    }

}
