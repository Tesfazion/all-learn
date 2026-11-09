package com.alllearn.app.ui.screens.privacy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alllearn.app.R
import com.alllearn.app.ui.components.AllLearnTopAppBar

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AllLearnTopAppBar(
                title = stringResource(R.string.privacy_policy),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionHeading(text = "Introduction")
            Text(
                text = "All Learn respects your privacy. This Privacy Policy explains how we collect, use, disclose, and safeguard your information when you use our mobile application (the \"Service\").",
                style = MaterialTheme.typography.bodyMedium
            )

            SectionHeading(text = "Information We Collect")
            Text(
                text = "We collect information you provide directly to us, such as when you register for an account, complete onboarding, use the AI Helper Bot, submit quiz answers, or contact support. This may include your name, email address, password (hashed), study preferences, learning progress, course selections, and chat messages with the Helper Bot.",
                style = MaterialTheme.typography.bodyMedium
            )

            SectionHeading(text = "How We Use Your Information")
            Text(
                text = "We use your information to provide, maintain, and improve the Service; personalize your learning plan; generate AI outlines and lessons; send study reminders; authenticate your account; and communicate with you about your account or the Service.",
                style = MaterialTheme.typography.bodyMedium
            )

            SectionHeading(text = "Data Sharing")
            Text(
                text = "We do not sell your personal data. We may share information with service providers who help us operate the app (e.g., cloud hosting, AI APIs, crash reporting), but only for the purposes described in this policy and under strict confidentiality obligations. AI processing occurs in Cloud Functions—no API keys or secrets are stored in the app.",
                style = MaterialTheme.typography.bodyMedium
            )

            SectionHeading(text = "Your Rights")
            Text(
                text = "You may access, update, or delete your account and all associated data at any time through Settings > Delete Account & Data. You may also opt out of non-essential notifications.",
                style = MaterialTheme.typography.bodyMedium
            )

            SectionHeading(text = "Security")
            Text(
                text = "We use industry-standard encryption in transit and at rest. Firestore security rules ensure users can read and write only their own documents. We rate-limit AI calls per user on the server. No API keys exist inside the APK or repository.",
                style = MaterialTheme.typography.bodyMedium
            )

            SectionHeading(text = "Children's Privacy")
            Text(
                text = "The Service is not directed to children under 13. We do not knowingly collect personal information from children under 13.",
                style = MaterialTheme.typography.bodyMedium
            )

            SectionHeading(text = "Contact Us")
            Text(
                text = "If you have questions about this Privacy Policy, please contact us at privacy@alllearn.app.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}
