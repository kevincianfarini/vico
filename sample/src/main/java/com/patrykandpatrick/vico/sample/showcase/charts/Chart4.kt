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

package com.patrykandpatrick.vico.sample.showcase.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberEndAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberTopAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.compose.common.shape.rounded
import com.patrykandpatrick.vico.core.cartesian.DefaultPointConnector
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.model.columnSeries
import com.patrykandpatrick.vico.core.cartesian.model.lineSeries
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import com.patrykandpatrick.vico.core.common.shape.Shape
import com.patrykandpatrick.vico.databinding.Chart4Binding
import com.patrykandpatrick.vico.sample.showcase.Defaults
import com.patrykandpatrick.vico.sample.showcase.UISystem
import com.patrykandpatrick.vico.sample.showcase.rememberMarker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.random.Random

@Composable
internal fun Chart4(
    uiSystem: UISystem,
    modifier: Modifier,
) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            while (isActive) {
                modelProducer.tryRunTransaction {
                    columnSeries {
                        repeat(3) {
                            series(
                                List(Defaults.ENTRY_COUNT) {
                                    Defaults.COLUMN_LAYER_MIN_Y +
                                        Random.nextFloat() * Defaults.COLUMN_LAYER_RELATIVE_MAX_Y
                                },
                            )
                        }
                    }
                    lineSeries { series(List(Defaults.ENTRY_COUNT) { Random.nextFloat() * Defaults.MAX_Y }) }
                }
                delay(Defaults.TRANSACTION_INTERVAL_MS)
            }
        }
    }
    when (uiSystem) {
        UISystem.Compose -> ComposeChart4(modelProducer, modifier)
        UISystem.Views -> ViewChart4(modelProducer, modifier)
    }
}

@Composable
private fun ComposeChart4(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier,
) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider =
                        ColumnCartesianLayer.ColumnProvider.series(
                            columnColors.map { color ->
                                rememberLineComponent(
                                    color = color,
                                    thickness = 8.dp,
                                    shape = Shape.rounded(2.dp),
                                )
                            },
                        ),
                ),
                rememberLineCartesianLayer(
                    lines =
                        listOf(
                            rememberLineSpec(
                                shader = DynamicShader.color(lineColor),
                                pointConnector = DefaultPointConnector(cubicStrength = 0f),
                            ),
                        ),
                ),
                topAxis = rememberTopAxis(),
                endAxis = rememberEndAxis(),
            ),
        modelProducer = modelProducer,
        modifier = modifier,
        marker = rememberMarker(),
        runInitialAnimation = false,
        zoomState = rememberVicoZoomState(zoomEnabled = false),
    )
}

@Composable
private fun ViewChart4(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier,
) {
    val marker = rememberMarker()
    AndroidViewBinding(Chart4Binding::inflate, modifier) {
        with(chartView) {
            runInitialAnimation = false
            this.modelProducer = modelProducer
            this.marker = marker
        }
    }
}

private val columnColors = listOf(Color(0xff916cda), Color(0xffd877d8), Color(0xfff094bb))
private val lineColor = Color(0xfffdc8c4)
