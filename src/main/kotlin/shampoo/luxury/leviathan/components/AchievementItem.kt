package shampoo.luxury.leviathan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shampoo.luxury.leviathan.wrap.data.achievements.Achievement
import sun.java2d.loops.ProcessPath.drawPath
import sun.swing.SwingUtilities2.drawRect

/**
 * Composable function for displaying an individual achievement.
 *
 * @param achievement The achievement to display.
 */
@Composable
fun AchievementItem(achievement: Achievement) {
    Card(
        Modifier.fillMaxWidth(),
        RoundedCornerShape(8.dp),
        elevation = 2.dp,
        border =
            if (achievement.isUnlocked) {
                BorderStroke(1.dp, MaterialTheme.colors.primary)
            } else {
                null
            },
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Status icon
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (achievement.isUnlocked) {
                            MaterialTheme.colors.primary
                        } else {
                            Color.Gray.copy(alpha = 0.3f)
                        },
                    ),
                Alignment.Center,
            ) {
                if (achievement.isUnlocked) {
                    Canvas(Modifier.size(24.dp)) {
                        val path =
                            Path().apply {
                                moveTo(size.width * 0.2f, size.height * 0.5f)
                                lineTo(size.width * 0.45f, size.height * 0.75f)
                                lineTo(size.width * 0.8f, size.height * 0.25f)
                            }
                        drawPath(
                            path,
                            Color.White,
                            style = Stroke(width = size.width * 0.1f, cap = StrokeCap.Round),
                        )
                    }
                } else {
                    // Draw a simple lock shape using Canvas
                    Canvas(Modifier.size(24.dp)) {
                        // Draw the lock body
                        drawRect(
                            Color.Gray,
                            Offset(size.width * 0.25f, size.height * 0.4f),
                            Size(size.width * 0.5f, size.height * 0.5f),
                        )
                        // Draw the lock shackle
                        drawArc(
                            Color.Gray,
                            0f,
                            180f,
                            false,
                            Offset(size.width * 0.25f, size.height * 0.15f),
                            Size(size.width * 0.5f, size.height * 0.5f),
                            style = Stroke(width = size.width * 0.1f),
                        )
                    }
                }
            }

            Spacer(Modifier.width(16.dp))

            // Achievement details
            Column(Modifier.weight(1f)) {
                Text(
                    achievement.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                Text(
                    achievement.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Reward: ${achievement.reward}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primary,
                )
            }
        }
    }
}
