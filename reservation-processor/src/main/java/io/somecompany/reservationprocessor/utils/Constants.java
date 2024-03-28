package io.somecompany.reservationprocessor.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String RESERVATION_QUEUE = "q.reservation";
    public static final String RESERVATION_EXCHANGE = "x.reservation";
    public static final String RESERVATION_ROUTING_KEY = "reservation";
    public static final String FALL_BACK_RESERVATION_QUEUE = "q.fall-back-reservation";
    public static final String FALL_BACK_RESERVATION_EXCHANGE = "x.reservation-failure";
    public static final String FALL_BACK_ROUTING_KEY = "fall-back";
}
