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

package org.compass.core.mapping.osem;

import org.compass.core.accessor.Getter;
import org.compass.core.accessor.Setter;
import org.compass.core.mapping.Mapping;
import org.compass.core.mapping.ReverseType;
import org.compass.core.mapping.osem.internal.InternalObjectMapping;
import org.compass.core.mapping.support.AbstractResourcePropertyMapping;

/**
 * @author kimchy
 */
public class ClassPropertyMetaDataMapping extends AbstractResourcePropertyMapping implements InternalObjectMapping {

    private String accessor;

    private String propertyName;

    private String definedInAlias;

    private Getter getter;

    private Setter setter;

    public Mapping copy() {
        ClassPropertyMetaDataMapping copy = new ClassPropertyMetaDataMapping();
        super.copy(copy);
        copy.setSetter(getSetter());
        copy.setGetter(getGetter());
        copy.setAccessor(getAccessor());
        copy.setPropertyName(getPropertyName());
        return copy;
    }

    public boolean hasAccessors() {
        return true;
    }

    public boolean canBeCollectionWrapped() {
        return false;
    }

    public Getter getGetter() {
        return getter;
    }

    public void setGetter(Getter getter) {
        this.getter = getter;
    }

    public Setter getSetter() {
        return setter;
    }

    public void setSetter(Setter setter) {
        this.setter = setter;
    }

    /**
     * Returns <code>true</code> if this type of property
     * can act as the Java Bean Property meta-data id.
     */                                                 
    public boolean canActAsPropertyId() {
        return ReverseType.NO == getReverse();
    }

    public String getAccessor() {
        return accessor;
    }

    public void setAccessor(String accessor) {
        this.accessor = accessor;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getDefinedInAlias() {
        return definedInAlias;
    }

    public void setDefinedInAlias(String definedInAlias) {
        this.definedInAlias = definedInAlias;
    }
}
