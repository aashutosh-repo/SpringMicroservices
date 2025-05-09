package com.spring.customer.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration
public class HazelcastConfiguration {

    @Bean
    public Config hazleCastConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance");

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(5710).setPortAutoIncrement(true);
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        networkConfig.getJoin().getTcpIpConfig()
                .setEnabled(true)
                .addMember("127.0.0.1") // for Local
                .addMember("192.168.1.101") // for Other IP
                .addMember("192.168.1.102"); // for Other IP

        Map<String, Integer> caches = Map.of(
                "allCustomersCache", 3600,
                "customerByIdCache", 1800,
                "filteredCustomerCache", 1200,
                "customerRegionCache", 1800,
                "customerTypeCache", 900
                // Add more here...
        );

        for (Map.Entry<String, Integer> entry : caches.entrySet()) {
            String name = entry.getKey();
            int ttl = entry.getValue();
            MapConfig mapConfig = new MapConfig();
            mapConfig.setName(name)
                    .setTimeToLiveSeconds(ttl)
                    .setMaxIdleSeconds(600)
                    .setBackupCount(2);
            config.addMapConfig(mapConfig);
        }
        return config;
    }
}
