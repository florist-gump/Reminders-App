/*
 * Copyright (C) 2011 The Guava Authors
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

package com.google.common.base;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.annotations.GwtCompatible;
import com.google.common.testing.SerializableTester;

import junit.framework.TestCase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tests for {@link Enums}.
 *
 * @author Steve McKay
 */
@GwtCompatible(emulated = true)
public class EnumsTest extends TestCase {

  private enum TestEnum {
    CHEETO,
    HONDA,
    POODLE,
  }

  private enum OtherEnum {}

  public void testGetIfPresent() {
    assertThat(Enums.getIfPresent(TestEnum.class, "CHEETO")).hasValue(TestEnum.CHEETO);
    assertThat(Enums.getIfPresent(TestEnum.class, "HONDA")).hasValue(TestEnum.HONDA);
    assertThat(Enums.getIfPresent(TestEnum.class, "POODLE")).hasValue(TestEnum.POODLE);

    assertThat(Enums.getIfPresent(TestEnum.class, "CHEETO")).isPresent();
    assertThat(Enums.getIfPresent(TestEnum.class, "HONDA")).isPresent();
    assertThat(Enums.getIfPresent(TestEnum.class, "POODLE")).isPresent();

    assertThat(Enums.getIfPresent(TestEnum.class, "CHEETO")).hasValue(TestEnum.CHEETO);
    assertThat(Enums.getIfPresent(TestEnum.class, "HONDA")).hasValue(TestEnum.HONDA);
    assertThat(Enums.getIfPresent(TestEnum.class, "POODLE")).hasValue(TestEnum.POODLE);
  }

  public void testGetIfPresent_caseSensitive() {
    assertThat(Enums.getIfPresent(TestEnum.class, "cHEETO")).isAbsent();
    assertThat(Enums.getIfPresent(TestEnum.class, "Honda")).isAbsent();
    assertThat(Enums.getIfPresent(TestEnum.class, "poodlE")).isAbsent();
  }

  public void testGetIfPresent_whenNoMatchingConstant() {
    assertThat(Enums.getIfPresent(TestEnum.class, "WOMBAT")).isAbsent();
  }

  // Create a second ClassLoader and use it to get a second version of the TestEnum class.
  // Run Enums.getIfPresent on that other TestEnum and then return a WeakReference containing the
  // new ClassLoader. If Enums.getIfPresent does caching that prevents the shadow TestEnum
  // (and therefore its ClassLoader) from being unloaded, then this WeakReference will never be
  // cleared.

  public void testStringConverter_convert() {
    Converter<String, TestEnum> converter = Enums.stringConverter(TestEnum.class);
    assertEquals(TestEnum.CHEETO, converter.convert("CHEETO"));
    assertEquals(TestEnum.HONDA, converter.convert("HONDA"));
    assertEquals(TestEnum.POODLE, converter.convert("POODLE"));
    assertNull(converter.convert(null));
    assertNull(converter.reverse().convert(null));
  }

  public void testStringConverter_convertError() {
    Converter<String, TestEnum> converter = Enums.stringConverter(TestEnum.class);
    try {
      converter.convert("xxx");
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testStringConverter_reverse() {
    Converter<String, TestEnum> converter = Enums.stringConverter(TestEnum.class);
    assertEquals("CHEETO", converter.reverse().convert(TestEnum.CHEETO));
    assertEquals("HONDA", converter.reverse().convert(TestEnum.HONDA));
    assertEquals("POODLE", converter.reverse().convert(TestEnum.POODLE));
  }

  public void testStringConverter_nullConversions() {
    Converter<String, TestEnum> converter = Enums.stringConverter(TestEnum.class);
    assertNull(converter.convert(null));
    assertNull(converter.reverse().convert(null));
  }

  public void testStringConverter_serialization() {
    SerializableTester.reserializeAndAssert(Enums.stringConverter(TestEnum.class));
  }

  @Retention(RetentionPolicy.RUNTIME)
  private @interface ExampleAnnotation {}

  private enum AnEnum {
    @ExampleAnnotation FOO,
    BAR
  }
}

