package org.compass.core.converter.xsem;

import java.io.Reader;

import org.compass.core.CompassException;
import org.compass.core.config.CompassConfigurable;
import org.compass.core.config.CompassSettings;
import org.compass.core.converter.ConversionException;
import org.compass.core.xml.AliasedXmlObject;
import org.compass.core.xml.XmlObject;

/**
 * An {@link org.compass.core.converter.xsem.XmlContentConverter} implementation that wraps the actual {@link org.compass.core.converter.xsem.XmlContentConverter}
 * configured (based on the settings) and creates and configures a new {@link org.compass.core.converter.xsem.XmlContentConverter}
 * for both {@link #toXml(org.compass.core.xml.XmlObject)} and {@link #fromXml(String, java.io.Reader)}.
 *
 * @author kimchy
 */
public class PrototypeXmlContentConverterWrapper implements XmlContentConverterWrapper, CompassConfigurable {

    private CompassSettings settings;

    public void configure(CompassSettings settings) throws CompassException {
        this.settings = settings;
    }

    /**
     * Converts the {@link org.compass.core.xml.XmlObject} into raw xml by creating and configuring a new
     * {@link org.compass.core.converter.xsem.XmlContentConverter} implementation.
     *
     * @see org.compass.core.converter.xsem.XmlContentConverter#toXml(org.compass.core.xml.XmlObject)
     */
    public String toXml(XmlObject xmlObject) throws ConversionException {
        return createContentConverter().toXml(xmlObject);
    }

    /**
     * Converts a raw xml and an alias into an {@link org.compass.core.xml.AliasedXmlObject} by creating and configuring
     * a new {@link org.compass.core.converter.xsem.XmlContentConverter} implementation.
     *
     * @see org.compass.core.converter.xsem.XmlContentConverter#fromXml(String, java.io.Reader)
     */
    public AliasedXmlObject fromXml(String alias, Reader xml) throws ConversionException {
        return createContentConverter().fromXml(alias, xml);
    }

    public XmlContentConverter createContentConverter() {
        return XmlContentConverterUtils.createXmlContentConverter(settings);
    }

    public String toString() {
        return "Prototype Xml Content Converter wrapping [" + XmlContentConverterUtils.createXmlContentConverter(settings) + "]";
    }
}
