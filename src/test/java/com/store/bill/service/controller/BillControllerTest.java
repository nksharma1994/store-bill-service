package com.store.bill.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.bill.service.dto.request.OrderProductSummary;
import com.store.bill.service.dto.request.OrderSummary;
import com.store.bill.service.dto.response.BillSummary;
import com.store.bill.service.service.BillService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BillController.class)
public class BillControllerTest {
    @MockBean
    private BillService billService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldFailGetBillSummaryForUserIdNotSupplied() throws Exception {
        mockMvc.perform(post("/store/bill/summary")
                .content(objectMapper.writeValueAsString(OrderSummary.builder()
                        //missing userId
                        .orderProductSummaries(List.of(OrderProductSummary.builder()
                                .productId(1L)
                                .quantity(1)
                                .build()))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailGetBillSummaryForProductIdNotSupplied() throws Exception {
        mockMvc.perform(post("/store/bill/summary")
                .content(objectMapper.writeValueAsString(OrderSummary.builder()
                        .userId(1L)
                        .orderProductSummaries(List.of(OrderProductSummary.builder()
                                // missing productId
                                .quantity(1)
                                .build()))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldFailGetBillSummaryForNegativeQuantity() throws Exception {
        mockMvc.perform(post("/store/bill/summary")
                .content(objectMapper.writeValueAsString(OrderSummary.builder()
                        .userId(1L)
                        .orderProductSummaries(List.of(OrderProductSummary.builder()
                                .productId(1L)
                                .quantity(-1)
                                .build()))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailGetBillSummaryForZeroQuantity() throws Exception{
        mockMvc.perform(post("/store/bill/summary")
                .content(objectMapper.writeValueAsString(OrderSummary.builder()
                        .userId(1L)
                        .orderProductSummaries(List.of(OrderProductSummary.builder()
                                .productId(1L)
                                .quantity(0)
                                .build()))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetBillSummary() throws Exception {
        Mockito.when(billService.getBillSummary(ArgumentMatchers.any())).thenReturn(BillSummary.builder().build());

        mockMvc.perform(post("/store/bill/summary")
                .content(objectMapper.writeValueAsString(OrderSummary.builder()
                        .userId(1L)
                        .orderProductSummaries(List.of(OrderProductSummary.builder()
                                .productId(1L)
                                .quantity(10)
                                .build()))
                        .build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
