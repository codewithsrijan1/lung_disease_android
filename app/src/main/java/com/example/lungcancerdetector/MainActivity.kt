package com.example.lungcancerdetector

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    val classifier = remember { LungCancerClassifier(context) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var results by remember { mutableStateOf<List<Pair<String, Float>>>(emptyList()) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
        uri?.let {
            results = classifier.classifyFromUri(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Lung Cancer Detector",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Model Accuracy: 92.4%", color = Color.Gray, fontSize = 14.sp)
            Text("Input Size: 350 × 350 RGB", color = Color.Gray, fontSize = 14.sp)
            Text("Architecture: Xception + GAP", color = Color.Gray, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri == null) {
                Text(
                    text = "Select CT Image",
                    color = Color.LightGray,
                    fontSize = 18.sp
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { picker.launch("image/*") },
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
        ) {
            Text("Choose Image", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = results.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            val topResult = results.firstOrNull()
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = topResult?.first ?: "-",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), color = Color(0xFFEEEEEE))
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Confidence: ${((topResult?.second ?: 0f) * 100).toInt()}%",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
        
        if (results.isEmpty()) {
            Text("-", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Confidence: 0%", fontSize = 14.sp, color = Color.Black)
        }
    }
}
