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

package org.compass.core.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.compass.core.Compass;
import org.compass.core.config.CompassEnvironment;
import org.compass.core.config.CompassSettings;

/**
 * @author kimchy
 */
public class TransactionFactoryFactory {

    private static final Log log = LogFactory.getLog(TransactionFactoryFactory.class);

    public static TransactionFactory createTransactionFactory(Compass compass, CompassSettings settings) {
        TransactionFactory transactionFactory = (TransactionFactory) settings.getSettingAsInstance(CompassEnvironment.Transaction.FACTORY, LocalTransactionFactory.class.getName());
        if (log.isDebugEnabled()) {
            log.debug("Using transaction factory [" + transactionFactory + "]");
        }
        transactionFactory.configure(compass, settings);
        return transactionFactory;
    }

    public static LocalTransactionFactory createLocalTransactionFactory(Compass compass, CompassSettings settings) {
        LocalTransactionFactory localTransactionFactory = new LocalTransactionFactory();
        localTransactionFactory.configure(compass, settings);
        return localTransactionFactory;
    }
}