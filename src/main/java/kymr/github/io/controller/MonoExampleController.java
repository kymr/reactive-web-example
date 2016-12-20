/**
 * @(#) MonoExampleController.class $version 2016. 12. 14
 * <p>
 * Copyright 2007 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kymr.github.io.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * MonoExampleController 
 */
@Slf4j
@RestController
public class MonoExampleController {
    @RequestMapping("/mono")
    public Mono<String> mono(String name) {
        return Mono.just(name)
            .log()
            .map(s -> "Mono : " + name);
    }
}