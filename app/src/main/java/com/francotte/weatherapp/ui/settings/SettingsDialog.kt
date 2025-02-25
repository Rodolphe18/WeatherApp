package com.francotte.weatherapp.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.francotte.weatherapp.R


@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.feature_settings_title),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                SettingsDialogSectionTitle(text = "Effacer toutes mes villes", onClick)
                HorizontalDivider()
                LinksPanel()
            }

        },
        confirmButton = {
            Text(
                text = stringResource(R.string.feature_settings_dismiss_dialog_button_text),
                style = MaterialTheme.typography.labelLarge,
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LinksPanel() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        WeatherAppTextButton(
            onClick = { },
        ) {
            Text(text = stringResource(R.string.feature_settings_privacy_policy))
        }
        WeatherAppTextButton(
            onClick = {},
        ) {
            Text(text = stringResource(R.string.feature_settings_licenses))
        }
        WeatherAppTextButton(
            onClick = { },
        ) {
            Text(text = stringResource(R.string.feature_settings_brand_guidelines))
        }
        WeatherAppTextButton(
            onClick = { },
        ) {
            Text(text = stringResource(R.string.feature_settings_feedback))
        }
    }
}

@Composable
fun WeatherAppTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        content = content,
    )
}

@Composable
private fun SettingsDialogSectionTitle(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        color = Color.Red,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .padding(top = 8.dp, bottom = 16.dp)
            .clickable { onClick() },
    )
}