package com.mf.cartservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mf.cartservice.dto.Mapper;
import com.mf.cartservice.dto.request.AddProductRequestDto;
import com.mf.cartservice.dto.response.CartResponseDTO;
import com.mf.cartservice.dto.response.Response;
import com.mf.cartservice.entity.Cart;
import com.mf.cartservice.entity.CartItem;
import com.mf.cartservice.exception.ValidationException;
import com.mf.cartservice.feignClients.ProductFeignClient;
import com.mf.cartservice.feignClients.UserFeignClient;
import com.mf.cartservice.model.Product;
import com.mf.cartservice.model.User;
import com.mf.cartservice.repository.CartItemRepository;
import com.mf.cartservice.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final UserFeignClient userFeignClient;
	private final ProductFeignClient productFeignClient;
	private final Mapper mapper;
	private Logger logger = LoggerFactory.getLogger(CartService.class);

	public Response createCart(Long iduser) {
		Cart cart = new Cart();
		User user = userFeignClient.getUserById(iduser);
		if (Objects.nonNull(user)) {
			Cart existigCartByIdUser = cartRepository.findByIdUser(iduser);
			if(Objects.isNull(existigCartByIdUser)) {
				cart.setIduser(iduser);
				cartRepository.save(cart);
				return new Response("Cart created", cart);
			}
			throw new ValidationException("Already exists a cart for user: " + iduser );
		}
		logger.info("User with id: " + iduser + " does not exists");
		throw new ValidationException("User with id: " + iduser + " does not exists");
	}
	
	public Response addProductToCart(AddProductRequestDto addProductRequestDTO, String token) {
		Optional<Cart> existingCart = cartRepository.findById(addProductRequestDTO.getIdcart());
		if (existingCart.isPresent()) {
			Product existingProduct = productFeignClient.getProductById(addProductRequestDTO.getIdproduct(), token);
			if (Objects.nonNull(existingProduct)) {
				List<CartItem> existingCartItems = cartItemRepository.findByIdCartAndIdproduct(
						addProductRequestDTO.getIdcart(), addProductRequestDTO.getIdproduct());

				if (addProductRequestDTO.getProductquantity() < existingProduct.getStock()) {
					if (existingCartItems.isEmpty()) {
						CartItem newItem = mapper.mapperToCartItem(addProductRequestDTO, existingProduct, existingCart);

						cartItemRepository.save(newItem);
						CartResponseDTO cartResponseDTO = mapper.cartResponseDto(existingProduct, newItem);
						Response response = new Response("Product added successfully to the cart cart eempty", cartResponseDTO);
						return response;
					} else {
						Response response = new Response();
						for (CartItem cartItem : existingCartItems) {
							if (cartItem.getOrdernumber().equals("Pending")) {
								Long newQuantity = cartItem.getProductquantity()
										+ addProductRequestDTO.getProductquantity();
								BigDecimal totalPrice = existingProduct.getPriceperunit()
										.multiply(new BigDecimal(newQuantity));
								cartItem.setProductquantity(newQuantity);
								cartItem.setTotalprice(totalPrice);
								cartItemRepository.save(cartItem);
								CartResponseDTO cartResponseDTO = mapper.cartResponseDto(existingProduct, cartItem);
								response = new Response("Product added successfully to the cart pending", cartResponseDTO);
								return response;
							} else {
								CartItem newItem = mapper.mapperToCartItem(addProductRequestDTO, existingProduct,
										existingCart);
								cartItemRepository.save(newItem);
								CartResponseDTO cartResponseDTO = mapper.cartResponseDto(existingProduct, newItem);
								response = new Response("Product added successfully to the cart validar", cartResponseDTO);
								return response;
							}
						}
					}
				}
				throw new ValidationException("Insufficient stock");
			}
			throw new ValidationException("Does not exists product with id: " + addProductRequestDTO.getIdproduct());
		}
		throw new ValidationException("Does not exists cart with id: " + addProductRequestDTO.getIdcart());
	}

	private Response updateQuantity(Long idcart, Long idproduct, String action, String token) {
		Response response = new Response();
		List<CartItem> existingCartItem = cartItemRepository.findByIdCartAndIdproduct(idcart, idproduct);
		if(!existingCartItem.isEmpty()) {
			for (CartItem cartItem : existingCartItem) {
				if (cartItem.getOrdernumber().equals("Pending")) {
					Long newQuantity = 0L;
					if (action.equals("increase")) {
						newQuantity = cartItem.getProductquantity() + 1;
					} else if (action.equals("decrease")) {
						newQuantity = cartItem.getProductquantity() - 1;
					}
					Product existingProduct = productFeignClient.getProductById(idproduct, token);
					BigDecimal totalPrice = existingProduct.getPriceperunit().multiply(new BigDecimal(newQuantity));
					cartItem.setProductquantity(newQuantity);
					cartItem.setTotalprice(totalPrice);
					cartItemRepository.save(cartItem);
					response = new Response("Quantity updated", cartItem);
					return response;
				}
				throw new ValidationException("There is not products to update");
			}			
		}
		throw new ValidationException("There is not products with that values, validate");
		
	}

	public Response increaseProductQuantityFromCart(Long idcart, Long idproduct, String token) {
		Response response = updateQuantity(idcart, idproduct, "increase", token);
		return response;
	}

	public Response decreaseProductQuantityFromCart(Long idcart, Long idproduct, String token) {
		Response response = updateQuantity(idcart, idproduct, "decrease", token);
		return response;
	}

	public Response getallCartItems(Long idcart, String token) {
		List<CartResponseDTO> items = cartItemRepository.findAllbyIdcart(idcart).stream().map(item -> {
			Product product = productFeignClient.getProductById(item.getIdproduct(), token);
			return mapper.cartResponseDto(product, item);
		}).collect(Collectors.toList());

		if (!items.isEmpty()) {
			Response response = new Response("Retrieved successfully", items);
			return response;
		}
		throw new ValidationException("There aren't items for that idCart or does not exist that cart");
	}

	public List<CartItem> getallItems(Long idcart, String ordernumber) {
		List<CartItem> items = cartItemRepository.findAllItemsToOrder(idcart, ordernumber);
		if (!items.isEmpty()) {
			return items;
		}
		throw new ValidationException("There aren't items for that idCart or does not exist that cart");
	}

	public Response removeItemFromCart(Long idcart, Long idproduct) {
		Response response = new Response();
		if (validateIfExistsCart(idcart)) {
			List<CartItem> existingCartItem = cartItemRepository.findByIdCartAndIdproduct(idcart, idproduct);
			if (!existingCartItem.isEmpty()) {
				for (CartItem cartItem : existingCartItem) {
					if (cartItem.getOrdernumber().equals("Pending")) {
						cartItemRepository.deleteById(cartItem.getIdcartitem());
						response = new Response("Removed item succesfully", null);
					}
				}
				return response;
			}
			throw new ValidationException("item does not exists in the cart!");
		}
		throw new ValidationException("Cart does not exists!");
	}

	public Response cleanCart(Long idcart) {
		if (validateIfExistsCart(idcart)) {
			cartItemRepository.findByIdcart(idcart).stream().forEach(item -> {
				cartItemRepository.deleteById(item.getIdcartitem());
			});
			return new Response("Clean cart succesfully", null);
		}
		throw new ValidationException("Cart does not exists!");
	}

	public boolean validateIfExistsCart(Long idCart) {
		Optional<Cart> cart = cartRepository.findById(idCart);
		if (cart.isPresent()) {
			return true;
		}
		return false;
	}

	public String updateOrderNumber(Long idCart, String ordernumber) {
		List<CartItem> items = getallItems(idCart, "Pending");
		for (CartItem item : items) {
			item.setOrdernumber(ordernumber);
			cartItemRepository.save(item);
		}
		return "Add ordernumber successfully!";
	}

}
