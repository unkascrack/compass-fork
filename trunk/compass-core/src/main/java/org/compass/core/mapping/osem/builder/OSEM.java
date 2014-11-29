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

package org.compass.core.mapping.osem.builder;

/**
 * Static builder allowing to construct OSEM (Object to Search Engine Mapping)
 * definitions.
 *
 * <p>Here is an exmaple how it can be used:
 *
 * <p><pre>
 * import static org.compass.core.mapping.osem.builder.OSEM.*;
 *
 *
 * conf.addMapping(
 *          searchable(Author.class).alias("author")
 *              .add(id("id"))
 *              .add(property("name").add(metadata("name"))
 *              .add(property("age").add(metadata("age").format("000")))
 * );
 * </pre>
 * @author kimchy
 */
public abstract class OSEM {

    private OSEM() {

    }

    /**
     * Constructs a new searchable class mapping builder.
     */
    public static SearchableMappingBuilder searchable(Class clazz) {
        return new SearchableMappingBuilder(clazz);
    }

    /**
     * Constructs a new searchdble id mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableIdMappingBuilder)
     */
    public static SearchableIdMappingBuilder id(String name) {
        return new SearchableIdMappingBuilder(name);
    }

    /**
     * Constructs a new searchable id component mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableIdComponentMappingBuilder)
     */
    public static SearchableIdComponentMappingBuilder idComponent(String name) {
        return new SearchableIdComponentMappingBuilder(name);
    }

    /**
     * Constructs a new searchable component mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableComponentMappingBuilder)
     */
    public static SearchableComponentMappingBuilder component(String name) {
        return new SearchableComponentMappingBuilder(name);
    }

    /**
     * Constructs a new searchable referennce mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableReferenceMappingBuilder)
     */
    public static SearchableReferenceMappingBuilder reference(String name) {
        return new SearchableReferenceMappingBuilder(name);
    }

    /**
     * Constructs a new searchable constant mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableConstantMappingBuilder)
     */
    public static SearchableConstantMappingBuilder constant(String name) {
        return new SearchableConstantMappingBuilder(name);
    }

    /**
     * Constructs a new searchable dynamic meta data mapping builder based on the name, the
     * converter (jexl, velocity, groovy, ...) and the dynamic expression to use.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableDynamicMetaDataMappingBuilder)
     */
    public static SearchableDynamicMetaDataMappingBuilder dynamicMetadata(String name, String converter, String expression) {
        return new SearchableDynamicMetaDataMappingBuilder(name, converter, expression);
    }

    /**
     * Constructs a new searchable property mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchablePropertyMappingBuilder)
     */
    public static SearchablePropertyMappingBuilder property(String name) {
        return new SearchablePropertyMappingBuilder(name);
    }

    /**
     * Constructs a new searchable dynamic property mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableDynamicPropertyMappingBuilder)
     */
    public static SearchableDynamicPropertyMappingBuilder dynamicProperty(String name) {
        return new SearchableDynamicPropertyMappingBuilder(name);
    }

    /**
     * Constructs a new searchable metadata mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchablePropertyMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableMetaDataMappingBuilder)
     * @see org.compass.core.mapping.osem.builder.SearchableIdMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableMetaDataMappingBuilder)
     */
    public static SearchableMetaDataMappingBuilder metadata(String name) {
        return new SearchableMetaDataMappingBuilder(name);
    }

    /**
     * Constructs a new searchable analyzer property mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableAnalyzerMappingBuilder)
     */
    public static SearchableAnalyzerMappingBuilder analyzer(String name) {
        return new SearchableAnalyzerMappingBuilder(name);
    }

    /**
     * Constructs a new searchable cascade mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableCascadeMappingBuilder)
     */
    public static SearchableCascadeMappingBuilder cascade(String name) {
        return new SearchableCascadeMappingBuilder(name);
    }

    /**
     * Constructs a new searchable parent mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableParentMappingBuilder)
     */
    public static SearchableParentMappingBuilder parent(String name) {
        return new SearchableParentMappingBuilder(name);
    }

    /**
     * Constructs a new searchable boost mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#add(org.compass.core.mapping.osem.builder.SearchableBoostMappingBuilder)
     */
    public static SearchableBoostMappingBuilder boost(String name) {
        return new SearchableBoostMappingBuilder(name);
    }

    /**
     * Constructs a new searchable all mapping builder.
     *
     * @see org.compass.core.mapping.osem.builder.SearchableMappingBuilder#all(org.compass.core.mapping.osem.builder.SearchableAllMappingBuilder)
     */
    public static SearchableAllMappingBuilder all() {
        return new SearchableAllMappingBuilder();
    }
}
