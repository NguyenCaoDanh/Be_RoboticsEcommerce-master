
package com.wisdom.roboticsecommerce.Service.Impl;

import com.wisdom.roboticsecommerce.Dto.ProductDto;
import com.wisdom.roboticsecommerce.Entities.Product;
import com.wisdom.roboticsecommerce.Entities.ProductImage;
import com.wisdom.roboticsecommerce.Mapper.Impl.PageMapperImpl;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.ProductImageRepository;
import com.wisdom.roboticsecommerce.Repositories.ProductRepository;
import com.wisdom.roboticsecommerce.Service.ProductService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import com.wisdom.roboticsecommerce.Utils.Utils;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private PageMapperImpl pageMapper;
    @Override
    public void create(MultipartFile imageProduct, ProductDto productDto) {
        try {
            Utils.checkSize(imageProduct);
            Utils.isValid(imageProduct);
            var userId = mapper.getUserIdByToken();
            Product product = new Product();
            product.setCode(productDto.getCode());
            product.setName(productDto.getName());
            product.setQuantity(productDto.getQuantity());
            product.setPrice(productDto.getPrice());
            product.setCategoryId(productDto.getCategoryId());
            product.setPromotionId(productDto.getPromotionId());
            product.setAccountId(userId);
            product.setCreatAt(new Date());
            product.setStatus(Constants.STATUS_ACTIVE);
            product.setDeleted(Constants.DONT_DELETE);
            productRepository.save(product);
            ProductImage productImage = new ProductImage();
            productImage.setImage(Utils.imageName(imageProduct));
            productImage.setProductId(product.getId());
            productImage.setAccountId(userId);
            productImage.setStatus(Constants.STATUS_ACTIVE);
            productImage.setDeleted(Constants.DONT_DELETE);
            productImage.setCreatAt(new Date());
            productImageRepository.save(productImage);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(MultipartFile imageProduct, ProductDto productDto, Long productId) {
        try {
            Optional<Product> productOptional = productRepository.findByIdAndDeleted(productId,Constants.DONT_DELETE);
            if (productOptional.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy sản phẩm");
            }
            Product product = productOptional.get();
            Utils.checkSize(imageProduct);
            Utils.isValid(imageProduct);
            var userId = mapper.getUserIdByToken();
            product.setCode(productDto.getCode());
            product.setName(productDto.getName());
            product.setQuantity(productDto.getQuantity());
            product.setPrice(productDto.getPrice());
            product.setCategoryId(productDto.getCategoryId());
            product.setPromotionId(productDto.getPromotionId());
            product.setAccountId(userId);
            product.setUpdateAt(new Date());
            productRepository.save(product);
            Optional<ProductImage> productImageOptional = productImageRepository.findByIdAndDeleted(product.getId(),Constants.DONT_DELETE);
            ProductImage productImage = productImageOptional.get();
            productImage.setImage(Utils.imageName(imageProduct));
            productImage.setAccountId(userId);
            productImage.setUpdateAt(new Date());
            productImageRepository.save(productImage);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleted(Long productId) {
        try {
            Optional<Product> productList = productRepository.findByIdAndDeleted(productId, Constants.DONT_DELETE);
            if (productList.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy sản phẩm");
            }
            Product product = productList.get();
            product.setUpdateAt(new Date());
            product.setStatus(Constants.STATUS_INACTIVE);
            product.setDeleted(Constants.DELETED);
            productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Page<Map<String, Object>> search(Long categoryId, String name, Integer status, Integer pageNo, Integer pageSize, String sortBy, String sortType) {
        try{
            var pageable = pageMapper.customPage(pageNo, pageSize, sortBy, sortType);
            return productRepository.search(categoryId,name,status,pageable);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Product findById(Long productId) {
        try {
            Optional<Product> productOptional = productRepository.findByIdAndDeleted(productId,Constants.DONT_DELETE);
            if (productOptional.isEmpty()){
                throw new MessageDescriptorFormatException("Không tìm thấy sản phẩm");
            }
            return productOptional.get();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public Product detailProduct(Long id){
        Product product = productRepository.getOneCustom(
                id,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE);
        if (product == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }

        return product;
    }
    @Override
    public Page<Map<String,Object>> findByKeyword(String keyword, Long categoryId, Pageable pageable) {
        Page<Map<String,Object>> productList = productRepository.findByKeyword(
                keyword,
                categoryId,
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE,
                pageable);
        return productList;
    }
}
