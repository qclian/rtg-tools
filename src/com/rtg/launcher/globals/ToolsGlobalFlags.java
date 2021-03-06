/*
 * Copyright (c) 2016. Real Time Genomics Limited.
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
package com.rtg.launcher.globals;

import java.util.List;

import com.reeltwo.jumble.annotations.JumbleIgnore;
import com.rtg.reference.Ploidy;
import com.rtg.util.cli.Flag;

/**
 * Experimental flags for tools release
 */
@JumbleIgnore
public class ToolsGlobalFlags extends GlobalFlagsInitializer {

  /** Allow SAM file loading when sam header is not coordinate sorted */
  public static final String SAM_IGNORE_SORT_ORDER_FLAG = "com.rtg.sam.ignore-header-sortorder";

  /** Allow definite SDF-ID mismatches to be warning rather than exit */
  public static final String LENIENT_SDF_ID_MISMATCH_FLAG = "com.rtg.sam.lenient-sdf-id";

  /** Level of gzip compression to use. */
  public static final String GZIP_LEVEL = "com.rtg.utils.gzip-level";

  /** When writing VCFs asynchronously, the maximum number of records to buffer (per VCF) */
  public static final String VCF_ASYNC_BUFFER_SIZE = "com.rtg.vcf.async-buffer-size";

  /** When looking at chromosomes declared as polyploid, treat as though they were actually the given ploidy */
  public static final String TREAT_POLYPLOID_AS = "com.rtg.reference.polyploid-as";

  /** Which strand simulated reads are sequenced from: 0 = random, -1 = reverse, 1 = forward */
  public static final String READ_STRAND = "com.rtg.simulation.reads.read-strand";
  /** Supply explicit sequence used for fragment read-through */
  public static final String READ_THROUGH = "com.rtg.simulation.reads.read-through";
  /** If set, assume fragments are from OS-Seq sequencing, with this minimum size (e.g. probe length) */
  public static final String OS_SEQ_FRAGMENTS = "com.rtg.simulation.reads.os-seq-fragments";

  /** Output the best path found along with the haplotypes */
  public static final String VCFEVAL_DUMP_BEST_PATH = "com.rtg.vcf.eval.dump-path";
  /** When comparing consistent paths, whether to maximize included calls, baseline, or sum of both */
  public static final String VCFEVAL_MAXIMIZE_MODE = "com.rtg.vcf.eval.maximize";
  /** Custom variant path result processor */
  public static final String VCFEVAL_PATH_PROCESSOR = "com.rtg.vcf.eval.custom-path-processor";
  /** Custom variant factories */
  public static final String VCFEVAL_VARIANT_FACTORY = "com.rtg.vcf.eval.custom-variant-factory";
  /** Specify the maximum number of simultaneous paths before vcfeval skips a region */
  public static final String VCFEVAL_MAX_PATHS = "com.rtg.vcf.eval.max-paths";
  /** Specify the maximum number of iterations since last sync point before vcfeval skips a region */
  public static final String VCFEVAL_MAX_ITERATIONS = "com.rtg.vcf.eval.max-iterations";
  /** Specify whether to treat the missing side of a half call as an explicit token requiring a match, or just ignore */
  public static final String VCFEVAL_EXPLICIT_HALF_CALL = "com.rtg.vcf.eval.explicit-half-call";
  /** Turn on alternate ROC slope calculation */
  public static final String VCFEVAL_ALT_ROC_SLOPE_CALCULATION = "com.rtg.vcf.eval.alt-roc-slope";

  ToolsGlobalFlags(List<Flag<?>> flags) {
    super(flags);
  }


  @Override
  public void registerFlags() {
    registerFlag(SAM_IGNORE_SORT_ORDER_FLAG);
    registerFlag(LENIENT_SDF_ID_MISMATCH_FLAG, Boolean.class, true);
    registerFlag(GZIP_LEVEL, Integer.class, 2);

    registerFlag(TREAT_POLYPLOID_AS, Ploidy.class, Ploidy.HAPLOID);

    registerFlag(VCF_ASYNC_BUFFER_SIZE, Integer.class, 2000);

    // Simulation
    registerFlag(READ_THROUGH, String.class, "default");
    registerFlag(READ_STRAND, Integer.class, 0);
    registerFlag(OS_SEQ_FRAGMENTS, Integer.class, 0);

    // vcfeval
    registerFlag(VCFEVAL_DUMP_BEST_PATH);
    registerFlag(VCFEVAL_MAXIMIZE_MODE, String.class, "default");
    registerFlag(VCFEVAL_PATH_PROCESSOR, String.class, "");
    registerFlag(VCFEVAL_VARIANT_FACTORY, String.class, "");
    registerFlag(VCFEVAL_MAX_PATHS, Integer.class, 50000);
    registerFlag(VCFEVAL_MAX_ITERATIONS, Integer.class, 10000000);
    registerFlag(VCFEVAL_ALT_ROC_SLOPE_CALCULATION);
    registerFlag(VCFEVAL_EXPLICIT_HALF_CALL, Boolean.class, true);
  }
}
