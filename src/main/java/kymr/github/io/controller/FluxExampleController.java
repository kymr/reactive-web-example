/**
 * @(#) FluxExampleController.class $version 2016. 12. 14
 * <p>
 * Copyright 2007 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kymr.github.io.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

/**
 * FluxExampleController 
 */
@RestController
public class FluxExampleController {
    @RequestMapping("/flux")
    public Flux<String> flux(String name) {
        return getList(name);
    }

    private Flux<String> getList(String name) {
        return Flux.fromStream(
                Stream.iterate(1, i -> i + 1).limit(10))
            .map(i -> name + i);
    }
}