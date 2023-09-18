// Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
// or more contributor license agreements. Licensed under the Elastic License
// 2.0; you may not use this file except in compliance with the Elastic License
// 2.0.
package org.elasticsearch.xpack.esql.expression.function.scalar.multivalue;

import java.lang.Override;
import java.lang.String;
import org.elasticsearch.compute.data.Block;
import org.elasticsearch.compute.data.DoubleArrayVector;
import org.elasticsearch.compute.data.DoubleBlock;
import org.elasticsearch.compute.data.LongBlock;
import org.elasticsearch.compute.data.Vector;
import org.elasticsearch.compute.operator.DriverContext;
import org.elasticsearch.compute.operator.EvalOperator;
import org.elasticsearch.search.aggregations.metrics.CompensatedSum;

/**
 * {@link EvalOperator.ExpressionEvaluator} implementation for {@link MvAvg}.
 * This class is generated. Do not edit it.
 */
public final class MvAvgLongEvaluator extends AbstractMultivalueFunction.AbstractEvaluator {
  private final DriverContext driverContext;

  public MvAvgLongEvaluator(EvalOperator.ExpressionEvaluator field, DriverContext driverContext) {
    super(field);
    this.driverContext = driverContext;
  }

  @Override
  public String name() {
    return "MvAvg";
  }

  /**
   * Evaluate blocks containing at least one multivalued field.
   */
  @Override
  public Block evalNullable(Block fieldVal) {
    LongBlock v = (LongBlock) fieldVal;
    int positionCount = v.getPositionCount();
    DoubleBlock.Builder builder = DoubleBlock.newBlockBuilder(positionCount);
    CompensatedSum work = new CompensatedSum();
    for (int p = 0; p < positionCount; p++) {
      int valueCount = v.getValueCount(p);
      if (valueCount == 0) {
        builder.appendNull();
        continue;
      }
      int first = v.getFirstValueIndex(p);
      if (valueCount == 1) {
        long value = v.getLong(first);
        double result = MvAvg.single(value);
        builder.appendDouble(result);
        continue;
      }
      int end = first + valueCount;
      for (int i = first; i < end; i++) {
        long value = v.getLong(i);
        MvAvg.process(work, value);
      }
      double result = MvAvg.finish(work, valueCount);
      builder.appendDouble(result);
    }
    return builder.build();
  }

  /**
   * Evaluate blocks containing at least one multivalued field.
   */
  @Override
  public Vector evalNotNullable(Block fieldVal) {
    LongBlock v = (LongBlock) fieldVal;
    int positionCount = v.getPositionCount();
    double[] values = new double[positionCount];
    CompensatedSum work = new CompensatedSum();
    for (int p = 0; p < positionCount; p++) {
      int valueCount = v.getValueCount(p);
      int first = v.getFirstValueIndex(p);
      if (valueCount == 1) {
        long value = v.getLong(first);
        double result = MvAvg.single(value);
        values[p] = result;
        continue;
      }
      int end = first + valueCount;
      for (int i = first; i < end; i++) {
        long value = v.getLong(i);
        MvAvg.process(work, value);
      }
      double result = MvAvg.finish(work, valueCount);
      values[p] = result;
    }
    return new DoubleArrayVector(values, positionCount);
  }

  /**
   * Evaluate blocks containing only single valued fields.
   */
  @Override
  public Block evalSingleValuedNullable(Block fieldVal) {
    LongBlock v = (LongBlock) fieldVal;
    int positionCount = v.getPositionCount();
    DoubleBlock.Builder builder = DoubleBlock.newBlockBuilder(positionCount);
    CompensatedSum work = new CompensatedSum();
    for (int p = 0; p < positionCount; p++) {
      int valueCount = v.getValueCount(p);
      if (valueCount == 0) {
        builder.appendNull();
        continue;
      }
      assert valueCount == 1;
      int first = v.getFirstValueIndex(p);
      long value = v.getLong(first);
      double result = MvAvg.single(value);
      builder.appendDouble(result);
    }
    return builder.build();
  }

  /**
   * Evaluate blocks containing only single valued fields.
   */
  @Override
  public Vector evalSingleValuedNotNullable(Block fieldVal) {
    LongBlock v = (LongBlock) fieldVal;
    int positionCount = v.getPositionCount();
    double[] values = new double[positionCount];
    CompensatedSum work = new CompensatedSum();
    for (int p = 0; p < positionCount; p++) {
      int valueCount = v.getValueCount(p);
      assert valueCount == 1;
      int first = v.getFirstValueIndex(p);
      long value = v.getLong(first);
      double result = MvAvg.single(value);
      values[p] = result;
    }
    return new DoubleArrayVector(values, positionCount);
  }
}
