package org.compass.core.xml.javax;

import org.compass.core.xml.AliasedXmlObject;
import org.w3c.dom.Node;

/**
 * A java 5 implementation of {@link org.compass.core.xml.AliasedXmlObject} wrapping a {@link org.w3c.dom.Node}.
 *
 * @author kimchy
 */
public class NodeAliasedXmlObject extends NodeXmlObject implements AliasedXmlObject {

    private String alias;

    public NodeAliasedXmlObject(String alias, Node node) {
        super(node);
        this.alias = alias;
    }

    public String getAlias() {
        return this.alias;
    }
}
