package com.project.NexPay.payment.mapper;

import com.project.NexPay.payment.dto.response.OrderResponse;
import com.project.NexPay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord order);

}
