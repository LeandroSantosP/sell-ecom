package com.leandrosps.demo_sell_ecom.geteways;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

public interface MyClock {
    void setCurrentDate(LocalDateTime date);

    LocalDateTime getCurrentDate();
}

@Component
class FackClock implements MyClock {

    private LocalDateTime date = LocalDateTime.now();

    @Override
    public void setCurrentDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public LocalDateTime getCurrentDate() {
        return this.date;
    }

}
