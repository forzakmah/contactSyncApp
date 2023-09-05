package com.bkcoding.contactsyncapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bkcoding.contactsyncapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    viewModel: ContactViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState()
    )
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResultUiState by viewModel.searchContactUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = stringResource(id = R.string.title_contacts),
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        when (val state = searchResultUiState) {
            SearchContactUiState.SearchContactNotReady -> CenteredTextIndicator(
                text = stringResource(id = R.string.please_wait_sync_in_progress)
            )

            SearchContactUiState.Failed -> Unit
            SearchContactUiState.Loading -> Unit
            is SearchContactUiState.Success ->
                if (state.contacts.isEmpty())
                    CenteredTextIndicator(
                        text = stringResource(id = R.string.zero_contact_found)
                    )
                else
                    ContactsItems(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        query = query,
                        contacts = state.contacts,
                        onSearchQueryChanged = viewModel::onSearchQueryChanged
                    )

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsItems(
    modifier: Modifier = Modifier,
    query: String,
    contacts: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"),
    onSearchQueryChanged: (String) -> Unit
) {
    LazyColumn(modifier = modifier)
    {
        /**
         * the sticky header represent the search field to filter contacts by fullName and/or phone number
         */
        stickyHeader {
            SearchField(
                query = query,
                onValueChange = onSearchQueryChanged
            )
        }

        contacts.forEachIndexed { index, contact ->
            if (index > 0)
                item {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 0.25.dp,
                        color = Color.DarkGray
                    )
                }
            item {
                ContactRow(
                    contact = "Alex le testeur $index",
                    phoneNumber = "+216 25 072 165",
                    onClick = {
                        /**
                         * when user click over the row
                         */
                    }
                ) {
                    /**
                     * when the user click the call icon
                     */
                }
            }
        }
    }
}

@Composable
fun ContactRow(
    modifier: Modifier = Modifier,
    contact: String,
    phoneNumber: String,
    onClick: () -> Unit,
    call: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ContactScreenConfig.contactRowHeight.dp)
            .clickable {
                onClick.invoke()
            }
            .padding(ContactScreenConfig.padding.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularPrefixName(
                firstName = "Saul",
                lastName = "Cambell"
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = contact,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.size(2.5.dp))
                Text(
                    text = phoneNumber,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    fontSize = 14.sp
                )
            }
        }
        IconButton(
            onClick = call
        ) {
            Icon(
                imageVector = Icons.Filled.Call,
                contentDescription = null
            )
        }
    }
}

@Composable
fun CircularPrefixName(
    modifier: Modifier = Modifier,
    firstName: String,
    lastName: String,
) {
    Card(
        modifier = modifier.size(ContactScreenConfig.circularPrefixFullName.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier.size(ContactScreenConfig.circularPrefixFullName.dp)
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                text = "${firstName.first().uppercase()}${lastName.first().uppercase()}",
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        }
    }
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    query: String,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(ContactScreenConfig.padding.dp)
    ) {
        val containerColor = Color.White.copy(0.1f)
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(ContactScreenConfig.searchFieldHeight.dp)
                .align(Alignment.Center),
            value = query,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                imeAction = if (query.isEmpty()) ImeAction.Done else ImeAction.None
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    /**
                     * hide keyboard
                     */
                    focusManager.clearFocus()
                }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = stringResource(id = R.string.app_name),
                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.placeholder_search),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            },
            shape = RoundedCornerShape(ContactScreenConfig.searchFieldCornerRadius.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}

@Composable
fun CenteredTextIndicator(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

object ContactScreenConfig {
    const val searchFieldCornerRadius = 20
    const val searchFieldHeight = 50
    const val padding = 16
    const val circularPrefixFullName = 50
    const val contactRowHeight = 80
}