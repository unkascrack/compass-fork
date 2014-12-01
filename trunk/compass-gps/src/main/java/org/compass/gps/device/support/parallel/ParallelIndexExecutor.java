/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.compass.gps.device.support.parallel;

import org.compass.gps.CompassGpsException;
import org.compass.gps.spi.CompassGpsInterfaceDevice;

/**
 * Indexes a list of groups of {@link IndexEntity}
 * already partitioned using the provided {@link IndexEntitiesIndexer}.
 *
 * @author kimchy
 * @see AbstractParallelGpsDevice
 * @see IndexEntitiesIndexer
 * @see IndexEntity
 */
public interface ParallelIndexExecutor {

    /**
     * Indexes a list of groups of index entities already partitioned using the provided
     * index entities indexer.
     *
     * @param entities             The entities to index
     * @param indexEntitiesIndexer The indexer to use in order to index the entities
     * @param compassGps           The gps interface to use in order to obtain meta information
     * @throws org.compass.gps.CompassGpsException
     */
    void performIndex(IndexEntity[][] entities, IndexEntitiesIndexer indexEntitiesIndexer,
                      CompassGpsInterfaceDevice compassGps) throws CompassGpsException;
}