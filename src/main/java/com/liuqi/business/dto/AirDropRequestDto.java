package com.liuqi.business.dto;

import com.liuqi.response.ReturnResponse;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author tanyan
 * @create 2020-03=30
 * @description
 */
@Data
public class AirDropRequestDto {

    private CompletableFuture<ReturnResponse> future;

}
