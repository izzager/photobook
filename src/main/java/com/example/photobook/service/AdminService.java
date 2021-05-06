package com.example.photobook.service;

import com.example.photobook.dto.PlatformUsageDto;
import com.example.photobook.dto.UserActivityDto;

import java.util.List;

public interface AdminService {
    List<UserActivityDto> findLastUserActivities();
    List<PlatformUsageDto> getPlatformStatistics();
}
