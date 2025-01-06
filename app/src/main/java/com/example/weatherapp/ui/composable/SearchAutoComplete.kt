package com.example.weatherapp.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.model.AutoCompleteResult
import com.example.weatherapp.data.model.AutoCompleteResultItem
import com.example.weatherapp.ui.search_city.SearchCityViewModel
import com.example.weatherapp.ui.theme.LocalAppBarColor
import com.example.weatherapp.ui.theme.LocalBackgroundColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAutoComplete(
    viewModel: SearchCityViewModel = hiltViewModel(),
    cities: List<AutoCompleteResult>,
    onSelect: (AutoCompleteResult) -> Unit
) {

    var query by remember {
        mutableStateOf("")
    }
    val heightTextFields by remember {
        mutableStateOf(55.dp)
    }
    var autocompleteTextFieldWidth by remember {
        mutableStateOf(Size.Zero)
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Query Field
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightTextFields)
                    .border(
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .onGloballyPositioned { coordinates ->
                        autocompleteTextFieldWidth = coordinates.size.toSize()
                    },
                value = query,
                onValueChange = { newQuery ->
                    query = newQuery
                    viewModel.getAutoCompleteSearch(query)
                    expanded = true
                },
                placeholder = { Text(text = stringResource(R.string.place_holder_hint), fontWeight = FontWeight.SemiBold) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Gray
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )
        }

        AnimatedVisibility(visible = expanded) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .width(autocompleteTextFieldWidth.width.dp),
                shape = RoundedCornerShape(8.dp)
            ) {

                LazyColumn {
                    if (query.isNotEmpty()) {
                        items(
                            cities
                                .filter {
                                    it.shortName.lowercase()
                                        .contains(query.lowercase()) || it.shortName.lowercase()
                                        .contains("others")
                                }
                                .distinct()
                        ) { city ->
                            ItemsCategory(
                                city = city,
                                onSelect = {
                                    onSelect(city)
                                   expanded = !expanded
                                    query = ""
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ItemsCategory(
    city: AutoCompleteResult,
    onSelect: (AutoCompleteResult) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(city) }
            .padding(8.dp)
    ) {
        Text(text = city.longName, fontSize = 16.sp)
    }

}