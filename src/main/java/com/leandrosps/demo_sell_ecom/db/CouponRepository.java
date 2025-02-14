package com.leandrosps.demo_sell_ecom.db;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.jdbc.core.simple.JdbcClient;

import com.leandrosps.demo_sell_ecom.db.dbmodels.CouponDbModel;
import com.leandrosps.demo_sell_ecom.domain.MyCoupon;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;

public interface CouponRepository extends ListCrudRepository<CouponDbModel, String>, CouponRepositoryCustom {
}

interface CouponRepositoryCustom {
    MyCoupon getByCode(String code);

    void update(MyCoupon coupon);
}

class CouponRepositoryCustomImpl implements CouponRepositoryCustom {
    private JdbcClient jdbcClient;

    public CouponRepositoryCustomImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
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
                SET used = true
                WHERE code = :code
                """;
        this.jdbcClient.sql(sqlUpdateCoupon).param("code", coupon).update();
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}