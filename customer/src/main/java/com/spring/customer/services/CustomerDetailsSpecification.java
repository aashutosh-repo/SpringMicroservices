package com.spring.customer.services;

import com.spring.customer.customer.CustomerDetails;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class CustomerDetailsSpecification {
        public static Specification<CustomerDetails> hasCustomerId(String customerId) {
        return (root, query, cb) ->
                customerId == null ? null :
                        cb.equal(root.get("customerId").get("customerId"), customerId);
    }

    public static Specification<CustomerDetails> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.or(
                cb.like(cb.lower(root.get("firstName")), "%" + name.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("lastName")), "%" + name.toLowerCase() + "%")
        );
    }

    public static Specification<CustomerDetails> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<CustomerDetails> isBetweenDates(LocalDate start, LocalDate end) {
        return (root, query, cb) -> {
            if (start == null || end == null) return null;
            return cb.between(root.get("custCreationDt"), start, end);
        };
    }
}
