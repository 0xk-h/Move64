package com.hunter.move64.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hunter.move64.navigation.AppNav
import com.hunter.move64.ui.theme.Move64Theme

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AppPreview() {
    Move64Theme(darkTheme = true) {
        AppNav()
    }
}