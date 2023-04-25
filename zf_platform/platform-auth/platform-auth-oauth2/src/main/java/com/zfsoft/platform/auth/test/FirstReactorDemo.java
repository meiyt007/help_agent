//package com.zfsoft.platform.auth.test;
//
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * 第一个Reactor程序
// */
//public class FirstReactorDemo {
//    public static void main(String[] args) {
//        // 测试Flux
//        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::println);
//        System.out.println("----------------------------");
//
//        // 测试Mono
//        Mono.just(1).subscribe(System.out::println);
//        System.out.println("----------------------------");
//
//        // 测试2个参数的subscribe方法
//        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::print, System.err::println);
//        System.out.println("\n----------------------------");
//
//        // 测试3个参数的subscribe方法
//        Flux.just(1, 2, 3, 4, 5, 6)
//                .subscribe(System.out::print, System.err::println,
//                        () -> System.out.println("\ncomplete"));
//        System.out.println("----------------------------");
//        System.out.println("----------------------------");
//        System.out.println("----------------------------");
//
//        // 测试4个参数的subscribe方法
//        Flux.just(1, 2, 3, 4, 5, 6)
//                .subscribe(System.out::print, (errorConsumer) -> System.err.println("\n异常终止"),
//                        () -> System.out.println("\ncomplete"), subscription -> {
//                            System.out.println("订阅发生了");
//                            subscription.request(10);
//                            throw new RuntimeException("异常");
//
//                        });
//    }
//}