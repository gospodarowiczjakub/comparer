package hello.jdbc.deprecated;

import hello.model.Order;

import java.util.List;
import java.util.Optional;

public class OrderDAO implements Dao<Order> {

    @Override
    public Optional<Order> get(long id) {
        //TODO delete
        return null;
    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public void save(Order order) {

    }

    @Override
    public void update(Order order, String[] params) {

    }

    @Override
    public void delete(Order order) {

    }
}
