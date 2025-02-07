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
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.extension.getEnd
import com.patrykandpatrick.vico.core.common.extension.getStart
import com.patrykandpatrick.vico.core.common.extension.half
import com.patrykandpatrick.vico.core.common.inBounds
import com.patrykandpatrick.vico.core.common.unaryMinus
import java.text.DecimalFormat

/**
 * A [Decoration] that highlights a _y_ value.
 *
 * @property y returns the _y_ value.
 * @property line the [LineComponent] for the line.
 * @property labelComponent the label [TextComponent].
 * @property label returns the label text.
 * @property horizontalLabelPosition defines the horizontal position of the label.
 * @property verticalLabelPosition defines the vertical position of the label.
 * @property labelRotationDegrees the rotation of the label (in degrees).
 * @property verticalAxisPosition the position of the [VerticalAxis] whose scale the [HorizontalLine] should use when
 * interpreting [y].
 */
public class HorizontalLine(
    private val y: (ExtraStore) -> Float,
    private val line: LineComponent,
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
            val canvasY = bounds.bottom - (y - yRange.minY) / yRange.length * bounds.height()
            line.drawHorizontal(context, bounds.left, bounds.right, canvasY)
            if (labelComponent == null) return
            val clippingFreeVerticalLabelPosition =
                verticalLabelPosition.inBounds(
                    bounds = bounds,
                    distanceFromPoint = line.thicknessDp.half.pixels,
                    componentHeight =
                        labelComponent.getHeight(
                            context = context,
                            text = label,
                            rotationDegrees = labelRotationDegrees,
                        ),
                    y = canvasY,
                )
            labelComponent.drawText(
                context = context,
                text = label,
                textX =
                    when (horizontalLabelPosition) {
                        HorizontalPosition.Start -> bounds.getStart(isLtr)
                        HorizontalPosition.Center -> bounds.centerX()
                        HorizontalPosition.End -> bounds.getEnd(isLtr)
                    },
                textY =
                    when (clippingFreeVerticalLabelPosition) {
                        VerticalPosition.Top -> canvasY - line.thicknessDp.half.pixels
                        VerticalPosition.Center -> canvasY
                        VerticalPosition.Bottom -> canvasY + line.thicknessDp.half.pixels
                    },
                horizontalPosition = -horizontalLabelPosition,
                verticalPosition = clippingFreeVerticalLabelPosition,
                maxTextWidth = bounds.width().toInt(),
                rotationDegrees = labelRotationDegrees,
            )
        }
    }

    /** @suppress */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public companion object {
        private val decimalFormat: DecimalFormat = DecimalFormat("#.##;−#.##")

        public fun getLabel(y: Float): String = decimalFormat.format(y)
    }
}
