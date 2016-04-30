/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb;

import com.google.common.eventbus.EventBus;

/**
 * A domain represents a set of partitions, where each partition is a set of publishers and subscribers.
 * <p>
 * 
 * @author jgraham
 */
public interface Domain {
    /**
     * Creates a new partition with the given identifier.
     * 
     * @param partition
     *            the partition identifier.
     * 
     * @return the new partition.
     */
    public EventBus createPartition(String partition);

    /**
     * Returns the partition with the given identifier.
     * 
     * @param partition
     *            the partition identifier.
     * @return the partition with the given identifier.
     * @throws IllegalArgumentException
     *             if the partition does not exist.
     */
    public EventBus getPartition(String partition) throws IllegalArgumentException;
}
