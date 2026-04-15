package com.datnguyeni.shop_backend.controller;


import com.datnguyeni.shop_backend.dto.responseDTO.ApiResponse;
import com.datnguyeni.shop_backend.dto.responseDTO.RevenueResponse;
import com.datnguyeni.shop_backend.repository.OrderRepository;
import com.datnguyeni.shop_backend.service.DashboardService;
import com.datnguyeni.shop_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashBoardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

//    @GetMapping("/revenue")
//    public ApiResponse<RevenueResponse> getRevenueStats() {
//
//        RevenueResponse revenueResponse = orderService.getRevenueStats();
//
//        return ApiResponse.<RevenueResponse>builder()
//                .code(HttpStatus.OK.value())
//                .message("get data successfully")
//                .data(revenueResponse)
//                .build();
//    }

    @GetMapping("/revenue")
    public ApiResponse<RevenueResponse> getRevenueStats(@RequestParam(defaultValue = "this_week") String period) {

        RevenueResponse revenueResponse = dashboardService.getDashboardStats(period);

        return ApiResponse.<RevenueResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Get dashboard data successfully")
                .data(revenueResponse)
                .build();
    }


}
