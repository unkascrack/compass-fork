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

package org.compass.core.test.engine.lucene.transaction.async;

import org.compass.core.config.CompassEnvironment;
import org.compass.core.config.CompassSettings;

/**
 * @author kimchy
 */
public class FSAsyncTransactionTests extends AbstractAsyncTransactionTests {

    @Override
    protected CompassSettings buildCompassSettings() {
        CompassSettings settings =  super.buildCompassSettings();
        settings.setSetting(CompassEnvironment.CONNECTION, "target/test-index");
        return settings;
    }

    @Override
    protected void sleepForChangesToOccur() {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted", e);
        }
    }
}