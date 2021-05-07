package com.example.photobook.controller;

import com.example.photobook.dto.PlatformUsageDto;
import com.example.photobook.dto.UserActivityDto;
import com.example.photobook.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("findLastUserActivities")
    public List<UserActivityDto> findLastUserActivities() {
        return adminService.findLastUserActivities();
    }

    @GetMapping("getPlatformStatistics")
    public List<PlatformUsageDto> getPlatformStatistics() {
        return adminService.getPlatformStatistics();
    }

}
