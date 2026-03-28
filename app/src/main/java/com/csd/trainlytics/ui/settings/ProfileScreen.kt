package com.csd.trainlytics.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csd.trainlytics.domain.model.Gender
import com.csd.trainlytics.domain.model.UserProfile

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var ageInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }

    // Populate fields once loaded
    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            name = state.profile.name
            ageInput = state.profile.ageYears?.toString() ?: ""
            heightInput = state.profile.heightCm?.toString() ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "返回", tint = Color.White)
            }
            Text(
                "个人资料",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3FFF8B))
            }
        } else {
            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF3FFF8B),
                unfocusedBorderColor = Color(0xFF3A3A3A),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color(0xFF3FFF8B),
                focusedLabelColor = Color(0xFF3FFF8B),
                unfocusedLabelColor = Color(0xFF9B9B9B)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("姓名") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = fieldColors
                )

                OutlinedTextField(
                    value = ageInput,
                    onValueChange = { ageInput = it.filter { c -> c.isDigit() }.take(3) },
                    label = { Text("年龄") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = fieldColors
                )

                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it.filter { c -> c.isDigit() || c == '.' }.take(5) },
                    label = { Text("身高 (cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = fieldColors
                )

                // Gender selector
                Text("性别", style = MaterialTheme.typography.bodySmall, color = Color(0xFF9B9B9B))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(Gender.MALE to "男", Gender.FEMALE to "女", Gender.UNSPECIFIED to "未设置").forEach { (gender, label) ->
                        val selected = state.profile.gender == gender
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (selected) Color(0xFF1E3320) else Color(0xFF1A1A1A),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.TextButton(
                                onClick = { viewModel.updateProfile(state.profile.copy(gender = gender)) },
                                colors = ButtonDefaults.textButtonColors()
                            ) {
                                Text(
                                    label,
                                    color = if (selected) Color(0xFF3FFF8B) else Color(0xFF9B9B9B),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.updateProfile(
                            state.profile.copy(
                                name = name.trim(),
                                ageYears = ageInput.toIntOrNull(),
                                heightCm = heightInput.toFloatOrNull()
                            )
                        )
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(
                            brush = Brush.linearGradient(listOf(Color(0xFF3FFF8B), Color(0xFF13EA79))),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "保存",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = Color(0xFF004820)
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
