package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import java.util.Random;

import it.unisa.dia.gas.jpbc.Field;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class DegreeTwoExtensionQuadraticField<F extends Field> extends QuadraticField<F, DegreeTwoExtensionQuadraticElement> {

    public DegreeTwoExtensionQuadraticField(Random random, F targetField) {
        super(random, targetField);
    }


    public DegreeTwoExtensionQuadraticElement newElement() {
        return new DegreeTwoExtensionQuadraticElement(this);
    }

}
