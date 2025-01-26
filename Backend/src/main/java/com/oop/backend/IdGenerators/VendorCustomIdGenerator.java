package com.oop.backend.IdGenerators;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.text.DecimalFormat;

public class VendorCustomIdGenerator  implements IdentifierGenerator {
    private static final DecimalFormat df = new DecimalFormat("0");

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Long count = (Long) session.createQuery("select count(id) from VendorEntity").uniqueResult();
        return df.format(count + 1) + "V"; // Generates IDs in the format: 1V, 2V, etc.
    }
}
