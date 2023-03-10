package de.haw.vsp.tron.middleware.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.haw.vsp.tron.Enums.TransportType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface AsyncCall {

    TransportType transportType() default TransportType.TCP;
    
}
