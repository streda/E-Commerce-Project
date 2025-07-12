
package com.e_commerce.project.service;

import com.e_commerce.project.model.persistence.Cart;
import com.e_commerce.project.model.persistence.Item;
import com.e_commerce.project.model.persistence.User;
import com.e_commerce.project.model.persistence.repositories.CartRepository;
import com.e_commerce.project.model.persistence.repositories.ItemRepository;
import com.e_commerce.project.model.persistence.repositories.UserRepository;
import com.e_commerce.project.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Item testItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setCart(new Cart());
        testUser.getCart().setUser(testUser);

        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("Widget");
        testItem.setPrice(BigDecimal.valueOf(2.99));

        when(userRepository.findByUsername("testuser")).thenReturn(testUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testAddToCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testuser");
        request.setItemId(1L);
        request.setQuantity(2);

        Cart updatedCart = cartService.addToCart(request);

        assertNotNull(updatedCart);
        assertEquals(2, updatedCart.getItems().size());
        assertEquals(BigDecimal.valueOf(5.98), updatedCart.getTotal());
    }

    @Test
    public void testRemoveFromCart() {
        testUser.getCart().addItem(testItem);
        testUser.getCart().addItem(testItem);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testuser");
        request.setItemId(1L);
        request.setQuantity(1);

        Cart updatedCart = cartService.removeFromCart(request);

        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getItems().size());
        assertEquals(BigDecimal.valueOf(2.99), updatedCart.getTotal());
    }

    @Test
    public void testAddToCart_UserNotFound() {
        when(userRepository.findByUsername("nouser")).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("nouser");
        request.setItemId(1L);
        request.setQuantity(1);

        assertThrows(RuntimeException.class, () -> cartService.addToCart(request));
    }

    @Test
    public void testAddToCart_ItemNotFound() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testuser");
        request.setItemId(999L);
        request.setQuantity(1);

        assertThrows(RuntimeException.class, () -> cartService.addToCart(request));
    }
}
