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

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Assert;
import org.junit.Test;
import zipkin2.Span;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TracingProducerInterceptorTest extends BaseTracingTest {

	private final ProducerRecord<String, String> record = new ProducerRecord<>("topic",
			"value");

	@Test
	public void shouldNotTouchRecords() {
		final TracingProducerInterceptor<String, String> interceptor = new TracingProducerInterceptor<>();
		interceptor.configure(map);
		final ProducerRecord<String, String> tracedRecord = interceptor.onSend(record);
		Assert.assertEquals(record, tracedRecord);
	}

	@Test
	public void shouldCreateSpanOnSend() {
		// Given
		final TracingProducerInterceptor<String, String> interceptor = new TracingProducerInterceptor<>();
		interceptor.configure(map);
		interceptor.tracing = tracing;
		// When
		interceptor.onSend(record);
		// Then
		final Span span = spans.getLast();
		assertNotNull(span);
	}

	@Test
	public void shouldCreateChildSpanIfContextAvailable() {
		// Given
		final TracingProducerInterceptor<String, String> interceptor =
				new TracingProducerInterceptor<>();
		interceptor.configure(map);
		interceptor.tracing = tracing;
		brave.Span span = tracing.tracer().newTrace();
		tracing.propagation()
				.injector(KafkaInterceptorPropagation.HEADER_SETTER)
				.inject(span.context(), record.headers());
		// When
		interceptor.onSend(record);
		// Then
		final Span child = spans.getLast();
		assertNotNull(child);
		assertEquals(span.context().spanIdString(), child.parentId());
	}

}
