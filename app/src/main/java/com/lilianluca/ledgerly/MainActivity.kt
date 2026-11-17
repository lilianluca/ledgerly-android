package com.lilianluca.ledgerly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lilianluca.ledgerly.ui.navigation.LedgerlyNavDisplay
import com.lilianluca.ledgerly.ui.theme.LedgerlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LedgerlyTheme {
                LedgerlyNavDisplay()
            }
        }
    }
}
