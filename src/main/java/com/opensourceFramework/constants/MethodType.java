package com.opensourceFramework.constants;

public enum MethodType {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS,
    TRACE,
    CONNECT,
    POSTBYTES;

//    @Override
//    public String toString() {
//        switch (this) {
//            case GET:
//                return "GET";
//            case POST:
//                return "POST";
//            case PUT:
//                return "PUT";
//            case DELETE:
//                return "DELETE";
//            case PATCH:
//                return "PATCH";
//            case HEAD:
//                return "HEAD";
//            case OPTIONS:
//                return "OPTIONS";
//            case TRACE:
//                return "TRACE";
//            case CONNECT:
//                return "CONNECT";
//            case POSTBYTES:
//                return "POSTBYTES";
//            default:
//                return super.toString();
//        }
//    }
//
//    public static MethodType fromString(String method) {
//        switch (method.toUpperCase()) {
//            case "GET":
//                return GET;
//            case "POST":
//                return POST;
//            case "PUT":
//                return PUT;
//            case "DELETE":
//                return DELETE;
//            case "PATCH":
//                return PATCH;
//            case "HEAD":
//                return HEAD;
//            case "OPTIONS":
//                return OPTIONS;
//            case "TRACE":
//                return TRACE;
//            case "CONNECT":
//                return CONNECT;
//            case "POSTBYTES":
//                return POSTBYTES;
//            default:
//                throw new IllegalArgumentException("Unknown method type: " + method);
//        }
//    }
}