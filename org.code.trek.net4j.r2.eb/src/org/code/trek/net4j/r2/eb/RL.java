/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb;

/**
 * EventBus pub/sub resource locator (RL).
 * 
 * @author jgraham
 */
public final class RL {
    private static final String DOMAIN_NAME_SEPARATOR = ":";
    private String partition = "";
    private int domain = 0;

    public RL(int domain, String partition) {
        this.domain = domain;
        this.partition = partition;
    }

    public RL(String description) {
        if (!description.isEmpty()) {
            String[] tokens = description.split(DOMAIN_NAME_SEPARATOR);
            if ((tokens.length > 0) && !tokens[0].isEmpty()) {
                domain = Integer.parseInt(tokens[0]);
            }

            if (tokens.length > 1 && !tokens[1].isEmpty()) {
                partition = tokens[1];
            }
        }
    }

    public int getDomain() {
        return domain;
    }

    public String getPartition() {
        return partition;
    }

    @Override
    public String toString() {
        String result = partition.isEmpty() ? "/" : partition;
        result = domain + DOMAIN_NAME_SEPARATOR + partition;
        return result;
    }
}
