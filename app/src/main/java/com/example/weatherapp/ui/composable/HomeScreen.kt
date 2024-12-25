package com.example.weatherapp.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
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
import com.example.weatherapp.data.model.AutoCompleteResultItem
import com.example.weatherapp.ui.theme.Purple40
import com.example.weatherapp.ui.weather.WeatherViewModel

@Composable
fun HomeScreen(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.welcome_message),
            modifier = Modifier.padding(8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .height(60.dp)
                .width(180.dp),
            colors = buttonColors(containerColor = Purple40, contentColor = Color.White),
            onClick = onClick
        ) {
            Text(
                text = "Button",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAutoComplete(
    viewModel: WeatherViewModel = hiltViewModel(),
    cities: List<AutoCompleteResultItem>,
    onSelect: (AutoCompleteResultItem) -> Unit
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

    // Query Field
    Column(
        modifier = Modifier
            .padding(30.dp)
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
                        width = 1.8.dp,
                        color = Color.Black,
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
                placeholder = { Text("Enter city name") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "arrow",
                            tint = Color.Black
                        )
                    }
                }
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
                                .filter { it.display_place?.lowercase().orEmpty()
                                    .contains(query.lowercase()) || it.display_place?.lowercase().orEmpty()
                                    .contains("others")
                            }
                                .distinct()
                        ) { city ->
                            ItemsCategory(city = city, onSelect = onSelect)
                        }
                    }
                }

            }
        }

    }

}


@Composable
fun ItemsCategory(
    city: AutoCompleteResultItem,
    onSelect: (AutoCompleteResultItem) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(city)
            }
            .padding(10.dp)
    ) {
        Text(text = city.display_name.orEmpty(), fontSize = 16.sp)
    }

}