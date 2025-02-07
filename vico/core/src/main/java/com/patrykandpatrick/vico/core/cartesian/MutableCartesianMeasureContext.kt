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

package com.patrykandpatrick.vico.core.cartesian

import android.graphics.RectF
import com.patrykandpatrick.vico.core.common.MutableMeasureContext

/**
 * A [CartesianMeasureContext] implementation that facilitates the mutation of some of its properties.
 */
public class MutableCartesianMeasureContext(
    override val canvasBounds: RectF,
    override var density: Float,
    override var isLtr: Boolean,
    override var scrollEnabled: Boolean = false,
    override var horizontalLayout: HorizontalLayout = HorizontalLayout.Segmented,
    override var chartValues: ChartValues,
    spToPx: (Float) -> Float,
) : MutableMeasureContext(
        canvasBounds = canvasBounds,
        density = density,
        isLtr = isLtr,
        spToPx = spToPx,
    ),
    CartesianMeasureContext {
    override fun spToPx(sp: Float): Float = spToPx.invoke(sp)
}
