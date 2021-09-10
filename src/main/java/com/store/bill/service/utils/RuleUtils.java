package com.store.bill.service.utils;

import com.store.bill.service.dto.response.BillSummary;
import com.store.bill.service.dto.response.ProductSummary;
import com.store.bill.service.dto.response.User;
import com.store.bill.service.entity.enums.UserType;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class RuleUtils {

    private RuleUtils() {
        //private constructor to hide implicit one
    }

    // We can also use Apache Drools for Rule engine so to make rules from file which is outside code
    public static BillSummary applyRules(BillSummary billSummary) {
        User user = billSummary.getUser();
        Double discountPercentage = getDiscountPercentage(user.getType(), user.getJoiningDate());
        log.info("Discount percentage {} for User:{} ", discountPercentage, user);
        Double grossPrice = 0D;
        Double amountEligibleForDiscount = 0D;
        for(ProductSummary productSummary : billSummary.getProductSummaries()) {
            if(!"Grocery".equals(productSummary.getCategoryName())) {
                amountEligibleForDiscount+=productSummary.getGrossPrice();
            }
            grossPrice+=productSummary.getGrossPrice();
        }
        log.info("Amount eligible for discount: " + amountEligibleForDiscount);
        double discount = amountEligibleForDiscount*discountPercentage/100;
        log.info("Discount due to discount percentage : " + discount);
        double netPayablePrice = grossPrice - discount;
        log.info("Net price due to discount : " + netPayablePrice);
        int discountOnNetPayablePrice = Double.valueOf(netPayablePrice/100).intValue() * 5 ;
        log.info("Discount on net price (5 per 100) : " + discountOnNetPayablePrice);
        netPayablePrice-=discountOnNetPayablePrice;
        log.info("Net price after discount (5 per 100) : " + netPayablePrice);
        billSummary.setGrossPrice(grossPrice);
        billSummary.setDiscount(discount + discountOnNetPayablePrice);
        billSummary.setNetPayablePrice(netPayablePrice);
        return billSummary;
    }

    private static double getDiscountPercentage(UserType type, LocalDate joiningDate) {
        double discountPercentage = 0D;
        if(UserType.EMPLOYEE.equals(type)) {
            discountPercentage = 30D;
        } else if(UserType.AFFILIATE.equals(type)) {
            discountPercentage = 10D;
        } else if(UserType.CUSTOMER.equals(type) && LocalDate.now().minusYears(2).isAfter(joiningDate)) {
            discountPercentage = 5D;
        }
        return discountPercentage;
    }
}
