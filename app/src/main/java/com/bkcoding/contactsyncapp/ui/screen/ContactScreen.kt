package com.bkcoding.contactsyncapp.ui.screen

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bkcoding.contactsyncapp.R
import com.bkcoding.contactsyncapp.model.ContactModel
import com.bkcoding.contactsyncapp.utils.hasPermission
import com.bkcoding.contactsyncapp.utils.requestPermissionLauncher


const val readContactsPermission = Manifest.permission.READ_CONTACTS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactRoute(
    viewModel: ContactViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState()
    )
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResultUiState by viewModel.searchContactUiState.collectAsStateWithLifecycle()
    var hasPermission by remember {
        mutableStateOf(hasPermission(context, readContactsPermission))
    }
    val permissionLauncher = requestPermissionLauncher(
        permissionCallback = {
            hasPermission = it
        }
    )

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
        if (hasPermission)
            ContactScreen(
                padding = padding,
                query = query,
                searchResultUiState = searchResultUiState,
                onSearchQueryChanged = viewModel::onSearchQueryChanged
            )
        else
            RequestPermissionView(
                padding = padding,
                launcher = permissionLauncher
            )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ContactScreen(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    searchResultUiState: SearchContactUiState,
    query: String,
    onSearchQueryChanged: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        /**
         * the sticky header represent the search field to filter contacts by fullName and/or phone number
         */
        stickyHeader {
            SearchField(
                query = query,
                onValueChange = onSearchQueryChanged
            )
        }

        when (searchResultUiState) {
            SearchContactUiState.Failed,
            SearchContactUiState.Loading -> Unit

            SearchContactUiState.SearchContactNotReady ->
                item {
                    CenteredTextIndicator(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(padding),
                        text = stringResource(
                            id = R.string.please_wait_sync_in_progress
                        )
                    )
                }

            is SearchContactUiState.Success -> {
                if (searchResultUiState.contacts.isEmpty())
                    item {
                        CenteredTextIndicator(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(padding),
                            text = stringResource(id = R.string.zero_contact_found)
                        )
                    }
                else {
                    searchResultUiState.contacts.forEachIndexed { index, contact ->
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
                                contact = contact,
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
        }
    }
}

@Composable
fun ContactRow(
    modifier: Modifier = Modifier,
    contact: ContactModel,
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
                first = contact.displayName,
                second = contact.familyName
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = contact.displayName,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.size(2.5.dp))
                if (contact.phoneNumbers.isNotEmpty())
                    Text(
                        text = contact.phoneNumbers.first().trim(),
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
    first: String,
    second: String?,
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
                text = "${first.first().uppercase()}${second?.first()?.uppercase() ?: ""}",
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
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
internal fun RequestPermissionView(
    padding: PaddingValues,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.request_permission),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = String.format(
                stringResource(id = R.string.text_request_permission),
                stringResource(id = R.string.app_name)
            ),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                launcher.launch(readContactsPermission)
            }
        ) {
            Text(
                text = stringResource(id = R.string.btn_request_permission)
            )
        }
    }
}

object ContactScreenConfig {
    const val searchFieldCornerRadius = 20
    const val searchFieldHeight = 50
    const val padding = 16
    const val circularPrefixFullName = 50
    const val contactRowHeight = 80
}