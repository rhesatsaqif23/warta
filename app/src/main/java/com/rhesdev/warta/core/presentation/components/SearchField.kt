package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rhesdev.warta.R
import com.rhesdev.warta.core.presentation.theme.OutlineVariant
import com.rhesdev.warta.core.utils.Dimens

// Read-only search field that navigates to Search screen on tap.
@Composable
fun SearchField(onClick: () -> Unit, modifier: Modifier = Modifier) {
    SearchFieldContainer(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = stringResource(R.string.search_hint),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Default.Search,
            contentDescription = stringResource(R.string.cd_search),
            modifier = Modifier.size(Dimens.defaultIconSize),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Editable search input sharing SearchField styling.
@Composable
fun EditableSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(R.string.search_hint),
    focusRequester: FocusRequester? = null
) {
    SearchFieldContainer(modifier = modifier) {
        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (focusRequester != null) Modifier.focusRequester(focusRequester)
                        else Modifier
                    ),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            )
        }
        Box(
            modifier = Modifier.size(Dimens.xsHeight),
            contentAlignment = Alignment.Center
        ) {
            if (value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.cd_clear),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clickable(onClick = onClearClick)
                        .size(Dimens.defaultIconSize)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.cd_search),
                    modifier = Modifier.size(Dimens.defaultIconSize),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SearchFieldContainer(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(Dimens.pillRadius))
            .border(BorderStroke(Dimens.defaultStroke, OutlineVariant), RoundedCornerShape(Dimens.pillRadius))
            .padding(horizontal = Dimens.smallPadding, vertical = Dimens.xsPadding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchFieldPreview() {
    SearchField(onClick = {})
}

@Preview(showBackground = true)
@Composable
private fun EditableSearchFieldPreview() {
    EditableSearchField(
        value = "",
        onValueChange = {},
        onClearClick = {}
    )
}
