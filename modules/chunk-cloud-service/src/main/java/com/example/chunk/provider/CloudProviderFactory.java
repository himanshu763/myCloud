package com.example.chunk.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CloudProviderFactory {
    
    private final Map<String, CloudProvider> providerMap;
    
    @Autowired
    public CloudProviderFactory(List<CloudProvider> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(CloudProvider::getProviderName, Function.identity()));
    }
    
    public CloudProvider getProvider(String providerName) {
        CloudProvider provider = providerMap.get(providerName);
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported provider: " + providerName);
        }
        return provider;
    }
    
    public List<String> getAvailableProviders() {
        return List.copyOf(providerMap.keySet());
    }
}