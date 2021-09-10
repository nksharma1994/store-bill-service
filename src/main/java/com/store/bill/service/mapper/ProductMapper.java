package com.store.bill.service.mapper;

import com.store.bill.service.dto.response.ProductSummary;
import com.store.bill.service.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "categoryName", source = "category.name")
    ProductSummary productEntityToProductSummary(ProductEntity productEntity);
}
