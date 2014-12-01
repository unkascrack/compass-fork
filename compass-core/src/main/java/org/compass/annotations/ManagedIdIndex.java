package org.compass.annotations;

/**
 * Controls the index (see {@link org.compass.annotations.Index}) of the internal managed id generated
 * (see {@link org.compass.annotations.ManagedId}). Note, that this is an advance mapping setting, which
 * usually should not be set.
 *
 * @author kimchy
 * @see org.compass.annotations.ManagedId
 * @see org.compass.annotations.Index
 */
public enum ManagedIdIndex {
    /**
     * Lets Compass control it. Uses compass deafult, or the external setting for it.
     */
    NA,

    /**
     * Do not index the property value. This property can thus not be searched, but one
     * can still access its contents provided it is {@link org.compass.annotations.Store stored}.
     */

    NO,

    /**
     * Index the property's value without using an Analyzer, so it can be searched.
     * As no analyzer is used the value will be stored as a single term. This is
     * useful for unique Ids like product numbers.
     */
    NOT_ANALYZED,

    /**
     * @deprecated use {@link #NOT_ANALYZED} instead.
     */
    UN_TOKENIZED
}
