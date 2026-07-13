package com.example.weatherapp.ui.search_city

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.data.datastore.SavedCity
import com.example.weatherapp.ui.composable.ErrorScreen
import com.example.weatherapp.ui.composable.LoadingScreen
import com.example.weatherapp.ui.composable.SearchAutoComplete
import com.example.weatherapp.ui.theme.MoonTint
import com.example.weatherapp.ui.theme.Poppins
import com.example.weatherapp.ui.theme.RainTint
import com.example.weatherapp.ui.theme.SunTint
import com.example.weatherapp.ui.theme.glassCard
import com.example.weatherapp.ui.theme.inkColor
import com.example.weatherapp.ui.theme.mutedColor
import com.example.weatherapp.ui.theme.skyBrush
import kotlinx.coroutines.launch


@Composable
fun SearchCityScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchCityViewModel = hiltViewModel(),
    navigateToPagerScreen: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchCityScreen(modifier, viewModel, uiState, navigateToPagerScreen)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchCityViewModel = hiltViewModel(),
    uiState: UserCitiesUiState,
    navigateToPagerScreen: (Int) -> Unit
) {
    val isDay = !isSystemInDarkTheme()
    val autoCompletionResult = viewModel.autoCompletionResult
    val inSelectionMode by remember { derivedStateOf { viewModel.selectedCitiesToRemove.isNotEmpty() } }
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val state = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val scope = rememberCoroutineScope()
    LaunchedEffect(inSelectionMode) {
        if (inSelectionMode) {
            state.bottomSheetState.expand()
        }
    }
    LaunchedEffect(!inSelectionMode) {
        if (!inSelectionMode) {
            state.bottomSheetState.hide()
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(skyBrush(isDay))) {
        BottomSheetScaffold(
            containerColor = Color.Transparent,
            sheetDragHandle = {},
            scaffoldState = state,
            sheetPeekHeight = 0.dp,
            sheetShape = RectangleShape,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Outlined.Delete, null, tint = RainTint,
                        modifier = Modifier
                            .size(35.dp)
                            .clickable {
                                scope.launch {
                                    viewModel.deleteCitiesFromUserCities()
                                    state.bottomSheetState.hide()
                                }
                            })
                    Text(
                        text = stringResource(R.string.delete_cities),
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = inkColor(isDay)
                    )
                }
            },
            content = {
                Column(
                    modifier = modifier
                        .safeDrawingPadding()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = stringResource(R.string.manage_cities),
                        fontFamily = Poppins,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = inkColor(isDay)
                    )
                    SearchAutoComplete(cities = autoCompletionResult, isDay = isDay) { city ->
                        viewModel.addCityToUserFavoriteCities(city)
                    }
                    Spacer(Modifier.height(16.dp))
                    when (uiState) {
                        UserCitiesUiState.Error -> ErrorScreen { viewModel.reload() }
                        UserCitiesUiState.Loading -> LoadingScreen()
                        is UserCitiesUiState.Success ->
                            if (uiState.userCities.isNotEmpty()) {
                                UserCitiesList(
                                    viewModel = viewModel,
                                    savedCities = uiState.userCities.toList(),
                                    inSelectionMode = inSelectionMode,
                                    isDay = isDay,
                                    onItemSelected = { index -> navigateToPagerScreen(index) })
                            }
                    }
                }
            })
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserCitiesList(
    viewModel: SearchCityViewModel,
    savedCities: List<SavedCity>,
    onItemSelected: (Int) -> Unit,
    inSelectionMode: Boolean,
    isDay: Boolean
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(items = savedCities) { index, savedCity ->
            val selected by remember { derivedStateOf { savedCity in viewModel.selectedCitiesToRemove } }
            UserCityItem(
                selected = selected,
                inSelectionMode = inSelectionMode,
                savedCity = savedCity,
                isDay = isDay,
                modifier = if (inSelectionMode) {
                    Modifier.clickable {
                        if (selected) viewModel.selectCityToRemove(savedCity) else viewModel.unSelectCityToRemove(
                            savedCity
                        )
                    }
                } else {
                    Modifier.combinedClickable(
                        onLongClick = { viewModel.unSelectCityToRemove(savedCity) },
                        onClick = { onItemSelected(index) }
                    )
                })
        }
    }
}


@Composable
fun UserCityItem(
    selected: Boolean,
    inSelectionMode: Boolean,
    savedCity: SavedCity,
    isDay: Boolean,
    modifier: Modifier = Modifier
) {
    val cityIsDay = savedCity.isDay ?: isDay
    Row(
        modifier = modifier
            .fillMaxWidth()
            .glassCard(isDay)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (cityIsDay) R.drawable.ic_ph_sun else R.drawable.ic_ph_moon
            ),
            contentDescription = null,
            tint = if (cityIsDay) SunTint else MoonTint,
            modifier = Modifier.size(30.dp)
        )
        Text(
            modifier = Modifier.weight(1f),
            text = savedCity.name,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = inkColor(isDay)
        )
        savedCity.temperature?.let { temperature ->
            Text(
                text = "${temperature}°",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = inkColor(isDay)
            )
        }
        if (inSelectionMode) {
            if (selected) {
                Icon(Icons.Filled.CheckCircle, null, tint = RainTint)
            } else {
                Icon(Icons.Filled.RadioButtonUnchecked, null, tint = mutedColor(isDay))
            }
        }
    }
}
