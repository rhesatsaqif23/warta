package com.rhesdev.warta.feature.news.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rhesdev.warta.R
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.feature.news.presentation.components.SectionHeader

// Weekly coverage strip from the stats by_day facet.
@Composable
fun TrendStrip(
    byDay: Map<String, Int>,
    selectedDay: String?,
    onDaySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = remember(byDay) { byDay.toSortedMap().entries.toList().takeLast(7) }
    if (days.isEmpty()) return
    val max = maxOf(days.maxOf { it.value }, 1)
    val selectedColor = MaterialTheme.colorScheme.primary
    val barColor = MaterialTheme.colorScheme.primaryContainer

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
                .padding(16.dp)
        ) {
            SectionHeader(stringResource(R.string.section_this_week))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            days.forEach { (day, count) ->
                val selected = day == selectedDay
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onDaySelected(if (selected) null else day) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        val barHeight =
                            (size.height - 4.dp.toPx()) * (count.toFloat() / max) + 2.dp.toPx()
                        drawRoundRect(
                            color = if (selected) selectedColor else barColor,
                            topLeft = Offset(0f, size.height - barHeight),
                            size = Size(size.width, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )
                    }
                    Text(
                        text = day.takeLast(2),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrendStripPreview() {
    WartaTheme {
        TrendStrip(
            byDay = mapOf(
                "2026-09-18" to 120,
                "2026-09-19" to 200,
                "2026-09-20" to 90,
                "2026-09-21" to 310,
                "2026-09-22" to 260,
                "2026-09-23" to 180,
                "2026-09-24" to 140
            ),
            selectedDay = null,
            onDaySelected = {}
        )
    }
}
