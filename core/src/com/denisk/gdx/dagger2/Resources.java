package com.denisk.gdx.dagger2;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author denisk
 * @since 11/9/15.
 */
@Component
@Singleton
public interface Resources {
    ResourceProvider resources();
}
