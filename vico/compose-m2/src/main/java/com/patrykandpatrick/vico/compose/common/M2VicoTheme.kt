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

package com.patrykandpatrick.vico.compose.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

/** Creates and remembers a [VicoTheme] based on [MaterialTheme.colors]. */
@Composable
public fun rememberM2VicoTheme(
    candlestickCartesianLayerColors: VicoTheme.CandlestickCartesianLayerColors =
        VicoTheme.CandlestickCartesianLayerColors.fromDefaultColors(getDefaultColors()),
    columnCartesianLayerColors: List<Color> = listOf(MaterialTheme.colors.primary, MaterialTheme.colors.secondary),
    lineCartesianLayerColors: List<Color> = columnCartesianLayerColors,
    elevationOverlayColor: Color = if (isSystemInDarkTheme()) MaterialTheme.colors.onBackground else Color.Transparent,
    lineColor: Color = MaterialTheme.colors.onBackground.copy(alpha = .2f),
    textColor: Color = MaterialTheme.colors.onBackground,
): VicoTheme =
    remember(
        candlestickCartesianLayerColors,
        columnCartesianLayerColors,
        lineCartesianLayerColors,
        elevationOverlayColor,
        lineColor,
        textColor,
    ) {
        VicoTheme(
            candlestickCartesianLayerColors,
            columnCartesianLayerColors,
            lineCartesianLayerColors,
            elevationOverlayColor,
            lineColor,
            textColor,
        )
    }
