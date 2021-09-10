package com.store.bill.service.service;

import com.store.bill.service.dto.request.OrderProductSummary;
import com.store.bill.service.dto.request.OrderSummary;
import com.store.bill.service.dto.response.BillSummary;
import com.store.bill.service.entity.CategoryEntity;
import com.store.bill.service.entity.ProductEntity;
import com.store.bill.service.entity.UserEntity;
import com.store.bill.service.entity.enums.UserType;
import com.store.bill.service.exception.ResourceNotFoundException;
import com.store.bill.service.repository.ProductRepository;
import com.store.bill.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {
    private BillService subject;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    private static final String DUMMY_STRING = "ABC";
    private static final Long DUMMY_ID = 1L;

    @BeforeEach
    public void setup() {
        subject = new BillServiceImpl(userRepository, productRepository);
    }

    @Test
    public void shouldGetBillSummaryForEmployee() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockedEmployeeUserEntity()));
        when(productRepository.findAllById(anySet())).thenReturn(mockedProducts());

        BillSummary billSummary = subject.getBillSummary(mockedOrderSummary());
        assertThat(billSummary.getUser().getName()).isEqualTo(DUMMY_STRING);
        assertThat(billSummary.getUser().getType()).isEqualTo(UserType.EMPLOYEE);

        assertThat(billSummary.getProductSummaries().size()).isEqualTo(2);

        // Grocery 10*100 + Other 5*50
        assertThat(billSummary.getGrossPrice()).isEqualTo(1250.0);
        // Other  5*50 * 30% + 11*5
        assertThat(billSummary.getDiscount()).isEqualTo(130.0);
        // gross price - discount
        assertThat(billSummary.getNetPayablePrice()).isEqualTo(1120.0);
    }

    @Test
    public void shouldGetBillSummaryForAffiliate() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockedAffiliateUserEntity()));
        when(productRepository.findAllById(anySet())).thenReturn(mockedProducts());

        BillSummary billSummary = subject.getBillSummary(mockedOrderSummary());
        assertThat(billSummary.getUser().getName()).isEqualTo(DUMMY_STRING);
        assertThat(billSummary.getUser().getType()).isEqualTo(UserType.AFFILIATE);

        assertThat(billSummary.getProductSummaries().size()).isEqualTo(2);

        // Grocery 10*100 + Other 5*50
        assertThat(billSummary.getGrossPrice()).isEqualTo(1250.0);
        // Other 5*50 * 10% + 12*5
        assertThat(billSummary.getDiscount()).isEqualTo(85.0);
        // gross price - discount
        assertThat(billSummary.getNetPayablePrice()).isEqualTo(1165.0);
    }

    @Test
    public void shouldGetBillSummaryForCustomerOver2Years() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockedCustomerOver2YearsUserEntity()));
        when(productRepository.findAllById(anySet())).thenReturn(mockedProducts());

        BillSummary billSummary = subject.getBillSummary(mockedOrderSummary());
        assertThat(billSummary.getUser().getName()).isEqualTo(DUMMY_STRING);
        assertThat(billSummary.getUser().getType()).isEqualTo(UserType.CUSTOMER);

        assertThat(billSummary.getProductSummaries().size()).isEqualTo(2);

        // Grocery 10*100 + Other 5*50
        assertThat(billSummary.getGrossPrice()).isEqualTo(1250.0);
        // Other 5*50 * 5% + 12*5
        assertThat(billSummary.getDiscount()).isEqualTo(72.5);
        // gross price - discount
        assertThat(billSummary.getNetPayablePrice()).isEqualTo(1177.5);
    }

    @Test
    public void shouldGetBillSummaryForCustomerLessThan2Years() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockedCustomerLessThan2YearsUserEntity()));
        when(productRepository.findAllById(anySet())).thenReturn(mockedProducts());

        BillSummary billSummary = subject.getBillSummary(mockedOrderSummary());
        assertThat(billSummary.getUser().getName()).isEqualTo(DUMMY_STRING);
        assertThat(billSummary.getUser().getType()).isEqualTo(UserType.CUSTOMER);

        assertThat(billSummary.getProductSummaries().size()).isEqualTo(2);

        // Grocery 10*100 + Other 5*50
        assertThat(billSummary.getGrossPrice()).isEqualTo(1250.0);
        // Other 5*50 * 0% + 12*5
        assertThat(billSummary.getDiscount()).isEqualTo(60.0);
        // gross price - discount
        assertThat(billSummary.getNetPayablePrice()).isEqualTo(1190.0);
    }

    @Test
    public void shouldFailGetBillSummaryForUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> subject.getBillSummary(mockedOrderSummary())).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void shouldFailGetBillSummaryForProductNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockedCustomerLessThan2YearsUserEntity()));
        when(productRepository.findAllById(anyCollection())).thenReturn(List.of());
        assertThatThrownBy(() -> subject.getBillSummary(mockedOrderSummary())).isInstanceOf(ResourceNotFoundException.class);
    }

    private OrderSummary mockedOrderSummary() {
        return OrderSummary.builder()
                .userId(DUMMY_ID)
                .orderProductSummaries(List.of(
                        OrderProductSummary.builder()
                                .productId(DUMMY_ID)
                                .quantity(10)
                                .build(),
                        OrderProductSummary.builder()
                                .productId(2L)
                                .quantity(5)
                                .build()
                ))
                .build();
    }

    private Iterable<ProductEntity> mockedProducts() {
        List<ProductEntity> productEntities = new ArrayList<>(2);

        ProductEntity productEntityGrocery = new ProductEntity();
        CategoryEntity categoryEntityGrocery = new CategoryEntity();
        categoryEntityGrocery.setId(DUMMY_ID);
        categoryEntityGrocery.setName("Grocery");
        productEntityGrocery.setId(DUMMY_ID);
        productEntityGrocery.setName(DUMMY_STRING);
        productEntityGrocery.setPrice(100D);
        productEntityGrocery.setCategory(categoryEntityGrocery);
        productEntities.add(productEntityGrocery);

        ProductEntity productEntityOther = new ProductEntity();
        CategoryEntity categoryEntityOther = new CategoryEntity();
        categoryEntityOther.setId(2L);
        categoryEntityOther.setName("Other");
        productEntityOther.setId(2L);
        productEntityOther.setName(DUMMY_STRING);
        productEntityOther.setPrice(50D);
        productEntityOther.setCategory(categoryEntityOther);
        productEntities.add(productEntityOther);
        return productEntities;
    }

    private UserEntity mockedEmployeeUserEntity() {
        UserEntity userEntity = mockedUserEntity();
        userEntity.setType(UserType.EMPLOYEE);
        return userEntity;
    }

    private UserEntity mockedAffiliateUserEntity() {
        UserEntity userEntity = mockedUserEntity();
        userEntity.setType(UserType.AFFILIATE);
        return userEntity;
    }

    private UserEntity mockedCustomerOver2YearsUserEntity() {
        UserEntity userEntity = mockedUserEntity();
        userEntity.setType(UserType.CUSTOMER);
        userEntity.setJoiningDate(LocalDate.now().minusYears(3));
        return userEntity;
    }

    private UserEntity mockedCustomerLessThan2YearsUserEntity() {
        UserEntity userEntity = mockedUserEntity();
        userEntity.setType(UserType.CUSTOMER);
        userEntity.setJoiningDate(LocalDate.now().minusYears(1));
        return userEntity;
    }

    private UserEntity mockedUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(DUMMY_ID);
        userEntity.setName(DUMMY_STRING);
        userEntity.setJoiningDate(LocalDate.now());
        return userEntity;
    }
}
