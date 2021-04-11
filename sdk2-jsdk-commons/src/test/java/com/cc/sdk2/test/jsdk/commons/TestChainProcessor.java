package com.cc.sdk2.test.jsdk.commons;

import com.cc.sdk2.jsdk.commons.processor.ChainProcessor;

import java.util.Collections;
import java.util.List;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/4/11 13:23
 **/
public class TestChainProcessor {
    public static void main(String[] args) {
        ChainProcessor<String> fetch = new ChainProcessor<String>() {
            @Override
            protected boolean process(String data) {
                System.out.println("layer 1.....");
                return true;
            }
        };
        ChainProcessor.Context context = fetch.new Context();
        context.addConData("test", "test1");
        context.addConData("test1", Collections.EMPTY_LIST);
        fetch.setContext(context);
        fetch.addProcessor(new ChainProcessor<String>() {
            @Override
            protected boolean process(String data) {
                System.out.println("layer 2.....");
                String test = getContext().getContextData("test");
                System.out.println(test);
                List<String> list = getContext().getContextData("test1");
                System.out.println(list);
                return true;
            }
        }.setContext(context));
        fetch.start("hello");
    }
}
