package com.takipi.sdk.v1.internal.impl;

import java.util.UUID;

import com.takipi.sdk.v1.api.TakipiOptions;
import com.takipi.sdk.v1.internal.TakipiClient;
import com.takipi.sdk.v1.internal.debug.TakipiDebugClient;
import com.takipi.sdk.v1.internal.impl.agent.TakipiAgentClientFactory;
import com.takipi.sdk.v1.internal.impl.agent.TakipiAgentDetector;
import com.takipi.sdk.v1.internal.impl.nop.TakipiNopClientImpl;

public class TakipiClientFactory {
	
	public static TakipiClient create(String frameworkId, TakipiOptions options) {
		String clientId = generateClientId();
		
		TakipiAgentDetector agentDetector = TakipiAgentDetector.create();
		
		if (!agentDetector.isAgentPresent()) {
			return TakipiNopClientImpl.create(frameworkId, clientId, options);
		}
		
		int agentMaxSupportedProtocolVersion = agentDetector.getMaxSupportedProtocolVersion();
		
		TakipiClient client = TakipiAgentClientFactory.create(
				frameworkId, clientId, options, agentMaxSupportedProtocolVersion);
		
		if (options.isDebugEnabled()) {
			return TakipiDebugClient.wrap(client);
		} else {
			return client;
		}
	}
	
	private static String generateClientId() {
		return UUID.randomUUID().toString();
	}
}
