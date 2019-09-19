package hello.model.db;

import hello.model.Order;

import java.util.List;
import java.util.Optional;

public class DbClaim {
    private String claimCaseNumber;
    private List<Optional<Order>> orders;

    public DbClaim(String claimCaseNumber, List<Optional<Order>> orders) {
        this.claimCaseNumber = claimCaseNumber;
        this.orders = orders;
    }

    public String getClaimCaseNumber() {
        return claimCaseNumber;
    }

    public void setClaimCaseNumber(String claimCaseNumber) {
        this.claimCaseNumber = claimCaseNumber;
    }

    public List<Optional<Order>> getOrders() {
        return orders;
    }

    public void setOrders(List<Optional<Order>> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "DbClaimCase{" +
                "claimCaseNumber='" + claimCaseNumber + '\'' +
                ", orders=" + orders +
                '}';
    }
}
