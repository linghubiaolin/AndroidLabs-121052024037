package com.example.layoutlab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TaskRed = Color(0xFFD32F2F)
private val PageBg = Color(0xFFF7F7F7)

/** 一条学习任务 */
private data class StudyTask(
    val title: String,
    val done: Boolean = false
)

@Composable
fun TaskScreen() {
    // mutableStateListOf：列表内容变化会自动触发重组
    val tasks = remember {
        mutableStateListOf(
            StudyTask("学习 Column 和 Row", true),
            StudyTask("学习状态管理"),
            StudyTask("完成 Compose 实验")
        )
    }
    var input by remember { mutableStateOf("") }
    val doneCount = tasks.count { it.done }

    MaterialTheme(colorScheme = lightColorScheme(primary = TaskRed)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PageBg)
                .safeDrawingPadding()
                .padding(20.dp)
        ) {
            // 标题
            Text(
                text = "课程学习任务",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TaskRed
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 输入框 + 添加按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("请输入学习任务") },
                    singleLine = true,
                    shape = RoundedCornerShape(4.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {
                        val title = input.trim()
                        if (title.isNotEmpty()) {
                            tasks.add(StudyTask(title))
                            input = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TaskRed),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text(text = "添加", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 完成进度：随勾选、添加、删除自动变化
            Text(
                text = "已完成：$doneCount / ${tasks.size}",
                fontSize = 13.sp,
                color = Color(0xFF757575)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 任务列表
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(tasks.size) { index ->
                    TaskRow(
                        task = tasks[index],
                        onCheckedChange = { checked ->
                            tasks[index] = tasks[index].copy(done = checked)
                        },
                        onDelete = { tasks.removeAt(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: StudyTask,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.done,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(checkedColor = TaskRed)
            )

            Text(
                text = task.title,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp,
                color = if (task.done) Color(0xFF9E9E9E) else Color(0xFF212121),
                textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None
            )

            TextButton(onClick = onDelete) {
                Text(text = "删除", fontSize = 13.sp, color = TaskRed)
            }
        }
    }
}
