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

package com.patrykandpatrick.vico.compose.cartesian

import android.graphics.RectF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.patrykandpatrick.vico.core.cartesian.ChartValues
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.MutableCartesianMeasureContext

@Composable
internal fun rememberCartesianMeasureContext(
    scrollEnabled: Boolean,
    canvasBounds: RectF,
    horizontalLayout: HorizontalLayout,
    spToPx: (Float) -> Float,
    chartValues: ChartValues,
): MutableCartesianMeasureContext =
    remember {
        MutableCartesianMeasureContext(
            canvasBounds = canvasBounds,
            density = 0f,
            isLtr = true,
            spToPx = spToPx,
            chartValues = chartValues,
        )
    }.apply {
        this.density = LocalDensity.current.density
        this.isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
        this.scrollEnabled = scrollEnabled
        this.horizontalLayout = horizontalLayout
        this.spToPx = spToPx
        this.chartValues = chartValues
    }
