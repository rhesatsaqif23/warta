package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhesdev.warta.core.presentation.theme.Outline
import com.rhesdev.warta.core.presentation.theme.PrimaryGradient

/** Category chip with gradient-selected / outlined-unselected states. */
@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
        modifier = modifier
            .clip(shape)
            .then(
                if (selected) Modifier.background(PrimaryGradient)
                else Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .border(BorderStroke(1.dp, Outline), shape)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryChipPreview() {
    CategoryChip(label = "Nasional", selected = true, onClick = {})
}
