package de.akquinet.gomobile.ipojo.training.service;


/**
 * Service exposed by reseller.
 * Providers should also publish the following property
 * <ul>
 * <li>product.type : product type</li>
 * </ul>
 * @author Clement Escoffier
 */
public interface ResellerService {

    /**
     * Service Property specifying the type of product.
     */
    public static final String PRODUCT_TYPE = "product.type";

    /**
     * Buys a product.
     * The product type / name must be the value returned by
     * {@link ResellerService#getType()}
     * @return the bought product.
     */
    public Product buy();

    /**
     * Gets product type.
     * @return the type of product sold by the vendor
     */
    public String getType();

}
