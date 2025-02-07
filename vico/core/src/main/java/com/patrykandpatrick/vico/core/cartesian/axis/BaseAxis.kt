/*
 * Copyright 2024 by Patryk Goworowski and Patrick Michalik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("DeprecatedCallableAddReplaceWith")

package com.patrykandpatrick.vico.core.cartesian.axis

import android.graphics.RectF
import androidx.annotation.RestrictTo
import com.patrykandpatrick.vico.core.cartesian.CartesianValueFormatter
import com.patrykandpatrick.vico.core.common.Defaults
import com.patrykandpatrick.vico.core.common.MeasureContext
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.extension.orZero
import com.patrykandpatrick.vico.core.common.extension.setAll

/**
 * A basic implementation of [Axis] used throughout the library.
 *
 * @see Axis
 * @see HorizontalAxis
 * @see VerticalAxis
 */
public abstract class BaseAxis<Position : AxisPosition> : Axis<Position> {
    private val restrictedBounds: MutableList<RectF> = mutableListOf()

    override val bounds: RectF = RectF()

    protected val MeasureContext.axisThickness: Float
        get() = axisLine?.thicknessDp.orZero.pixels

    protected val MeasureContext.tickThickness: Float
        get() = tick?.thicknessDp.orZero.pixels

    protected val MeasureContext.guidelineThickness: Float
        get() = guideline?.thicknessDp.orZero.pixels

    protected val MeasureContext.tickLength: Float
        get() = if (tick != null) tickLengthDp.pixels else 0f

    /**
     * The [TextComponent] to use for labels.
     */
    public var label: TextComponent? = null

    /**
     * The [LineComponent] to use for the axis line.
     */
    public var axisLine: LineComponent? = null

    /**
     * The [LineComponent] to use for ticks.
     */
    public var tick: LineComponent? = null

    /**
     * The [LineComponent] to use for guidelines.
     */
    public var guideline: LineComponent? = null

    /**
     * The tick length (in dp).
     */
    public var tickLengthDp: Float = 0f

    /**
     * Used by [BaseAxis] subclasses for sizing and layout.
     */
    public var sizeConstraint: SizeConstraint = SizeConstraint.Auto()

    /** The [CartesianValueFormatter] for the axis. */
    public var valueFormatter: CartesianValueFormatter = CartesianValueFormatter.decimal()

    /**
     * The rotation of axis labels (in degrees).
     */
    public var labelRotationDegrees: Float = 0f

    /**
     * An optional [TextComponent] to use as the axis title.
     */
    public var titleComponent: TextComponent? = null

    /**
     * The axis title.
     */
    public var title: CharSequence? = null

    override fun setRestrictedBounds(vararg bounds: RectF?) {
        restrictedBounds.setAll(bounds.filterNotNull())
    }

    protected fun isNotInRestrictedBounds(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ): Boolean =
        restrictedBounds.none {
            it.contains(left, top, right, bottom) || it.intersects(left, top, right, bottom)
        }

    /**
     * Used to construct [BaseAxis] instances.
     */
    public open class Builder<Position : AxisPosition>(builder: Builder<Position>? = null) {
        /**
         * The [TextComponent] to use for labels.
         */
        public var label: TextComponent? = builder?.label

        /**
         * The [LineComponent] to use for the axis line.
         */
        public var axis: LineComponent? = builder?.axis

        /**
         * The [LineComponent] to use for axis ticks.
         */
        public var tick: LineComponent? = builder?.tick

        /**
         * The tick length (in dp).
         */
        public var tickLengthDp: Float = builder?.tickLengthDp ?: Defaults.AXIS_TICK_LENGTH

        /**
         * The [LineComponent] to use for guidelines.
         */
        public var guideline: LineComponent? = builder?.guideline

        /** The [CartesianValueFormatter] for the axis. */
        public var valueFormatter: CartesianValueFormatter =
            builder?.valueFormatter ?: CartesianValueFormatter.decimal()

        /**
         * Used by [BaseAxis] subclasses for sizing and layout.
         */
        public var sizeConstraint: SizeConstraint = SizeConstraint.Auto()

        /**
         * An optional [TextComponent] to use as the axis title.
         */
        public var titleComponent: TextComponent? = builder?.titleComponent

        /**
         * The axis title.
         */
        public var title: CharSequence? = builder?.title

        /**
         * The rotation of axis labels (in degrees).
         */
        public var labelRotationDegrees: Float = builder?.labelRotationDegrees ?: 0f
    }

    /**
     * Defines how a [BaseAxis] is to size itself.
     * - For [VerticalAxis], this defines the width.
     * - For [HorizontalAxis], this defines the height.
     *
     * @see [VerticalAxis]
     * @see [HorizontalAxis]
     */
    public sealed class SizeConstraint {
        /**
         * The axis will measure itself and use as much space as it needs, but no less than [minSizeDp], and no more
         * than [maxSizeDp].
         */
        public class Auto(
            public val minSizeDp: Float = 0f,
            public val maxSizeDp: Float = Float.MAX_VALUE,
        ) : SizeConstraint()

        /**
         * The axis size will be exactly [sizeDp].
         */
        public class Exact(public val sizeDp: Float) : SizeConstraint()

        /**
         * The axis will use a fraction of the available space.
         *
         * @property fraction the fraction of the available space that the axis should use.
         */
        public class Fraction(public val fraction: Float) : SizeConstraint() {
            init {
                require(fraction in MIN..MAX) { "Expected a value in the interval [$MIN, $MAX]. Got $fraction." }
            }

            private companion object {
                const val MIN = 0f
                const val MAX = 0.5f
            }
        }

        /**
         * The axis will measure the width of its label component ([label]) for the given [String] ([text]), and it will
         * use this width as its size. In the case of [VerticalAxis], the width of the axis line and the tick length
         * will also be considered.
         */
        public class TextWidth(public val text: String) : SizeConstraint()
    }
}

/** @suppress */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public fun <Position : AxisPosition, A : BaseAxis<Position>> BaseAxis.Builder<Position>.setTo(axis: A): A {
    axis.axisLine = this.axis
    axis.tick = tick
    axis.guideline = guideline
    axis.label = label
    axis.tickLengthDp = tickLengthDp
    axis.valueFormatter = valueFormatter
    axis.sizeConstraint = sizeConstraint
    axis.titleComponent = titleComponent
    axis.title = title
    axis.labelRotationDegrees = labelRotationDegrees
    return axis
}
