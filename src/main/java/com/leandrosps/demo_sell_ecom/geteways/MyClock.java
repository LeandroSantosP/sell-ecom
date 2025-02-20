package com.leandrosps.demo_sell_ecom.geteways;

import java.time.LocalDateTime;

public interface MyClock {
    void setCurrentDate(LocalDateTime date);
    LocalDateTime getCurrentDate();
}
