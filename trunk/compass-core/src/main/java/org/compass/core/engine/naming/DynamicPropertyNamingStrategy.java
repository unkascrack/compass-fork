package org.compass.core.engine.naming;

/**
 * A naming strategy that uses {@link org.compass.core.engine.naming.DynamicPropertyPath} when building
 * {@link org.compass.core.engine.naming.PropertyPath}.
 *
 * @author kimchy
 * @author lexi
 * @see org.compass.core.engine.naming.PropertyPath
 * @see org.compass.core.engine.naming.DynamicPropertyPath
 * @see org.compass.core.engine.naming.PropertyNamingStrategyFactory
 * @see org.compass.core.engine.naming.DefaultPropertyNamingStrategyFactory
 */
public class DynamicPropertyNamingStrategy implements PropertyNamingStrategy {

    public boolean isInternal(String name) {
        return name.charAt(0) == '$';
    }

    public PropertyPath getRootPath() {
        return new StaticPropertyPath("$");
    }

    public PropertyPath buildPath(PropertyPath root, String name) {
        return new DynamicPropertyPath(root, name);
    }
}
