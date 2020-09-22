package de.akquinet.gomobile.ipojo.training.service;


/**
 * Vendors sell Products
 * @author Clement Escoffier
 */
public class Product {

    /**
     * Product Name.
     */
    private final String m_name;

    /**
     * Vendor Name.
     */
    private final String m_vendor;

    /**
     * Creates a Product for vendor.
     * @param n the name
     * @param f the vendor name
     */
    public Product(String n, String f) {
        m_name = n;
        m_vendor = f;
    }

    /**
     * Creates a Product for reseller.
     * @param n the name
     */
    public Product(String n) {
        m_name = n;
        m_vendor = null;
    }

    /**
     * Gets the product name.
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Gets the vendor.
     * @return the vendor
     */
    public String getVendor() {
        return m_vendor;
    }

}
