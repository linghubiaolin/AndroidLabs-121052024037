package com.example.layoutlab

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExperimentMenu()
        }
    }
}

private data class Experiment(
    val no: String,
    val name: String,
    val tech: String,
    val page: Class<*>
)

@Composable
private fun ExperimentMenu() {
    val context = LocalContext.current

    val experiments = listOf(
        Experiment("01", "线性布局", "LinearLayout · layout_weight", LinearLayoutActivity::class.java),
        Experiment("02", "表格布局", "TableLayout · stretchColumns", TableLayoutActivity::class.java),
        Experiment("03", "约束布局 · 计算器", "ConstraintLayout · Chain", CalculatorActivity::class.java),
        Experiment("04", "约束布局 · 太空订票", "ConstraintLayout · Bias", SpaceActivity::class.java),
        Experiment("05", "Compose 任务清单", "Jetpack Compose · State", ComposeTaskActivity::class.java)
    )

    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF009688))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Android 界面布局实验",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
            Text(
                text = "实验二 · 五种布局方式",
                fontSize = 13.sp,
                color = Color(0xFF757575)
            )
            Spacer(modifier = Modifier.height(4.dp))

            experiments.forEach { experiment ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            context.startActivity(Intent(context, experiment.page))
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF009688)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = experiment.no,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = experiment.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF212121)
                            )
                            Text(
                                text = experiment.tech,
                                fontSize = 12.sp,
                                color = Color(0xFF757575)
                            )
                        }

                        Text(
                            text = "›",
                            fontSize = 20.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            }
        }
    }
}
