package io.gmas.fuze;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = InternalApplicationModule.class)
public interface InternalApplicationComponent extends FZApplicationComponent {
}
