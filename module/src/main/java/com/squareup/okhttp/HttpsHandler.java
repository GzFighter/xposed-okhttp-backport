/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.squareup.okhttp;

import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public final class HttpsHandler extends HttpHandler {
    private static final List<String> ENABLED_TRANSPORTS = Arrays.asList("http/1.1");

    @Override
    protected int getDefaultPort() {
        return 443;
    }

    @Override
    protected OkHttpClient newOkHttpClient(Proxy proxy) {
        OkHttpClient client = super.newOkHttpClient(proxy);
        client.setTransports(ENABLED_TRANSPORTS);

        HostnameVerifier verifier = HttpsURLConnection.getDefaultHostnameVerifier();
        // Assume that the internal verifier is better than the
        // default verifier.
        try {
            if (!Class.forName("javax.net.ssl.DefaultHostnameVerifier").isInstance(verifier)) {
                client.setHostnameVerifier(verifier);
            }
        } catch (ClassNotFoundException e) {
            // if we cannot find the class than let's NOT taper with it
        }

        return client;
    }
}