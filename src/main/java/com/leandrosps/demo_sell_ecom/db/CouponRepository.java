package com.leandrosps.demo_sell_ecom.db;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.jdbc.core.simple.JdbcClient;

import com.leandrosps.demo_sell_ecom.db.dbmodels.CouponDbModel;
import com.leandrosps.demo_sell_ecom.domain.MyCoupon;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;

public interface CouponRepository extends ListCrudRepository<CouponDbModel, String>, CouponRepositoryCustom {
}

interface CouponRepositoryCustom {
    void persiste(MyCoupon coupon);

    MyCoupon getByCode(String code);

    void update(MyCoupon coupon);
}

class CouponRepositoryCustomImpl implements CouponRepositoryCustom {
    private JdbcClient jdbcClient;

    public CouponRepositoryCustomImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void persiste(MyCoupon coupon) {
        String sqlInsertCoupon = """
                INSERT INTO coupons (code, percentage, is_available, usage_limit, used, expired_at, created_at)
                VALUES (:code, :percentage, :is_available, :usage_limit, :used, :expired_at, :created_at)
                """;


        this.jdbcClient.sql(sqlInsertCoupon).param("code", coupon.getCode()).param("percentage", coupon.getPercentage())
                .param("is_available", coupon.isAvailable()).param("usage_limit", coupon.getQuantity())
                .param("used", coupon.getUsed()).param("expired_at", coupon.getExpiredAt())
                .param("created_at", coupon.getCreatedAt()).update();
       
    }

    @Override
    public MyCoupon getByCode(String code) {
        String sql = """
                SELECT * from coupons WHERE code = :code
                """;

        var couponDbData = this.jdbcClient.sql(sql).param("code", code).query(CouponDbModel.class).optional()
                .orElseThrow(() -> new NotFoundEx("INVALID COUPON: " + code));
        return new MyCoupon(couponDbData.getCode(), couponDbData.getPercentage(), couponDbData.is_available(),
                couponDbData.getUsage_limit(), couponDbData.getUsed(), couponDbData.getExpired_at(),
                couponDbData.getCreated_at());
    }

    @Override
    public void update(MyCoupon coupon) {

        String sqlUpdateCoupon = """
                UPDATE coupons
                SET used = :used,
                is_available = :is_available,
                expired_at = :expired_at
                WHERE code = :code
                """;

        this.jdbcClient.sql(sqlUpdateCoupon).param("code", coupon.getCode()).param("used", coupon.getUsed())
                .param("is_available", coupon.isAvailable()).param("expired_at", coupon.getExpiredAt()).update();
    }
}