package com.mplatforma.amr.service;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 2/13/14
 * Time: 8:42 AM
 * To change this template use File | Settings | File Templates.
 */
@Startup
@Singleton
public class ESClientBean {
    private TransportClient client;
    @PostConstruct
    public void init() {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "elasticsearch_databankalliance_Prj_Cluster").build();
        client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    }
    @PreDestroy
    public void release() {
        getClient().close();
    }

    public TransportClient getClient() {
        return client;
    }
}
