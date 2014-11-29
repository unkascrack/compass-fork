package org.compass.core.converter.dynamic;

import org.compass.core.converter.ConversionException;

/**
 * A factory for {@link org.compass.core.converter.dynamic.ExpressionEvaluator}.
 *
 * @author kimchy
 */
public interface ExpressionEvaluatorFactory {

    /**
     * Creates a new {@link org.compass.core.converter.dynamic.ExpressionEvaluator}.
     *
     * @return A new expression evaluator
     * @throws org.compass.core.converter.ConversionException
     */
    ExpressionEvaluator create() throws ConversionException;
}
