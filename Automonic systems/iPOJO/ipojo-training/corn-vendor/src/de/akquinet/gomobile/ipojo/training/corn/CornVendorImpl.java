package de.akquinet.gomobile.ipojo.training.corn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akquinet.gomobile.ipojo.training.corn.service.CornVendor;


/**
 * Simple, Unlimited Reseller implementation
 */
//TODO Add the annotation to declare a component type, instantiate it and provides the VendorService
public class CornVendorImpl implements CornVendor {

    private Logger m_logger = LoggerFactory.getLogger(CornVendorImpl.class);

    public void getCorn() {
        m_logger.info("Get corn");
    }


}
