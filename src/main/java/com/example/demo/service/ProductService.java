package com.example.demo.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.strategy.DiscountContext;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountContext discountContext;

    public ProductService(ProductRepository productRepository, DiscountContext discountContext) {
        this.productRepository = productRepository;
        this.discountContext = discountContext;
    }

    public Iterable<Product> getAllProducts() {
        Iterable<Product> products = productRepository.findAll();
        for (Product product : products) {
            applyPricing(product);
        }
        return products;
    }

    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            applyPricing(product);
        }
        return product;
    }

    // POST /products/save — add.html ส่ง Product + ProductDetail (1:1) + reviews[0] (1:N) มาพร้อมกัน
    public Product createProduct(Product product) {
        if (product.getDetail() != null) {
            product.getDetail().setProduct(product);
        }
        if (product.getReviews() != null) {
            for (Review review : product.getReviews()) {
                review.setProduct(product);
                if (review.getReviewDate() == null) {
                    review.setReviewDate(LocalDate.now());
                }
            }
        }
        return productRepository.save(product);
    }

    // POST /products/update/{id} — edit.html ไม่ส่ง detail.id กลับมาและไม่แตะ reviews เลย
    // จึงโหลด entity เดิมจาก DB มาอัปเดตทีละฟิลด์ ป้องกัน Hibernate สร้าง ProductDetail แถวใหม่ซ้ำ
    public Product updateProduct(Long id, Product formProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบสินค้า id=" + id));

        existing.setName(formProduct.getName());
        existing.setCategory(formProduct.getCategory());
        existing.setBrand(formProduct.getBrand());
        existing.setStock(formProduct.getStock());
        existing.setPrice(formProduct.getPrice());
        existing.setDiscountType(formProduct.getDiscountType());

        ProductDetail incomingDetail = formProduct.getDetail();
        if (incomingDetail != null) {
            ProductDetail detail = existing.getDetail();
            if (detail == null) {
                detail = new ProductDetail();
                detail.setProduct(existing);
                existing.setDetail(detail);
            }
            detail.setDescription(incomingDetail.getDescription());
            detail.setWarranty(incomingDetail.getWarranty());
            detail.setWeight(incomingDetail.getWeight());
            detail.setDimensions(incomingDetail.getDimensions());
            detail.setManufacturedCountry(incomingDetail.getManufacturedCountry());
        }

        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private void applyPricing(Product product) {
        double discountedPrice = discountContext.applyDiscount(product.getPrice(), product.getDiscountType());
        product.setDiscountedPrice(discountedPrice);
    }
}