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

package com.patrykandpatrick.vico.sample.previews

import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.AxisValueOverrider
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.model.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.model.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.sample.showcase.rememberMarker

private val model =
    CartesianChartModel(
        ColumnCartesianLayerModel.build {
            series(2, -1, -4, 2, 1, -5, -2, -3)
            series(3, -2, 2, -1, 2, -3, -4, -1)
            series(1, -2, 2, 1, -1, 4, 4, -2)
        },
    )

private val columnProvider
    @Composable
    get() =
        ColumnCartesianLayer.ColumnProvider.series(
            rememberLineComponent(color = Color(0xFF494949), thickness = 8.dp),
            rememberLineComponent(color = Color(0xFF7C7A7A), thickness = 8.dp),
            rememberLineComponent(color = Color(0xFFFF5D73), thickness = 8.dp),
        )

@Preview
@Composable
public fun StackedColumnChartWithNegativeValues() {
    val marker = rememberMarker()
    Surface {
        CartesianChartHost(
            modifier = Modifier.height(250.dp),
            chart =
                rememberCartesianChart(
                    rememberColumnCartesianLayer(
                        columnProvider = columnProvider,
                        mergeMode = { ColumnCartesianLayer.MergeMode.Stacked },
                    ),
                    startAxis =
                        rememberStartAxis(
                            itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 8 }) },
                        ),
                    bottomAxis = rememberBottomAxis(),
                    persistentMarkers = mapOf(2f to marker, 3f to marker),
                ),
            model = model,
        )
    }
}

@Preview
@Composable
public fun StackedColumnChartWithNegativeValuesAndDataLabels() {
    Surface {
        CartesianChartHost(
            chart =
                rememberCartesianChart(
                    rememberColumnCartesianLayer(
                        columnProvider = columnProvider,
                        dataLabel = rememberTextComponent(style = TextStyle.Default),
                        mergeMode = { ColumnCartesianLayer.MergeMode.Stacked },
                    ),
                    startAxis =
                        rememberStartAxis(
                            itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 8 }) },
                        ),
                    bottomAxis = rememberBottomAxis(),
                ),
            model = model,
        )
    }
}

@Preview
@Composable
public fun StackedColumnChartWithNegativeValuesAndAxisValuesOverridden() {
    Surface {
        CartesianChartHost(
            chart =
                rememberCartesianChart(
                    rememberColumnCartesianLayer(
                        columnProvider = columnProvider,
                        axisValueOverrider = AxisValueOverrider.fixed(minY = 1f, maxY = 4f),
                        mergeMode = { ColumnCartesianLayer.MergeMode.Stacked },
                    ),
                    startAxis =
                        rememberStartAxis(
                            itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 4 }) },
                        ),
                    bottomAxis = rememberBottomAxis(),
                ),
            model = model,
        )
    }
}

@Preview
@Composable
public fun StackedColumnChartWithNegativeValuesAndAxisValuesOverridden2() {
    Surface {
        CartesianChartHost(
            chart =
                rememberCartesianChart(
                    rememberColumnCartesianLayer(
                        columnProvider = columnProvider,
                        axisValueOverrider = AxisValueOverrider.fixed(minY = -2f, maxY = 0f),
                        mergeMode = { ColumnCartesianLayer.MergeMode.Stacked },
                    ),
                    startAxis =
                        rememberStartAxis(
                            itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 3 }) },
                        ),
                    bottomAxis = rememberBottomAxis(),
                ),
            model = model,
        )
    }
}
