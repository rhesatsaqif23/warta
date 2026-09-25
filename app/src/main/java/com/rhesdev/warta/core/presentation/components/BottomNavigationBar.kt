package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.annotation.StringRes
import com.rhesdev.warta.R
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.core.utils.Dimens

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Outlined.Home, R.string.nav_home),
    BottomNavItem("category", Icons.Outlined.GridView, R.string.nav_category),
    BottomNavItem("profile", Icons.Outlined.Person, R.string.nav_profile)
)

// Compact bottom bar with equal click areas.
@Composable
fun WartaBottomNavigationBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        HorizontalDivider(
            thickness = Dimens.defaultStroke,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentRoute == item.route
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = { onItemClick(item.route) })
                        .padding(vertical = Dimens.xxsPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.pillRadius))
                            .then(
                                if (selected) Modifier.background(
                                    MaterialTheme.colorScheme.primaryContainer
                                ) else Modifier
                            )
                            .padding(Dimens.customPaddingLabel)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.labelRes),
                            modifier = Modifier.size(Dimens.defaultIconSize),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WartaBottomNavigationBarPreview() {
    WartaTheme {
        WartaBottomNavigationBar(currentRoute = "home", onItemClick = {})
    }
}
