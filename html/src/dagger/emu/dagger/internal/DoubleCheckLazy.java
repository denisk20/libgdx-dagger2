/*
 * Copyright (C) 2014 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dagger.internal;

import javax.inject.Provider;

import dagger.Lazy;

/**
 * A basic {@link Lazy} implementation that memoizes the value returned from a {@link Provider}
 * using the double-check idiom described in Effective Java 2: Item 71.
 *
 * @author Gregory Kick
 * @since 2.0
 */
// TODO(gak): Unify the duplicated code between this and ScopedProvider.
public final class DoubleCheckLazy<T> implements Lazy<T> {
  private static final Object UNINITIALIZED = new Object();

  private final Provider<T> provider;
  private volatile Object instance = UNINITIALIZED;

  private DoubleCheckLazy(Provider<T> provider) {
    assert provider != null;
    this.provider = provider;
  }

  @SuppressWarnings("unchecked") // cast only happens when result comes from the factory
  @Override
  public T get() {
    // to suppress it.
    Object result = instance;
    if (result == UNINITIALIZED) {
      synchronized (this) {
        result = instance;
        if (result == UNINITIALIZED) {
          instance = result = provider.get();
        }
      }
    }
    return (T) result;
  }

  public static <T> Lazy<T> create(Provider<T> provider) {
    if (provider == null) {
      throw new NullPointerException();
    }
    return new DoubleCheckLazy<T>(provider);
  }
}
