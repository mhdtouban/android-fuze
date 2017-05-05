package io.gmas.fuze.commons.qualifiers;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.gmas.fuze.commons.FragmentViewModel;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresFragmentViewModel {
    Class<? extends FragmentViewModel> value();
}
