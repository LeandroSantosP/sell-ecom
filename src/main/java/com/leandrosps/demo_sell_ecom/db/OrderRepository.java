package com.leandrosps.demo_sell_ecom.db;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.leandrosps.demo_sell_ecom.domain.Order;

@Repository
public interface OrderRepository extends ListCrudRepository<OrderDbModel, String>, OrderRepositoryCustom {
}

interface OrderRepositoryCustom {
    void save(Order order);
    Order getOrder(String order_id);
    void update(Order order);
}

class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    @Override
    public void save(Order order) {
        // TODO Auto-generated method stub
        System.out.println("LOOKS OK!");
    }

    @Override
    public Order getOrder(String order_id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrder'");
    }

    @Override
    public void update(Order order) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}
