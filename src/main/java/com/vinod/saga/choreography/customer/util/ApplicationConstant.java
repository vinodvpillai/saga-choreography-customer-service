package com.vinod.saga.choreography.customer.util;

import java.math.BigDecimal;

public final class ApplicationConstant {

    /**
     * Instantiates a new application constant.
     */
    private ApplicationConstant() {
    }

    public static final BigDecimal MAX_CREDIT_LIMIT = new BigDecimal(200);

    //SQS
    public static final String CREDIT_RESERVED_SQS = "Credit_Reserved";
    public static final String CREDIT_LIMIT_EXCEEDED_SQS = "Credit_Limit_Exceeded";

    /**
     * The Enum Customer Status.
     */
    public enum CustomerStatus {
        PENDING("Pending"),
        REGISTERED("Registered"),
        BLOCKED("Blocked");

        /** The value. */
        private final String value;

        CustomerStatus(final String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}
