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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberEndAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition.Vertical
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition.Vertical.End
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition.Vertical.Start
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.model.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.model.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.model.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import com.patrykandpatrick.vico.core.common.shape.Shape
import com.patrykandpatrick.vico.sample.showcase.rememberMarker

private val model =
    CartesianChartModel(
        ColumnCartesianLayerModel.build { series(1, 2, 4, 1, 4) },
        LineCartesianLayerModel.build { series(4, 1, 8, 12, 5) },
    )

private val markerMap: Map<Float, CartesianMarker>
    @Composable get() = mapOf(4f to rememberMarker())

@Composable
private fun getColumnLayer(verticalAxisPosition: Vertical? = null) =
    rememberColumnCartesianLayer(
        columnProvider =
            ColumnCartesianLayer.ColumnProvider.series(
                rememberLineComponent(
                    color = Color.Black,
                    thickness = 8.dp,
                    shape = Shape.Pill,
                ),
            ),
        verticalAxisPosition = verticalAxisPosition,
    )

@Composable
private fun getLineLayer(verticalAxisPosition: Vertical? = null) =
    rememberLineCartesianLayer(
        lines =
            listOf(
                rememberLineSpec(
                    shader = DynamicShader.color(Color.DarkGray),
                    backgroundShader =
                        DynamicShader.verticalGradient(
                            arrayOf(Color.DarkGray, Color.DarkGray.copy(alpha = 0f)),
                        ),
                ),
            ),
        verticalAxisPosition = verticalAxisPosition,
    )

private val startAxis: Axis<Start>
    @Composable get() =
        rememberStartAxis(
            label = rememberTextComponent(style = TextStyle.Default, color = Color.Black),
            itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 5 }) },
        )

private val endAxis: Axis<End>
    @Composable get() =
        rememberEndAxis(
            label = rememberTextComponent(style = TextStyle.Default, color = Color.DarkGray),
            itemPlacer = remember { AxisItemPlacer.Vertical.count(count = { 7 }) },
        )

@Composable
@Preview("Chart with independent axes", widthDp = 350)
public fun ChartWithIndependentAxes(modifier: Modifier = Modifier) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                getColumnLayer(Start),
                getLineLayer(End),
                startAxis = startAxis,
                bottomAxis = rememberBottomAxis(),
                endAxis = endAxis,
            ),
        model = model,
        modifier = modifier,
    )
}

@Composable
@Preview("Chart with dependent axes", widthDp = 350)
public fun ChartWithDependentAxes(modifier: Modifier = Modifier) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                getColumnLayer(),
                getLineLayer(),
                startAxis = startAxis,
                bottomAxis = rememberBottomAxis(),
                endAxis = endAxis,
                persistentMarkers = markerMap,
            ),
        model = model,
        modifier = modifier,
    )
}

@Composable
@Preview("Column chart", widthDp = 350)
public fun ColumnChart(modifier: Modifier = Modifier) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                getColumnLayer(),
                startAxis = startAxis,
                bottomAxis = rememberBottomAxis(),
                persistentMarkers = markerMap,
            ),
        model = model,
        modifier = modifier,
    )
}

@Composable
@Preview("Line chart", widthDp = 350)
public fun LineChart(modifier: Modifier = Modifier) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                getLineLayer(),
                startAxis = startAxis,
                bottomAxis = rememberBottomAxis(),
                persistentMarkers = markerMap,
            ),
        model = model,
        modifier = modifier,
    )
}
