/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb;

import java.util.HashMap;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

/**
 * A publish/subscribe style event bus that runs within a single Java virtual machine.
 * <p>
 * Publishers and subscribes are grouped by <i>domain</i> and (optionally) by a <i>partition</i> within a domain.
 * <p>
 * A <i>domain</i> is an integer that identifies a set of publishers and subscribers.
 * <p>
 * A <i>partition</i> is a subset of publishers and subscribers within a domain identified by a <i>path</i>.
 * <p>
 * A <i>path</i> is a list of string identifiers separated by the '/' character. For example:
 * <code>/data/repository</code>.
 * <p>
 * A publisher and subscriber must be registered on the same domain and partition to communicate.
 * <p>
 * 
 * @author jgraham
 */
public class PubSubEventBus {
    private static class DomainImpl implements Domain {
        private HashMap<String, EventBus> partitions = new HashMap<String, EventBus>();
        private final int id;

        DomainImpl(int id) {
            this.id = id;
        }

        @Override
        public EventBus createPartition(String partition) {
            EventBus eventBus = partitions.get(partition);
            if (eventBus == null) {
                partitions.put(partition, new AsyncEventBus(java.util.concurrent.Executors.newCachedThreadPool()));
                eventBus = partitions.get(partition);
            }

            return eventBus;
        }

        @Override
        public EventBus getPartition(String partition) {

            if (!partitions.containsKey(partition)) {
                throw new java.lang.IllegalArgumentException("undefined partition: " + partition + " domain: " + id);
            }

            return partitions.get(partition);
        }
    }

    private static PubSubEventBus instance;

    /**
     * @return the singleton instance of the event bus.
     */
    public static PubSubEventBus getInstance() {
        if (instance == null) {
            instance = new PubSubEventBus();
        }

        return instance;
    }

    private HashMap<Integer, Domain> domains = new HashMap<Integer, Domain>();

    private PubSubEventBus() {
    }

    public Domain createDomain(int domainId) {
        Domain domain = domains.get(domainId);

        if (domain == null) {
            domain = new DomainImpl(domainId);
            domains.put(domainId, domain);
        }

        return domain;
    }

    public Domain getDomain(int domainId) {
        if (!domains.containsKey(domainId)) {
            throw new java.lang.IllegalArgumentException("undefined domain: " + domainId);
        }

        return domains.get(domainId);
    }

    public void reset() {
        domains.clear();
    }
}
