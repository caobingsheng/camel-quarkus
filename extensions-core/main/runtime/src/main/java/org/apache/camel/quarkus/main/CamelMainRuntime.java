/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.quarkus.main;

import java.util.Arrays;

import io.quarkus.runtime.Quarkus;
import org.apache.camel.CamelContext;
import org.apache.camel.quarkus.core.CamelRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of the {@link CamelRuntime} based on camel-main.
 */
public class CamelMainRuntime implements CamelRuntime {
    private static final Logger LOGGER = LoggerFactory.getLogger(CamelMainRuntime.class);
    private final CamelMain main;

    public CamelMainRuntime(CamelMain main) {
        this.main = main;
    }

    @Override
    public void start(String[] args) {
        if (args.length > 0) {
            LOGGER.info("Starting camel-quarkus with args: {}", Arrays.toString(args));
        }

        new Thread(() -> {
            try {
                main.run(args);
            } catch (Exception e) {
                LOGGER.error("Failed to start application", e);
                stop();
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void stop() {
        main.stop();
    }

    @Override
    public int waitForExit() {
        Quarkus.waitForExit();
        return this.main.getExitCode();
    }

    @Override
    public CamelContext getCamelContext() {
        return main.getCamelContext();
    }
}