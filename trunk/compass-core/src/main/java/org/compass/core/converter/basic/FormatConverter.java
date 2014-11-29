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

package org.compass.core.converter.basic;

import org.compass.core.converter.Converter;
import org.compass.core.converter.mapping.ResourcePropertyConverter;

/**
 * A {@link org.compass.core.converter.Converter} that can handle format parameter.
 *
 * @author kimchy
 */
public interface FormatConverter<T> extends ResourcePropertyConverter<T> {

    /**
     * Sets the format for the {@link org.compass.core.converter.Converter}.
     */
    void setFormat(String format);

    /**
     * Returns a copy of the format converter.
     */
    FormatConverter copy();
}