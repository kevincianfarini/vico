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

package com.patrykandpatrick.vico.core.cartesian.decoration

import android.graphics.RectF
import androidx.annotation.RestrictTo
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawContext
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.common.ExtraStore
import com.patrykandpatrick.vico.core.common.HorizontalPosition
import com.patrykandpatrick.vico.core.common.VerticalPosition
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.extension.getEnd
import com.patrykandpatrick.vico.core.common.extension.getStart
import com.patrykandpatrick.vico.core.common.extension.half
import com.patrykandpatrick.vico.core.common.inBounds
import com.patrykandpatrick.vico.core.common.unaryMinus
import java.text.DecimalFormat

/**
 * A [Decoration] that highlights a _y_ range.
 *
 * @property y returns the _y_ range.
 * @property box the box [ShapeComponent].
 * @property labelComponent the label [TextComponent].
 * @property label returns the label text.
 * @property horizontalLabelPosition defines the horizontal position of the label.
 * @property verticalLabelPosition defines the vertical position of the label.
 * @property labelRotationDegrees the rotation of the label (in degrees).
 * @property verticalAxisPosition the position of the [VerticalAxis] whose scale the [HorizontalBox] should use when
 * interpreting [y].
 */
public class HorizontalBox(
    private val y: (ExtraStore) -> ClosedFloatingPointRange<Float>,
    private val box: ShapeComponent,
    private val labelComponent: TextComponent? = null,
    private val label: (ExtraStore) -> CharSequence = { getLabel(y(it)) },
    private val horizontalLabelPosition: HorizontalPosition = HorizontalPosition.Start,
    private val verticalLabelPosition: VerticalPosition = VerticalPosition.Top,
    private val labelRotationDegrees: Float = 0f,
    private val verticalAxisPosition: AxisPosition.Vertical? = null,
) : Decoration {
    override fun onDrawAboveChart(
        context: CartesianDrawContext,
        bounds: RectF,
    ) {
        with(context) {
            val yRange = chartValues.getYRange(verticalAxisPosition)
            val extraStore = chartValues.model.extraStore
            val y = y(extraStore)
            val label = label(extraStore)
            val topY = bounds.bottom - (y.endInclusive - yRange.minY) / yRange.length * bounds.height()
            val bottomY = bounds.bottom - (y.start - yRange.minY) / yRange.length * bounds.height()
            val labelY =
                when (verticalLabelPosition) {
                    VerticalPosition.Top -> topY
                    VerticalPosition.Center -> (topY + bottomY).half
                    VerticalPosition.Bottom -> bottomY
                }
            box.draw(context, bounds.left, topY, bounds.right, bottomY)
            labelComponent?.drawText(
                context = context,
                text = label,
                textX =
                    when (horizontalLabelPosition) {
                        HorizontalPosition.Start -> bounds.getStart(isLtr)
                        HorizontalPosition.Center -> bounds.centerX()
                        HorizontalPosition.End -> bounds.getEnd(isLtr)
                    },
                textY = labelY,
                horizontalPosition = -horizontalLabelPosition,
                verticalPosition =
                    verticalLabelPosition.inBounds(
                        bounds = bounds,
                        componentHeight =
                            labelComponent.getHeight(
                                context = context,
                                text = label,
                                rotationDegrees = labelRotationDegrees,
                            ),
                        y = labelY,
                    ),
                maxTextWidth = bounds.width().toInt(),
                rotationDegrees = labelRotationDegrees,
            )
        }
    }

    /** @suppress */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public companion object {
        private val decimalFormat = DecimalFormat("#.##;−#.##")

        public fun getLabel(y: ClosedFloatingPointRange<Float>): String =
            "${decimalFormat.format(y.start)}–${decimalFormat.format(y.endInclusive)}"
    }
}
