/**
 *
 */
package de.akquinet.gomobile.ipojo.training.service;


/**
 * Service exposed by vendors.
 * Providers should also publish the following properties
 * <ul>
 * <li>product.type : product type</li>
 * <li>vendor.name : vendor name</li>
 * </ul>
 * @author Clement Escoffier
 */
public interface VendorService {

    /**
     * Service Property specifying the type of product.
     */
    public static final String PRODUCT_TYPE = "product.type";

    /**
     * Service Property specifying the vendor name.
     */
    public static final String VENDOR_NAME = "vendor.name";

    /**
     * Buys a product.
     * The product type / name must be the value returned by
     * {@link VendorService#getType()}
     * The product origin must be the value returned by
     * {@link VendorService#getName()}
     * @return the bought product.
     */
    public Product buy();

    /**
     * Gets vendor's name.
     * @return the name
     */
    public String getName();

    /**
     * Gets product type.
     * @return the type of product sold by the vendor
     */
    public String getType();

}
