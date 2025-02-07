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

package com.patrykandpatrick.vico.core.common.legend

import android.graphics.RectF
import com.patrykandpatrick.vico.core.common.BoundsAware
import com.patrykandpatrick.vico.core.common.DrawContext
import com.patrykandpatrick.vico.core.common.MeasureContext

/**
 * Defines the functions required by the library to draw a chart legend.
 */
public interface Legend : BoundsAware {
    /**
     * Returns the height of the legend.
     */
    public fun getHeight(
        context: MeasureContext,
        availableWidth: Float,
    ): Float

    /**
     * Draws the legend.
     */
    public fun draw(
        context: DrawContext,
        chartBounds: RectF,
    )
}
