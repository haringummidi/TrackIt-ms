package com.shellonfire.trackitms.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DashBoardInformation {
    private Long totalOrders;
    private Long dailyOrders;
    private Long todaySaleAmount;
    private Long totalItems;
    private Long numberOfAvailableItems;
    private Long numberOfLowStockItems;
    private Long numberOfOutOfStockItems;
    private List<OrderDto> latestOrders;
    private List<String> importantAlerts;
}
