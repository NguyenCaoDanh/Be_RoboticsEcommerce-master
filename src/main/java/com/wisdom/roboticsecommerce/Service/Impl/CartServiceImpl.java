package com.wisdom.roboticsecommerce.Service.Impl;


import com.wisdom.roboticsecommerce.Dto.CartRequest;
import com.wisdom.roboticsecommerce.Dto.ListCartRequest;
import com.wisdom.roboticsecommerce.Entities.Cart;
import com.wisdom.roboticsecommerce.Entities.Product;
import com.wisdom.roboticsecommerce.Mapper.Mapper;
import com.wisdom.roboticsecommerce.Repositories.CartRepository;
import com.wisdom.roboticsecommerce.Repositories.ProductRepository;
import com.wisdom.roboticsecommerce.Service.CartService;
import com.wisdom.roboticsecommerce.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Cart> getAllCart(Pageable pageable) {
        Page<Cart> cartList = cartRepository.getAllCustom(
                mapper.getUserIdByToken(),
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE,
                pageable
        );

        return cartList;
    }

    @Override
    public Cart addCart(CartRequest cartRequest) {
        Long userId = mapper.getUserIdByToken();

        // Kiểm tra sản phẩm có tồn tại không và có đủ số lượng không
        Product product = productRepository.getOneCustom(
                cartRequest.getProductId(),
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE
        );
        if (product == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        if (product.getQuantity() < cartRequest.getQuantity()) {
            throw new RuntimeException("Không đủ số lượng sản phẩm");
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<Cart> existingCartOptional = cartRepository.findByAccountIdAndProductIdAndStatusAndDeleted(
                userId,
                cartRequest.getProductId(),
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE);

        Cart cart;
        if (existingCartOptional.isPresent()) {
            cart = existingCartOptional.get();
            // Kiểm tra lại số lượng khi tăng thêm
            if (product.getQuantity() < cart.getQuantity() + cartRequest.getQuantity()) {
                throw new RuntimeException("Không đủ số lượng sản phẩm");
            }
            cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
            cart.setUpdateAt(new Date());
        } else {
            cart = new Cart();
            cart.setProductId(cartRequest.getProductId());
            cart.setQuantity(cartRequest.getQuantity());
            cart.setCreatAt(new Date());
            cart.setUpdateAt(new Date());
            cart.setStatus(Constants.STATUS_ACTIVE);
            cart.setDeleted(Constants.DONT_DELETE);
            cart.setAccountId(userId);
        }

        return cartRepository.save(cart);

    }

    @Override
    public Cart updateCart(CartRequest cartRequest) {
        Cart cart = cartRepository.getOneCustom(
                cartRequest.getId(),
                mapper.getUserIdByToken(),
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE);
        if (cart == null){
            throw new RuntimeException("Không tìm thấy giỏ hàng");
        }

        if (cartRequest.getQuantity() <= 0) {
            throw new RuntimeException("Số lượng sản phẩm phải lớn hơn 0");
        }

        // Kiểm tra sản phẩm có tồn tại không và có đủ số lượng không
        Product product = productRepository.getOneCustom(
                cart.getProductId(),
                Constants.STATUS_ACTIVE,
                Constants.DONT_DELETE
        );
        if (product == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        if (product.getQuantity() < cartRequest.getQuantity()) {
            throw new RuntimeException("Không đủ số lượng sản phẩm");
        }

        cart.setQuantity(cartRequest.getQuantity());
        cart.setUpdateAt(new Date());

        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(ListCartRequest listCartRequest) {
        // duyệt qua list cartId
        // nếu như tìm thấy cartId dưới db thì update delete
        // nếu k tìm thấy thì báo lỗi
        for (Long item : listCartRequest.getListCartId()){
            Optional<Cart> cart = cartRepository.findByIdAndAccountId(
                    item,
                    mapper.getUserIdByToken()
            );
            if (cart.isEmpty()){
                continue;
            }
            cartRepository.deleteById(item);
        }
    }
}
