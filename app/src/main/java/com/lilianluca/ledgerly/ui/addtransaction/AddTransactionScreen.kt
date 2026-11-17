package com.lilianluca.ledgerly.ui.addtransaction

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.lilianluca.ledgerly.R
import com.lilianluca.ledgerly.domain.model.TransactionType
import com.lilianluca.ledgerly.ui.theme.LedgerlyTheme
import com.lilianluca.ledgerly.util.formatDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/** Stateful entry point: connects the form to its ViewModel and navigates back after saving. */
@Composable
fun AddTransactionRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddTransactionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // One-off reaction to state (like useEffect with a dependency): leave the screen once saved
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigateBack()
    }

    AddTransactionScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onTypeChange = viewModel::onTypeChange,
        onAmountChange = viewModel::onAmountChange,
        onCategoryChange = viewModel::onCategoryChange,
        onDateChange = viewModel::onDateChange,
        onNoteChange = viewModel::onNoteChange,
        onSave = viewModel::onSave,
        modifier = modifier,
    )
}

/** Stateless form: renders [uiState] and reports every user action through callbacks. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    uiState: AddTransactionUiState,
    onNavigateBack: () -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Pure UI state (dialog open/closed) lives in the composable; rememberSaveable survives rotation
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_transaction_title)) },
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
                // Avoid adding the navigation bar inset twice when the keyboard opens
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TransactionTypeSelector(
                selected = uiState.type,
                onSelect = onTypeChange,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = uiState.amountText,
                onValueChange = onAmountChange,
                label = { Text(stringResource(R.string.field_amount)) },
                suffix = { Text(stringResource(R.string.currency_suffix)) },
                isError = uiState.amountError != null,
                // Always present, so the layout doesn't jump when an error appears
                supportingText = {
                    uiState.amountError?.let { Text(stringResource(it.messageRes())) }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next,
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Transfers have no category
            if (uiState.type != TransactionType.TRANSFER) {
                OutlinedTextField(
                    value = uiState.category,
                    onValueChange = onCategoryChange,
                    label = { Text(stringResource(R.string.field_category)) },
                    isError = uiState.categoryError,
                    supportingText = {
                        if (uiState.categoryError) Text(stringResource(R.string.error_category_empty))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            DateField(
                date = uiState.date,
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = uiState.note,
                onValueChange = onNoteChange,
                label = { Text(stringResource(R.string.field_note)) },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                minLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            Button(
                onClick = onSave,
                enabled = !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }

    if (showDatePicker) {
        TransactionDatePickerDialog(
            initialDate = uiState.date,
            onConfirm = { date ->
                onDateChange(date)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
        )
    }
}

@Composable
private fun TransactionTypeSelector(
    selected: TransactionType,
    onSelect: (TransactionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Explicit order: expense first, as it is the most common type
    val options = listOf(
        TransactionType.EXPENSE to R.string.transaction_type_expense,
        TransactionType.INCOME to R.string.transaction_type_income,
        TransactionType.TRANSFER to R.string.transaction_type_transfer,
    )
    SingleChoiceSegmentedButtonRow(modifier = modifier.padding(bottom = 8.dp)) {
        options.forEachIndexed { index, (type, labelRes) ->
            SegmentedButton(
                selected = type == selected,
                onClick = { onSelect(type) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                label = { Text(stringResource(labelRes)) },
            )
        }
    }
}

/** Read-only text field showing the date; tapping anywhere on it opens the picker. */
@Composable
private fun DateField(
    date: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val locale = LocalConfiguration.current.locales[0]
    val label = stringResource(R.string.field_date)
    val formattedDate = formatDate(date, locale)
    Box(modifier = modifier) {
        OutlinedTextField(
            value = formattedDate,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            // Hidden from screen readers; the overlay below describes the whole field instead
            modifier = Modifier
                .fillMaxWidth()
                .clearAndSetSemantics {},
        )
        // Transparent overlay: a read-only text field would otherwise swallow the tap
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    onClickLabel = stringResource(R.string.select_date),
                    role = Role.Button,
                    onClick = onClick,
                )
                .semantics { contentDescription = "$label: $formattedDate" },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionDatePickerDialog(
    initialDate: LocalDate,
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    // The Material date picker works with UTC milliseconds, so convert LocalDate <-> millis in UTC
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = state.selectedDateMillis
                    if (millis != null) {
                        onConfirm(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                    } else {
                        onDismiss()
                    }
                },
            ) {
                // System-provided strings are already translated to every language
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        },
    ) {
        DatePicker(state = state)
    }
}

@StringRes
private fun AmountError.messageRes(): Int = when (this) {
    AmountError.EMPTY -> R.string.error_amount_empty
    AmountError.INVALID -> R.string.error_amount_invalid
    AmountError.ZERO -> R.string.error_amount_zero
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddTransactionScreenPreview() {
    LedgerlyTheme {
        AddTransactionScreen(
            uiState = AddTransactionUiState(date = LocalDate.of(2026, 11, 17)),
            onNavigateBack = {},
            onTypeChange = {},
            onAmountChange = {},
            onCategoryChange = {},
            onDateChange = {},
            onNoteChange = {},
            onSave = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, locale = "cs")
@Composable
private fun AddTransactionScreenErrorsPreview() {
    LedgerlyTheme {
        AddTransactionScreen(
            uiState = AddTransactionUiState(
                amountText = "12,345",
                date = LocalDate.of(2026, 11, 17),
                amountError = AmountError.INVALID,
                categoryError = true,
            ),
            onNavigateBack = {},
            onTypeChange = {},
            onAmountChange = {},
            onCategoryChange = {},
            onDateChange = {},
            onNoteChange = {},
            onSave = {},
        )
    }
}
