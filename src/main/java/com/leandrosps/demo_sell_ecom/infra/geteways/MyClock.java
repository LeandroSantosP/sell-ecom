package com.leandrosps.demo_sell_ecom.infra.geteways;

import java.time.LocalDateTime;

public interface MyClock {
    void setCurrentDate(LocalDateTime date);
    LocalDateTime getCurrentDate();
}
