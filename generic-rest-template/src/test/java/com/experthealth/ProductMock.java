package com.experthealth;

/**
 * Simple Mock object for TESTs
 * Created on 14/05/2015.
 * <p/>
 *
 * @author moses.mansaray
 */
public class ProductMock {
  public int sku;
  public String name;

  public ProductMock(int sku, String name) {
    this.sku = sku;
    this.name = name;
  }

  @Override
  public String toString() {
    return "Product : ID=" + sku + ", Name=" + name ;
  }
}
