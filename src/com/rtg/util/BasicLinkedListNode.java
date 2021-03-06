/*
 * Copyright (c) 2014. Real Time Genomics Limited.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.rtg.util;

import java.util.Iterator;

/**
 * @param <T> the type of object to collect
 */
public class BasicLinkedListNode<T> implements Iterable<T> {
  final T mValue;
  final BasicLinkedListNode<T> mNext;
  final int mSize;

  /**
   * Create a new pointer to the head of a list
   * @param value the value to store at the head
   * @param tail the rest of the list
   */
  public BasicLinkedListNode(T value, BasicLinkedListNode<T> tail) {
    mNext = tail;
    mValue = value;
    if (tail == null) {
      mSize = 1;
    } else {
      mSize = tail.mSize + 1;
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new BasicLinkedListNodeIterator<>(this);
  }

  /**
   * @return the number of elements in this list
   */
  public int size() {
    return mSize;
  }

  /**
   * @return the value stored in the node
   */
  public T getValue() {
    return mValue;
  }
}
