/*
 * Copyright 2012, Google Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib2.immutable.sorted;

import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multiset;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.sorted.SortedAnnotationElement;
import org.jf.dexlib2.iface.sorted.value.SortedEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.sorted.value.SortedImmutableEncodedValue;
import org.jf.dexlib2.immutable.sorted.value.SortedImmutableEncodedValueFactory;
import org.jf.util.ImmutableSortedSetConverter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Comparator;

public class SortedImmutableAnnotationElement extends BaseAnnotationElement implements SortedAnnotationElement {
    @Nonnull public final String name;
    @Nonnull public final SortedImmutableEncodedValue value;

    public SortedImmutableAnnotationElement(@Nonnull String name,
                                            @Nonnull EncodedValue value) {
        this.name = name;
        this.value = SortedImmutableEncodedValueFactory.of(value);
    }

    public SortedImmutableAnnotationElement(@Nonnull String name,
                                            @Nonnull SortedImmutableEncodedValue value) {
        this.name = name;
        this.value = value;
    }

    public static SortedImmutableAnnotationElement of(AnnotationElement annotationElement) {
        if (annotationElement instanceof SortedImmutableAnnotationElement) {
            return (SortedImmutableAnnotationElement)annotationElement;
        }
        return new SortedImmutableAnnotationElement(
                annotationElement.getName(),
                annotationElement.getValue());
    }

    @Nonnull @Override public String getName() { return name; }
    @Nonnull @Override public SortedEncodedValue getValue() { return value; }

    public static final Comparator<AnnotationElement> COMPARE_BY_NAME = new Comparator<AnnotationElement>() {
        @Override
        public int compare(@Nonnull AnnotationElement element1, @Nonnull AnnotationElement element2) {
            return element1.getName().compareTo(element2.getName());
        }
    };

    @Nonnull
    public static ImmutableSortedSet<SortedImmutableAnnotationElement> immutableSortedSetOf(
            @Nullable Collection<? extends AnnotationElement> list) {
        ImmutableSortedSet<SortedImmutableAnnotationElement> set =  CONVERTER.convert(COMPARE_BY_NAME, list);
        if (list != null && set.size() < list.size()) {
            // There were duplicate annotations. Let's find them and print a warning.
            ImmutableSortedMultiset<AnnotationElement> multiset = ImmutableSortedMultiset.copyOf(COMPARE_BY_NAME, list);
            for (Multiset.Entry<AnnotationElement> entry: multiset.entrySet()) {
                AnnotationElement annotationElement = entry.getElement();
                // TODO: need to provide better context
                System.err.println(String.format("Ignoring duplicate annotation value for name: %s",
                        annotationElement.getName()));
            }
        }
        return set;
    }

    private static final ImmutableSortedSetConverter<SortedImmutableAnnotationElement, AnnotationElement> CONVERTER =
            new ImmutableSortedSetConverter<SortedImmutableAnnotationElement, AnnotationElement>() {
                @Override
                protected boolean isImmutable(@Nonnull AnnotationElement item) {
                    return item instanceof SortedImmutableAnnotationElement;
                }

                @Nonnull
                @Override
                protected SortedImmutableAnnotationElement makeImmutable(@Nonnull AnnotationElement item) {
                    return SortedImmutableAnnotationElement.of(item);
                }
            };
}
