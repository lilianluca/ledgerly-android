package com.lilianluca.ledgerly.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

/** Adds room below the last list item, so a floating action button doesn't cover it. */
@Composable
fun PaddingValues.withFabClearance(): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(layoutDirection),
        top = calculateTopPadding(),
        end = calculateEndPadding(layoutDirection),
        // FAB height (56 dp) + its margin (16 dp) + a small gap
        bottom = calculateBottomPadding() + 88.dp,
    )
}
