package com.alllearn.app.ui.screens.courses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alllearn.app.R
import com.alllearn.app.data.model.Course
import com.alllearn.app.ui.components.AllLearnBottomBar
import com.alllearn.app.ui.components.AllLearnTopAppBar
import com.alllearn.app.ui.viewmodel.DashboardViewModel

@Composable
fun CourseListScreen(
    viewModel: DashboardViewModel,
    userId: String,
    onOpenCourse: (String) -> Unit,
    onStartNewCourse: () -> Unit,
    onNavigate: (String) -> Unit,
    onOpenBot: (String) -> Unit
) {
    val coursesState = viewModel.getCoursesForUser(userId).collectAsState(initial = emptyList())
    val activeCourseId by viewModel.activeCourseId.collectAsState()

    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = stringResource(R.string.nav_courses),
                actions = {
                    IconButton(onClick = onStartNewCourse) {
                        Icon(Icons.Default.Add, contentDescription = "New Course")
                    }
                }
            )
        },
        bottomBar = {
            AllLearnBottomBar(
                currentRoute = "courses",
                onNavigate = onNavigate,
                onOpenBot = { onOpenBot(activeCourseId ?: "none") }
            )
        }
    ) { padding ->
        val courses = coursesState.value
        if (courses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.no_active_course),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onStartNewCourse) {
                        Text(stringResource(R.string.start_new_course))
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(courses) { course ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenCourse(course.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = course.outline.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${course.area} • ${course.deadlineWeeks} Weeks • ${course.hoursPerWeek} hrs/week",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Target: ${course.targetEndDate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
