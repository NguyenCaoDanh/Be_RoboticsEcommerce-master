package com.wisdom.roboticsecommerce.Service;

import com.wisdom.roboticsecommerce.Dto.CartRequest;
import com.wisdom.roboticsecommerce.Dto.ListCartRequest;
import com.wisdom.roboticsecommerce.Entities.Cart;
import com.wisdom.roboticsecommerce.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
 Page<Cart> getAllCart(Pageable pageable);
 Cart addCart(CartRequest cartRequest);
 Cart updateCart(CartRequest cartRequest);

 void deleteCart(ListCartRequest listCartRequest);
}
