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

package com.patrykandpatrick.vico.compose.common.component

import android.graphics.Typeface
import android.text.Layout
import android.text.TextUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.common.pixelSize
import com.patrykandpatrick.vico.compose.common.shader.toDynamicShader
import com.patrykandpatrick.vico.compose.common.text.toGraphicsTypeFace
import com.patrykandpatrick.vico.core.common.Defaults
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.MutableDimensions
import com.patrykandpatrick.vico.core.common.component.Component
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import com.patrykandpatrick.vico.core.common.shape.Shape

/**
 * Creates and remembers a [LineComponent] with the specified properties.
 */
@Composable
public fun rememberLineComponent(
    color: Color = Color.Black,
    thickness: Dp = Defaults.LINE_COMPONENT_THICKNESS_DP.dp,
    shape: Shape = Shape.Rect,
    dynamicShader: DynamicShader? = null,
    margins: Dimensions = Dimensions.Empty,
    strokeWidth: Dp = 0.dp,
    strokeColor: Color = Color.Transparent,
): LineComponent =
    remember(
        color,
        thickness,
        shape,
        dynamicShader,
        margins,
        strokeWidth,
        strokeColor,
    ) {
        LineComponent(
            color = color.toArgb(),
            thicknessDp = thickness.value,
            shape = shape,
            dynamicShader = dynamicShader,
            margins = margins,
            strokeWidthDp = strokeWidth.value,
            strokeColor = strokeColor.toArgb(),
        )
    }

/**
 * Creates and remembers a [ShapeComponent] with the specified properties.
 */
@Composable
public fun rememberShapeComponent(
    shape: Shape = Shape.Rect,
    color: Color = Color.Black,
    dynamicShader: DynamicShader? = null,
    margins: Dimensions = Dimensions.Empty,
    strokeWidth: Dp = 0.dp,
    strokeColor: Color = Color.Transparent,
): ShapeComponent =
    remember(
        shape,
        color,
        dynamicShader,
        margins,
        strokeWidth,
        strokeColor,
    ) {
        ShapeComponent(
            shape = shape,
            color = color.toArgb(),
            dynamicShader = dynamicShader,
            margins = margins,
            strokeWidthDp = strokeWidth.value,
            strokeColor = strokeColor.toArgb(),
        )
    }

/**
 * Creates and remembers a [ShapeComponent] with the specified properties.
 */
@Composable
public fun rememberShapeComponent(
    shape: Shape = Shape.Rect,
    color: Color = Color.Black,
    brush: Brush,
    margins: Dimensions = Dimensions.Empty,
    strokeWidth: Dp = 0.dp,
    strokeColor: Color = Color.Transparent,
): ShapeComponent =
    rememberShapeComponent(
        shape = shape,
        color = color,
        dynamicShader = brush.toDynamicShader(),
        margins = margins,
        strokeWidth = strokeWidth,
        strokeColor = strokeColor,
    )

/**
 * Creates and remembers a [LayeredComponent].
 */
@Composable
public fun rememberLayeredComponent(
    rear: Component,
    front: Component,
    padding: Dimensions = Dimensions.Empty,
): LayeredComponent =
    remember(rear, front, padding) {
        LayeredComponent(rear, front, padding)
    }

/**
 * Creates and remembers a [TextComponent].
 *
 * @param color the text color.
 * @param textSize the text size.
 * @param background an optional [ShapeComponent] to be displayed behind the text.
 * @param ellipsize the text truncation behavior.
 * @param lineCount the line count.
 * @param padding the padding between the text and the background.
 * @param margins the margins around the background.
 * @param typeface the [Typeface] for the text.
 * @param textAlignment the text alignment.
 * @param minWidth defines the minimum width.
 */
@Deprecated(
    message = """
        Use the Composable function that accepts a Compose UI TextStyle instead of an android graphics Typeface.
    """,
)
@Composable
public fun rememberTextComponent(
    color: Color = Color.Black,
    textSize: TextUnit = Defaults.TEXT_COMPONENT_TEXT_SIZE.sp,
    background: ShapeComponent? = null,
    ellipsize: TextUtils.TruncateAt = TextUtils.TruncateAt.END,
    lineCount: Int = Defaults.LABEL_LINE_COUNT,
    padding: MutableDimensions = MutableDimensions.empty(),
    margins: MutableDimensions = MutableDimensions.empty(),
    typeface: Typeface? = null,
    textAlignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    minWidth: TextComponent.MinWidth = TextComponent.MinWidth.fixed(),
): TextComponent =
    remember(color, textSize, background, ellipsize, lineCount, padding, margins, typeface, textAlignment, minWidth) {
        TextComponent.build {
            this.color = color.toArgb()
            textSizeSp = textSize.pixelSize()
            this.ellipsize = ellipsize
            this.lineCount = lineCount
            this.background = background
            this.padding = padding
            this.margins = margins
            this.typeface = typeface
            this.textAlignment = textAlignment
            this.minWidth = minWidth
        }
    }

/**
 * Create and remember [TextComponent].
 *
 * @param style the [TextStyle] for the text.
 * @param color the [Color] of the text. The default value is [style.color][TextStyle.color] if [Color.isSpecified] is
 * true, otherwise the default is [Color.Black].
 * @param size the text's size. The default value is [style.fontSize][TextStyle.fontSize].
 * @param background an optional [ShapeComponent] to be displayed behind the text.
 * @param ellipsize the text truncation behavior.
 * @param lineCount the line count.
 * @param padding the padding between the text and the background.
 * @param margins the margins around the background.
 * @param textAlignment the text alignment.
 * @param minWidth defines the minimum width.
 */
@Composable
public fun rememberTextComponent(
    style: TextStyle = TextStyle.Default,
    color: Color = if (style.color.isSpecified) style.color else Color.Black,
    size: TextUnit = style.fontSize,
    background: ShapeComponent? = null,
    ellipsize: TextUtils.TruncateAt = TextUtils.TruncateAt.END,
    lineCount: Int = Defaults.LABEL_LINE_COUNT,
    padding: MutableDimensions = MutableDimensions.empty(),
    margins: MutableDimensions = MutableDimensions.empty(),
    textAlignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
    minWidth: TextComponent.MinWidth = TextComponent.MinWidth.fixed(),
): TextComponent {
    val typeface = style.toGraphicsTypeFace()
    return remember(
        style, typeface, color, size,
        background,
        ellipsize,
        lineCount,
        padding,
        margins,
        typeface,
        textAlignment,
        minWidth,
    ) {
        TextComponent.build {
            this.color = color.toArgb()
            this.textSizeSp = size.pixelSize()
            this.ellipsize = ellipsize
            this.lineCount = lineCount
            this.background = background
            this.padding = padding
            this.margins = margins
            this.typeface = typeface
            this.textAlignment = textAlignment
            this.minWidth = minWidth
        }
    }
}

/** A [Dp] version of [TextComponent.MinWidth.fixed]. */
@Stable
public fun TextComponent.MinWidth.Companion.fixed(value: Dp = 0.dp): TextComponent.MinWidth = fixed(value.value)
