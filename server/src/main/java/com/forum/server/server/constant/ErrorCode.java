package com.forum.server.server.constant;

public class ErrorCode {
  public static final String SUCCESS = "GEN-00";
  public static final String BODY_NOT_VALID = "GEN-01";
  public static final String DATABASE_NOT_CONNECTED = "GEN-02";
  public static final String CANNOT_ACCESS_SERVICE = "GEN-03";
  public static final String SERVICE_ERROR = "GEN-04";

  public static final String USER_NOT_FOUND = "ATH-01";
  public static final String FAILED_LOGIN = "ATH-02";
  public static final String USER_IS_ON_LEAVE = "ATH-03";
  public static final String UNAUTHENTICATED = "ATH-04";
  public static final String UNAUTHORIZED = "ATH-05";
  public static final String USER_IS_NOT_ACTIVE = "ATH-06";

  public static final String ENGINE_MATCHING_TIMEOUT = "SEM-01";
  public static final String ICR_CAPTURE_FAILED = "ICR-01";
}
