package com.restauran.delivery.controllers;

import java.io.IOException;

import com.restauran.delivery.classes.FileUploadUtil;
import com.restauran.delivery.entity.CompletedOrderItem;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.repositories.CompletedOrdersRepository;
import com.restauran.delivery.repositories.ProductsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {

    @Autowired
	private ProductsRepository productRepository;

    @Autowired
    private CompletedOrdersRepository cOrdersRepository;
    
    private Iterable<CompletedOrderItem> getCompletedOrders() {

        Iterable<CompletedOrderItem> all = cOrdersRepository.findAll();

        return all;
    }

    @GetMapping("/admin/statistic")
    public String getStatisticPage(Model model) {

        model.addAttribute("title", "Страница администратора");
        Iterable<CompletedOrderItem> all = getCompletedOrders();
        model.addAttribute("products", all);

        return "/admin/admin";
    }

    
    @GetMapping("/admin/catalog")
    public String getCatalogPage(Model model) {

        model.addAttribute("product", new ProductUnit());

        Iterable<ProductUnit> products = productRepository.findAll();
        model.addAttribute("allProducts", products);

        return "/admin/alteringCatalog";
    }

    
    @PostMapping("/admin/add")
    public String addProduct(@ModelAttribute ProductUnit product, Model model) {

        model.addAttribute("newProduct", product);
        model.addAttribute("title", "Добавление продукта");
        
        return "/admin/newProduct";
    }

    
    @PostMapping("/admin/catalog")
	public String handleSavedProduct(ProductUnit product, 
                        @RequestParam("file") MultipartFile multipartFile) 
                        throws IOException {
        
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setPicture(fileName);
         
        ProductUnit savedProduct = productRepository.save(product);
 
        String uploadDir = "src/main/resources/static/img/" + savedProduct.getId();
 
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		return "redirect:/admin/catalog";
	}

    @GetMapping("/admin/product/{id}/edit")
    public String getEditPage(@PathVariable(value = "id") int id, Model model) {

        if (productRepository.existsById(id) == false) {
            return "redirect:/admin/catalog";
        }
        
        ProductUnit product = productRepository.findById(id).orElseThrow();

        model.addAttribute("product", product);

        return "admin/productEdit";
    }

    @PostMapping("/admin/product/{id}/edit")
	public String saveEditProduct(@PathVariable(value = "id") int id, 
                            ProductUnit newProduct) throws IOException {

        if (productRepository.existsById(id) == false) {
            return "redirect:/admin/catalog";
        }
        ProductUnit product = productRepository.findById(id).orElseThrow();

        product.setAmount(newProduct.getAmount());
        product.setComposition(newProduct.getComposition());
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
         
        productRepository.save(product);
        
		return "redirect:/admin/catalog";
	}

    @PostMapping("/admin/product/{id}/{id2}")
	public String saveEditProduct(@PathVariable(value = "id") int id, 
                            @RequestParam("file") MultipartFile multipartFile) 
                            throws IOException {

        if (productRepository.existsById(id) == false) {
            return "redirect:/admin/catalog";
        }
        ProductUnit product = productRepository.findById(id).orElseThrow();
        
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setPicture(fileName);
         
        productRepository.save(product);
 
        String uploadDir = "src/main/resources/static/img/" + product.getId();
 
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        
		return "redirect:/admin/product/{id}/edit";
	}

    @GetMapping("/admin/product/{id}/delete")
	public String deleteProduct(@PathVariable(value = "id") int id) throws IOException {
        
        if (productRepository.existsById(id) == false) {
            return "redirect:/admin/catalog";
        }
        productRepository.deleteById(id);
        
		return "redirect:/admin/catalog";
	}
}