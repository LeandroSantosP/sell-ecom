package com.leandrosps.demo_sell_ecom.infra.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;

import com.leandrosps.demo_sell_ecom.domain.MyCoupon;
import com.leandrosps.demo_sell_ecom.domain.Order;
import com.leandrosps.demo_sell_ecom.domain.OrderItem;
import com.leandrosps.demo_sell_ecom.domain.Status;
import com.leandrosps.demo_sell_ecom.infra.db.dbmodels.CouponDbModel;
import com.leandrosps.demo_sell_ecom.infra.db.dbmodels.OrderDbModel;
import com.leandrosps.demo_sell_ecom.infra.db.dbmodels.OrderItemDbModel;
import com.leandrosps.demo_sell_ecom.infra.errors.NotFoundEx;
import com.leandrosps.demo_sell_ecom.infra.geteways.MyClock;

public interface OrderRepository extends ListCrudRepository<OrderDbModel, String>, OrderRepositoryCustom {
}

interface OrderRepositoryCustom {
	void persist(Order order);

	Order getOrder(String order_id);

	void update(Order order);

	void updated_order_status(Order coupon);
}

class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

	JdbcClient jdbcClient;
	MyClock clock;

	public OrderRepositoryCustomImpl(JdbcClient jdbcClient, MyClock clock) {
		this.jdbcClient = jdbcClient;
		this.clock = clock;
	}

	@Override
	@Transactional
	public void persist(Order order) {
		String coupon = null;
		if (order.getCoupons().size() > 0) {
			coupon = order.getCoupons().get(0).getCode();
		}

		String sqlCreateOrder = """
				INSERT INTO
				    orders (id, total, status, client_id, client_email, coupon, created_at)
				VALUES
				    (:id, :total, :status, :client_id, :client_email, :coupon, :created_at)
				    """;

		this.jdbcClient.sql(sqlCreateOrder).param("id", order.getId())
				/* fix this address later */
				.param("total", order.getTotal()).param("status", order.getStatus())
				.param("client_id", order.getClientId()).param("client_email", order.getClientEmail())
				.param("coupon", coupon).param("created_at", order.getCreateAt()).update();

		for (var item : order.getOrderItems()) {
			String sqlCreateOrderItem = """
					INSERT INTO
					    order_items (id, unity_price, quantity, order_id, product_id)
					VALUES
					    (:id, :unity_price, :quantity, :order_id, :product_id)
					""";
			this.jdbcClient.sql(sqlCreateOrderItem).param("id", item.id()).param("unity_price", item.unityPrice())
					.param("quantity", item.quantity()).param("order_id", order.getId())
					.param("product_id", item.productId()).update();
		}
	}

	@Override
	@Transactional
	public Order getOrder(String order_id) {

		/* Create An Restore method for Order latter */
		if (order_id == null || order_id.isEmpty()) {
			throw new NotFoundEx("Order ID cannot be null or empty");
		}

		var sqlOrderData = """
				SELECT * FROM orders WHERE id = :order_id
				""";

		var orderData = this.jdbcClient.sql(sqlOrderData).param("order_id", order_id).query(OrderDbModel.class)
				.optional().orElseThrow(() -> new NotFoundEx("Order not Found!"));

		var sqlCouponData = """
				SELECT * FROM coupons WHERE code = :code
				""";

		List<MyCoupon> coupons = new ArrayList<>();

		if (orderData.getCoupon() != null) {
			var couponData = this.jdbcClient.sql(sqlCouponData).param("code", orderData.getCoupon())
					.query(CouponDbModel.class).optional().orElseThrow(() -> new NotFoundEx("Coupon not Found!"));
			coupons.add(new MyCoupon(couponData.getCode(), couponData.getPercentage(), couponData.is_available(),
					couponData.getUsage_limit(), couponData.getUsed(), couponData.getExpired_at(),
					couponData.getCreated_at()));
		}

		var sqlOrderItemsData = """
				SELECT * FROM order_items WHERE order_id = :order_id
				""";

		var orderItemsData = this.jdbcClient.sql(sqlOrderItemsData).param("order_id", orderData.getId())
				.query(OrderItemDbModel.class).list();

		List<OrderItem> orderItems = new ArrayList<>();
		orderItemsData.forEach(item -> {
			orderItems
					.add(new OrderItem(item.getId(), item.getUnity_price(), item.getProduct_id(), item.getQuantity()));
		});
		Status status = null;
		if (orderData.getStatus().equals("WAITING_PAYMENT")) {
			status = Status.WAITING_PAYMENT;
		} else if (orderData.getStatus().equals("RECUSSED")) {
			status = Status.RECUSSED;
		} else if (orderData.getStatus().equals("PAID")) {
			status = Status.PAID;
		} else if(orderData.getStatus().equals("CANCEL") ){
			status = Status.CANCEL;
		}
		return new Order(orderData.getId(), orderData.getClient_id(), orderData.getClient_email(), status, orderItems,
				coupons, orderData.getTotal(), orderData.getCreated_at(), clock);
	}

	@Override
	public void update(Order order) {
		this.jdbcClient.sql("""
				SELECT * FROM orders WHERE id = :id;
				""").param("id", order.getId()).query(OrderDbModel.class).optional()
				.orElseThrow(() -> new NotFoundEx("Order not Found!"));
		var updateSql = """
				UPDATE
					orders
				SET
					total = :total, status = :status, coupon = :coupon
				WHERE id = :order_id;
				""";
		this.jdbcClient.sql(updateSql).param("total", order.getTotal()).param("status", order.getStatus())
				.param("order_id", order.getId())
				.param("coupon", order.getCoupons().size() == 1 ? order.getCoupons().get(0).getCode() : null).update();
	}

	@Override
	public void updated_order_status(Order order) {
		this.jdbcClient.sql("""
				UPDATE
					orders SET status = :status
				WHERE
					id = :order_id
				""").param("order_id", order.getId()).param("status", order.getStatus()).update();
	}
}
