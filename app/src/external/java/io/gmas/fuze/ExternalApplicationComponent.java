package io.gmas.fuze;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ExternalApplicationModule.class)
public interface ExternalApplicationComponent extends FZApplicationComponent {
}
