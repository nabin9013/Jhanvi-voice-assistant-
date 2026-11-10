package com.example

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.JhanviBottomNav
import com.example.ui.components.JhanviDrawerContent
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AccessibilityScreen
import com.example.ui.screens.AdvancedSettingsScreen
import com.example.ui.screens.BrowserScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.DocumentsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JhanviHomeScreen
import com.example.ui.screens.JhanviRulesScreen
import com.example.ui.screens.MarketScreen
import com.example.ui.screens.MarketingScreen
import com.example.ui.screens.MemoriesScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PcPhoneScreen
import com.example.ui.screens.PhotosScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.UpgradeScreen
import com.example.ui.screens.WebCodingScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.JhanviTheme
import com.example.viewmodel.JhanviViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val jhanviViewModel: JhanviViewModel = viewModel()
            val settings by jhanviViewModel.settings.collectAsState()

            val audioPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                // Audio permission handled
            }

            androidx.compose.runtime.LaunchedEffect(Unit) {
                if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    audioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                }
            }

            JhanviTheme(darkTheme = settings.darkTheme) {
                JhanviMainContainer(viewModel = jhanviViewModel)
            }
        }
    }
}

@Composable
fun JhanviMainContainer(viewModel: JhanviViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            JhanviDrawerContent(
                currentRoute = currentScreen,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    viewModel.navigateTo(route)
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .statusBarsPadding()
        ) {
            // Main content screens
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentScreen) {
                    "home" -> HomeScreen(
                        viewModel = viewModel,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                    "scanner" -> ScannerScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "memories" -> MemoriesScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "chat" -> ChatScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "messages" -> MessagesScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })

                    // Advanced features
                    "browser" -> BrowserScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "coding" -> WebCodingScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "photos" -> PhotosScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "documents" -> DocumentsScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "marketing" -> MarketingScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "market" -> MarketScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "jhanvi_home" -> JhanviHomeScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })

                    // Drawer screens
                    "settings" -> SettingsScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "accessibility" -> AccessibilityScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "jhanvi_rules" -> JhanviRulesScreen(onBack = { viewModel.navigateTo("home") })
                    "pc_phone" -> PcPhoneScreen(viewModel = viewModel, onBack = { viewModel.navigateTo("home") })
                    "privacy_policy" -> PrivacyPolicyScreen(onBack = { viewModel.navigateTo("home") })
                    "about" -> AboutScreen(onBack = { viewModel.navigateTo("home") })
                    "upgrade" -> UpgradeScreen(onBack = { viewModel.navigateTo("home") })
                    "notifications" -> NotificationsScreen(onBack = { viewModel.navigateTo("home") })
                    "advanced_settings" -> AdvancedSettingsScreen(onBack = { viewModel.navigateTo("home") })

                    else -> HomeScreen(
                        viewModel = viewModel,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
            }

            // Always display the 7 main navigation buttons when on primary tabs
            if (currentScreen in listOf("home", "scanner", "memories", "chat", "messages")) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    JhanviBottomNav(
                        currentScreen = currentScreen,
                        isAssistantOn = settings.isAssistantOn,
                        onNavigate = { viewModel.navigateTo(it) },
                        onToggleAssistant = { viewModel.toggleAssistantState() },
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
            }
        }
    }
}
