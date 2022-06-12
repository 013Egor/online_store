package com.restauran.delivery.controllers.admin;

import java.io.IOException;

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

import com.restauran.delivery.classes.FileUploadUtil;
import com.restauran.delivery.entity.ProductUnit;
import com.restauran.delivery.service.ProductService;

@Controller
public class ProductEditor {

    @Autowired
	ProductService productService;

    @Autowired
    FileUploadUtil fileUploadUtil;

    private void saveImage(ProductUnit product, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setPicture(fileName);
         
        ProductUnit savedProduct = productService.save(product);
        
        String dir = "" + savedProduct.getId();
        fileUploadUtil.saveFile(dir, fileName, multipartFile);
    }

    @PostMapping("/admin/add")
    public String addProductInfo(@ModelAttribute ProductUnit product, Model model) {

        model.addAttribute("newProduct", product);
        model.addAttribute("title", "Добавление продукта");
        
        return "/admin/newProduct";
    }

    
    @PostMapping("/admin/catalog")
	public String saveProduct(ProductUnit product, 
                        @RequestParam("file") MultipartFile multipartFile) 
                        throws IOException {
        
        saveImage(product, multipartFile);

		return "redirect:/admin/catalog";
	}

    @GetMapping("/admin/product/{id}/edit")
    public String getEditPage(@PathVariable(value = "id") int id, Model model) {

        if (productService.existsById(id) == false) {
            return "redirect:/admin/catalog";
        }
        
        ProductUnit product = productService.getProductById(id);

        model.addAttribute("product", product);

        return "admin/productEdit";
    }

    @PostMapping("/admin/product/{id}/edit")
	public String saveEditProduct(@PathVariable(value = "id") int id, 
                            ProductUnit newProduct) throws IOException {

        if (productService.existsById(id) == false) {
            return "redirect:/admin/catalog";
        }
        ProductUnit product = productService.getProductById(id);

        product.changeProduct(newProduct);
         
        productService.save(product);
        
		return "redirect:/admin/catalog";
	}

    @PostMapping("/admin/product/{id}/{id2}")
	public String saveEditedImage (@PathVariable(value = "id") int id, 
                            @RequestParam("file") MultipartFile multipartFile) 
                            throws IOException {

        if (productService.existsById(id) == false) {
            return "redirect:/admin/catalog";
        }
        ProductUnit product = productService.getProductById(id);
        
        saveImage(product, multipartFile);
        
		return "redirect:/admin/product/{id}/edit";
	}

   

    @GetMapping("/admin/product/{id}/delete")
	public String deleteProduct(@PathVariable(value = "id") int id) throws IOException {
        
        if (productService.existsById(id) == false) {
            return "redirect:/admin/catalog";
        }
        try {
            fileUploadUtil.deleteFiles(id);
        } catch (IOException e) {}
        
        productService.deleteById(id);
        
		return "redirect:/admin/catalog";
	}
}
