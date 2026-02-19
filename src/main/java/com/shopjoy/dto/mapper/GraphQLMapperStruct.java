package com.shopjoy.dto.mapper;

import com.shopjoy.dto.request.*;
import com.shopjoy.entity.OrderStatus;
import com.shopjoy.entity.PaymentStatus;
import com.shopjoy.graphql.input.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for GraphQL input types to DTO request types.
 */
@Mapper(
    componentModel = "spring",
    imports = {OrderStatus.class, PaymentStatus.class}
)
public interface GraphQLMapperStruct {

    /**
     * Convert UpdateOrderInput to UpdateOrderRequest.
     */
    @Mapping(target = "status", expression = "java(input.status() != null ? OrderStatus.valueOf(input.status()) : null)")
    @Mapping(target = "paymentStatus", expression = "java(input.paymentStatus() != null ? PaymentStatus.valueOf(input.paymentStatus()) : null)")
    UpdateOrderRequest toUpdateOrderRequest(UpdateOrderInput input);

    /**
     * Convert UpdateOrderItemInput to UpdateOrderItemRequest.
     */
    @Mapping(target = "orderItemId", expression = "java(input.orderItemId() != null ? input.orderItemId().intValue() : null)")
    @Mapping(target = "productId", expression = "java(input.productId() != null ? input.productId().intValue() : null)")
    @Mapping(target = "price", expression = "java(input.price() != null ? input.price().doubleValue() : null)")
    UpdateOrderItemRequest toUpdateOrderItemRequest(UpdateOrderItemInput input);
}