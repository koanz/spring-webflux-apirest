package com.idea.springboot.webflux.app.mappers;

import com.idea.springboot.webflux.app.models.documents.Product;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "categoryDTO", target = "category")
    Product toEntity(ProductDTO dto);

    @Mapping(source = "category", target = "categoryDTO")
    ProductDTO toDto(Product product);
}
