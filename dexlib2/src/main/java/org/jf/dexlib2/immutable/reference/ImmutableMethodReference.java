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

package org.jf.dexlib2.immutable.reference;

import com.google.common.collect.ImmutableList;
import org.jf.dexlib2.base.reference.BaseMethodReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.util.ImmutableUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ImmutableMethodReference extends BaseMethodReference implements ImmutableReference {
    @Nonnull public final String containingClass;
    @Nonnull public final String name;
    @Nonnull public final ImmutableList<? extends ImmutableTypeReference> parameters;
    @Nonnull public final String returnType;

    public ImmutableMethodReference(@Nonnull String containingClass,
                                    @Nonnull String name,
                                    @Nullable List<? extends TypeReference> parameters,
                                    @Nonnull String returnType) {
        this.containingClass = containingClass;
        this.name = name;
        this.parameters = ImmutableTypeReference.immutableListOf(parameters);
        this.returnType = returnType;
    }

    public ImmutableMethodReference(@Nonnull String containingClass,
                                    @Nonnull String name,
                                    @Nullable ImmutableList<? extends ImmutableTypeReference> parameters,
                                    @Nonnull String returnType) {
        this.containingClass = containingClass;
        this.name = name;
        this.parameters = ImmutableUtils.nullToEmptyList(parameters);
        this.returnType = returnType;
    }

    @Nonnull
    public static ImmutableMethodReference of(@Nonnull MethodReference methodReference) {
        if (methodReference instanceof ImmutableMethodReference) {
            return (ImmutableMethodReference)methodReference;
        }
        return new ImmutableMethodReference(
                methodReference.getContainingClass(),
                methodReference.getName(),
                ImmutableList.copyOf(methodReference.getParameters()),
                methodReference.getReturnType());
    }

    @Nonnull @Override public String getContainingClass() { return containingClass; }
    @Nonnull @Override public String getName() { return name; }
    @Nonnull @Override public List<? extends ImmutableTypeReference> getParameters() { return parameters; }
    @Nonnull @Override public String getReturnType() { return returnType; }
}
