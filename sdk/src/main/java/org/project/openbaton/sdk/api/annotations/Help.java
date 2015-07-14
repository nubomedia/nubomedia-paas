package org.project.openbaton.sdk.api.annotations;

import java.lang.annotation.*;

/**
 * Created by lto on 14/07/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Help {
    String help() default "no help for this";
}
