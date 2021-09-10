package com.store.bill.service.service;

import com.store.bill.service.dto.request.OrderProductSummary;
import com.store.bill.service.dto.request.OrderSummary;
import com.store.bill.service.dto.response.BillSummary;
import com.store.bill.service.dto.response.ProductSummary;
import com.store.bill.service.dto.response.User;
import com.store.bill.service.entity.ProductEntity;
import com.store.bill.service.entity.UserEntity;
import com.store.bill.service.exception.ResourceNotFoundException;
import com.store.bill.service.mapper.ProductMapper;
import com.store.bill.service.mapper.UserMapper;
import com.store.bill.service.repository.ProductRepository;
import com.store.bill.service.repository.UserRepository;
import com.store.bill.service.utils.RuleUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    @Autowired
    public BillServiceImpl(UserRepository userRepository,
                           ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.productMapper = Mappers.getMapper(ProductMapper.class);
    }

    @Override
    public BillSummary getBillSummary(OrderSummary orderSummary) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(orderSummary.getUserId());
        if(userEntityOptional.isPresent()) {
            User user = userMapper.userEntityToUser(userEntityOptional.get());
            log.info("Get bill summary called for User" + user);
            Set<Long> productIds = orderSummary.getOrderProductSummaries().stream()
                    .map(OrderProductSummary::getProductId)
                    .collect(Collectors.toSet());
            Iterable<ProductEntity> productEntityIterable = productRepository.findAllById(productIds);
            Map<Long, ProductEntity> productEntities = new HashMap<>();
            productEntityIterable.forEach(productEntity -> productEntities.put(productEntity.getId(), productEntity));
            if(productIds.size()!=productEntities.size()) {
                throw new ResourceNotFoundException("Supplied productIds { "+ productIds.removeAll(productEntities.keySet())+ "} is not valid.");
            } else {
                List<ProductSummary> productSummaries = orderSummary.getOrderProductSummaries().stream().map(orderProductSummary -> {
                    ProductSummary productSummary
                            = productMapper.productEntityToProductSummary(productEntities.get(orderProductSummary.getProductId()));
                    productSummary.setQuantity(orderProductSummary.getQuantity());
                    productSummary.setGrossPrice(productSummary.getPrice()*orderProductSummary.getQuantity());
                    return productSummary;
                }).collect(Collectors.toList());

                return RuleUtils.applyRules(BillSummary.builder()
                        .user(user)
                        .productSummaries(productSummaries)
                        .build());
            }
        } else {
            throw new ResourceNotFoundException("Supplied userId is not valid.");
        }
    }
}
