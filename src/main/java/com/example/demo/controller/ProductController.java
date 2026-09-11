package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        Product product = new Product();
        // add.html ผูก *{reviews[0].reviewer} ตรงๆ จึงต้องมี element แรกรออยู่ก่อน
        product.getReviews().add(new Review());
        model.addAttribute("product", product);
        return "products/add";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        productService.createProduct(product);
        redirectAttributes.addFlashAttribute("message", "เพิ่มสินค้าสำเร็จ");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "products/edit";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product,
            RedirectAttributes redirectAttributes) {
        productService.updateProduct(id, product);
        redirectAttributes.addFlashAttribute("message", "อัปเดตสินค้าสำเร็จ");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "products/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "ลบสินค้าสำเร็จ");
        return "redirect:/products";
    }
}