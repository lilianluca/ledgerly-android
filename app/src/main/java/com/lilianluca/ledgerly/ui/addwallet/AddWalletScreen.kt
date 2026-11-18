package com.lilianluca.ledgerly.ui.addwallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lilianluca.ledgerly.R
import com.lilianluca.ledgerly.domain.model.WalletType
import com.lilianluca.ledgerly.ui.theme.LedgerlyTheme
import com.lilianluca.ledgerly.ui.wallets.labelRes

/** Stateful entry point: connects the form to its ViewModel and navigates back after saving. */
@Composable
fun AddWalletRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddWalletViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigateBack()
    }

    AddWalletScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNameChange = viewModel::onNameChange,
        onTypeChange = viewModel::onTypeChange,
        onInitialBalanceChange = viewModel::onInitialBalanceChange,
        onSave = viewModel::onSave,
        modifier = modifier,
    )
}

/** Stateless form: renders [uiState] and reports every user action through callbacks. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWalletScreen(
    uiState: AddWalletUiState,
    onNavigateBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onTypeChange: (WalletType) -> Unit,
    onInitialBalanceChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_wallet_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            WalletTypeSelector(
                selected = uiState.type,
                onSelect = onTypeChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )

            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text(stringResource(R.string.field_wallet_name)) },
                isError = uiState.nameError,
                supportingText = {
                    if (uiState.nameError) Text(stringResource(R.string.error_wallet_name_empty))
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = uiState.initialBalanceText,
                onValueChange = onInitialBalanceChange,
                label = { Text(stringResource(R.string.field_initial_balance)) },
                suffix = { Text(stringResource(R.string.currency_suffix)) },
                isError = uiState.initialBalanceError,
                supportingText = {
                    if (uiState.initialBalanceError) {
                        Text(stringResource(R.string.error_amount_invalid))
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = onSave,
                enabled = !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
private fun WalletTypeSelector(
    selected: WalletType,
    onSelect: (WalletType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val types = WalletType.entries
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        types.forEachIndexed { index, type ->
            SegmentedButton(
                selected = type == selected,
                onClick = { onSelect(type) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = types.size),
                label = { Text(stringResource(type.labelRes())) },
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, locale = "cs")
@Composable
private fun AddWalletScreenPreview() {
    LedgerlyTheme {
        AddWalletScreen(
            uiState = AddWalletUiState(nameError = true),
            onNavigateBack = {},
            onNameChange = {},
            onTypeChange = {},
            onInitialBalanceChange = {},
            onSave = {},
        )
    }
}
