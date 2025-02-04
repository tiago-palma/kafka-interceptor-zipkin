/*
 * Copyright 2018-2019 Sysco Middleware
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package no.sysco.middleware.kafka.interceptor.zipkin;

import brave.Tracing;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import zipkin2.Span;

class BaseTracingTest {

	ConcurrentLinkedDeque<Span> spans = new ConcurrentLinkedDeque<>();

	Tracing tracing = Tracing.newBuilder().spanReporter(spans::add).build();

	HashMap<String, Object> map = new HashMap<>();

	BaseTracingTest() {
		map.put("client.id", "client-1");
		map.put("group.id", "group-1");
	}

}
