package com.DsCommerce.tests;

import com.DsCommerce.entities.Category;
import com.DsCommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct() {

        Category category = CategoryFactory.createCategory();

        Product product = new Product(1L, "PlayStation 5", "Console de última geração da Sony", 4999.99,
                "https://example.com/ps5.jpg");
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct(String name) {

        Product product = createProduct();
        product.setName(name);
        return product;
    }
}
