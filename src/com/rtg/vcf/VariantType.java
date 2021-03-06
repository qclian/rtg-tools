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
package com.rtg.vcf;

import com.reeltwo.jumble.annotations.TestClass;

/**
 * Enumeration of Variant types
 */
@TestClass("com.rtg.vcf.VariantStatisticsTest")
public enum VariantType {
  //Ordered least to most precedence

  /** Missing / No call */
  NO_CALL(false, false, true),
  /** Equal to reference */
  UNCHANGED(false, false, true),
  /** Single nucleotide polymorphism */
  SNP(false, false, false),
  /** Multiple nucleotide polymorphism */
  MNP(false, false, false),
  /** Deletion with respect to reference */
  DELETION(true, false, false),
  /** Insertion with respect to reference */
  INSERTION(true, false, false),
  /** Complex indel with deletions and insertions */
  INDEL(true, false, false),
  /** Structural variant break end */
  SV_BREAKEND(false, true, false),
  /** Structural variant symbolic value */
  SV_SYMBOLIC(false, true, false),
  /** Structural variant missing allele */
  SV_MISSING(false, true, false);

  private final boolean mIsIndelType;
  private final boolean mIsSvType;
  private final boolean mIsNonVariant;

  VariantType(boolean isIndelType, boolean isSvType, boolean isNonVariant) {
    mIsIndelType = isIndelType;
    mIsSvType = isSvType;
    mIsNonVariant = isNonVariant;
  }

  public boolean isNonVariant() {
    return mIsNonVariant;
  }

  public boolean isIndelType() {
    return mIsIndelType;
  }

  public boolean isSvType() {
    return mIsSvType;
  }

  /**
   * Return a single VariantType most indicative of a pair of VariantTypes, to be used to assign a variant
   * type to a polyploid genotype or a variant record as a whole. This generally takes the more "complex"
   * of the input types.
   * @param a a variant type
   * @param b another variant type
   * @return the resolved variant type.
   */
  public static VariantType getPrecedence(VariantType a, VariantType b) {
    if (a != b && a.isIndelType() && b.isIndelType()) {
      // If both variants are not the same and both within the three Indel categories default to INDEL
      return INDEL;
    }
    if (a.ordinal() > b.ordinal()) {
      return a;
    }
    return b;
  }

  /**
   * If the allele is a symbolic structural variant, return the appropriate VariantType
   * @param allele the alt allele from VCF.
   * @return <code>VariantType.SV_SYMBOLIC</code> or <code>VariantType.SV_BREAKEND</code>, or null if not a symbolic variant
   */
  public static VariantType getSymbolicAlleleType(String allele) {
    if (allele.length() > 0) {
      if (allele.charAt(0) == '<') {
        return VariantType.SV_SYMBOLIC;
      } else if (allele.charAt(0) == VcfUtils.ALT_SPANNING_DELETION && allele.length() == 1) {
        return VariantType.SV_MISSING;
      } else {
        for (int i = 0; i < allele.length(); ++i) {
          final char c = allele.charAt(i);
          if ((c == '[') || (c == ']')) {
            return VariantType.SV_BREAKEND;
          }
        }
      }
    }
    return null;
  }

  /**
   * Determine the type of variant given a genotype
   * @param rec the VCF record containing the allele definitions
   * @param gtSplit the genotype, as allele indices
   * @return the type of variant
   */
  public static VariantType getType(VcfRecord rec, int[] gtSplit) {
    boolean allMissing = true;
    int altId = 0;
    for (int g : gtSplit) {
      if (g != -1) {
        allMissing = false;
        if (g != 0) {
          altId = g;
          break;
        }
      }
    }
    if (allMissing) {
      return VariantType.NO_CALL;
    }
    if (altId == 0) {
      return VariantType.UNCHANGED;
    }
    final String[] alleles = VcfUtils.getAlleleStrings(rec);
    VariantType altType = getType(alleles[0], alleles[altId]);
    for (int i = altId + 1; i < gtSplit.length; i++) {
      if (gtSplit[i] > 0 && gtSplit[i] != altId) {
        altType = getPrecedence(altType, getType(alleles[0], alleles[i]));
      }
    }
    return altType;
  }

  /**
   * Determine the type of variant given a reference allele and a called/alternative allele
   * @param refAllele the reference allele
   * @param altAllele the alternative allele
   * @return the type of variant
   */
  public static VariantType getType(String refAllele, String altAllele) {
    if (refAllele.equals(altAllele)) {
      return VariantType.UNCHANGED;
    } else if (refAllele.length() == 1 && altAllele.length() == 1 && altAllele.charAt(0) != VcfUtils.ALT_SPANNING_DELETION) {
      return VariantType.SNP;
    }
    final VariantType svType = getSymbolicAlleleType(altAllele);
    if (svType != null) {
      return svType;
    }
    if (refAllele.length() == altAllele.length()) {
      return VariantType.MNP;
    } else if (isInsertionOrDeletion(refAllele, altAllele)) {
      if (refAllele.length() < altAllele.length()) {
        return VariantType.INSERTION;
      } else {
        return VariantType.DELETION;
      }
    } else {
      return VariantType.INDEL;
    }
  }

  private static boolean isInsertionOrDeletion(String ref, String pred) {
    //Only call if have already ruled out Unchanged, SNP and MNP
    if (ref.length() == 0 || pred.length() == 0) {
      return true;
    }
    final boolean isInsert = ref.length() < pred.length();
    final String seq1 = isInsert ? ref : pred;
    final String seq2 = isInsert ? pred : ref;
    final int seq1Len = seq1.length();
    final int seq2Len = seq2.length();
    int left;
    for (left = 0; left < seq1Len; ++left) {
      if (seq1.charAt(left) != seq2.charAt(left)) {
        break;
      }
    }
    if (left == seq1Len) {
      return true;
    }
    int right;
    for (right = 0; right < seq1Len; ++right) {
      if (seq1.charAt(seq1Len - right - 1) != seq2.charAt(seq2Len - right - 1)) {
        break;
      }
    }
    return left + right >= seq1Len;
  }
}
