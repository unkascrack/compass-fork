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

package org.compass.gps.device.jdbc.snapshot;

import org.compass.gps.device.jdbc.JdbcGpsDeviceException;

/**
 * A jdbc snapshot persister, resposible for persisting and loading a
 * {@link JdbcSnapshot} from a persistance
 * store. Examples for stores can be RAM and FS (File System).
 * 
 * @see org.compass.gps.device.jdbc.snapshot.JdbcSnapshotPersister
 * @see RAMJdbcSnapshotPersister
 * @see FSJdbcSnapshotPersister
 * 
 * @author kimchy
 */
public interface JdbcSnapshotPersister {

    /**
     * Saves the jdbc snapshot to the store.
     * 
     * @param snapshot
     * @throws org.compass.gps.device.jdbc.JdbcGpsDeviceException
     */
    void save(JdbcSnapshot snapshot) throws JdbcGpsDeviceException;

    /**
     * Loads the jdbc snapshot from the store.
     * 
     * @return The jdbc snapshot
     * @throws org.compass.gps.device.jdbc.JdbcGpsDeviceException
     */
    JdbcSnapshot load() throws JdbcGpsDeviceException;
}
