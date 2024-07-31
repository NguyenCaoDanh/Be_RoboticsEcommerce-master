package com.wisdom.roboticsecommerce.Utils;

public class Constants {
    public static final String SUCCESSFUL = "00";
    public static final String SUSPECT = "07";
    public static final String NOT_INTERNET_BANKING = "09";
    public static final String ACCURACY = "10";
    public static final String EXPIRED = "11";
    public static final String LOCK = "12";
    public static final String WRONG_PASSWORD = "13";
    public static final String CANCEL ="24";
    public static final String SURPLUS = "51";
    public static final String TOO_TRADING = "65";
    public static final String MAINTENANCE = "75";
    public static final String WRONG_PASSWORD_SEVERAL_TIMES = "79";
    public static final String OTHER_ERROR = "99";

    public static final Integer STATUS_INACTIVE = 0; // trang thái không hoạt động
    public static final Integer STATUS_ACTIVE = 1; // trang thái đang hoạt động
    public static final Integer DONT_DELETE = 1; // CHƯA XOÁ
    public static final Integer DELETED = 0; // XOÁ

    // trạng thái đơn hàng
    public static final Integer	CONFIRM = 1; // xác nhận
    public static final Integer DELIVERING = 2; // đang giao
    public static final Integer DELIVERED = 3; // đã giao
    public static final Integer DROP = 4; // hủy đơn hàng
    public static final Integer RECEIVED = 5; // đã nhận
    // trạng thái thanh toán
    public static final Integer PAID = 1; // đã thanh toán
    public static final Integer UN_PAID = 2; // chưa thanh toán
    public static final Integer CANCELS = 3; // hủy thanh toán

}
