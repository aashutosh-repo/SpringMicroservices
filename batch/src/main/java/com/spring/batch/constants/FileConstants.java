package com.spring.batch.constants;

import lombok.Data;

@Data
public abstract class FileConstants {
    public static final String FILE_TYPE_USER = "USER_DATA";
    public static final String FILE_TYPE_PINCODE = "INDIA_PINCODE";
    public static final String FILE_TYPE_POPULATION = "INDIA_POPULATION";
    public static final String FILE_TYPE_TRANSACTION = "PAYMENTS";

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED = "FAILED";

    public static final String ERROR_CODE_INVALID_FORMAT = "INVALID_FORMAT";
    public static final String ERROR_CODE_MISSING_FIELD = "MISSING_FIELD";
    public static final String ERROR_CODE_DUPLICATE_ENTRY = "DUPLICATE_ENTRY";
    public static final String ERROR_CODE_UNKNOWN = "UNKNOWN_ERROR";
}
