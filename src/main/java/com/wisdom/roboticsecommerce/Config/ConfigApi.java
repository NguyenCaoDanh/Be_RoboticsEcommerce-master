package com.wisdom.roboticsecommerce.Config;

public class ConfigApi {
    static String[] PUBLIC_API = {
            "/images/**",

            "/api/auth/signin",
            "/api/auth/signup",

            "/api/category/search",
            "/api/category/findbyid",

            "/api/email/**",

            "/api/promotion/search",

            "/api/product/detail",
            "/api/product/searchKeyword",

            "/api/ghn/address/province/all",
            "/api/ghn/address/province/district/all",
            "/api/ghn/address/province/district/ward/all",
            "/api/ghn/service/all",
            "/api/ghn/fee-and-delivery-time/calculate",

            "/api/feedback/product/all",

            "/api/test"
    };
    static String[] ADMIN_USER_API = {
            "/api/account/info/detail",
            "/api/account/info/update",
            "/api/account/password/change",

            "/api/notification/all",
            "/api/notification/detail",

            "/api/product/findbyid",

            "/api/ghn/order/soc",
            "/api/ghn/order/create",
            "/api/ghn/store/detail",
            "/api/ghn/order/status",
            "/api/ghn/order/cancel"
    };
    static String[] ADMIN_API = {
            "/api/product/delete",
            "/api/product/create",
            "/api/product/update",
            "/api/product/search",

            "/api/category/create",
            "/api/category/update",
            "/api/category/delete",

            "/api/promotion/**",

            "/api/order/search",
            "/api/order/findById",
            "/ghn/order/print",

            "/api/account/search",

            "/api/feedback/detail",
            "/api/feedback/all",
            "/api/feedback/reply/create",
            "/api/feedback/reply/update",
            "/api/feedback/reply/delete",
            "/api/feedback/status/update",

            "/api/revenue/get"
    };
    static String[] USER_API = {
            "/api/payment/**",

            "/api/address/create",
            "/api/address/update",
            "/api/address/all",
            "/api/address/detail",
            "/api/address/delete",

            "/api/cart/getAllCart",
            "/api/cart/add",
            "/api/cart/update",
            "/api/cart/delete",

            "/api/order/updateStatus",
            "/api/order/createOrder",
            "/api/order/getAllOrdersByUserId",
            "/api/order/getOrdersByUserIdAndStatus",
            "/api/order/detail",
            "/api/order/cancelOrder",
            "/api/order/updateOrderStatus",
            "/api/order/addFeedback",

            "/api/order-detail/all",

            "/api/feedback/create"
    };
}
