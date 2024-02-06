package com.eltex.androidschool.feature.paging

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eltex.androidschool.R
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.theme.ComposeAppTheme

@Composable
fun ItemProgress(count: Int) {
    var i = 0
    val event = EventUiModel(
        content = stringResource(id = R.string.skeleton_content),
        author = stringResource(id = R.string.skeleton_author),
        published = stringResource(id = R.string.skeleton_published),
        type = stringResource(id = R.string.skeleton_type),
        datetime = stringResource(id = R.string.skeleton_datetime),
        link = stringResource(id = R.string.skeleton_link),
        likes = stringResource(id = R.string.skeleton_likes).toInt(),
        likedByMe = true,
        participants = stringResource(id = R.string.skeleton_participants).toInt(),
    )
    while (i < count) {
        CardSkeletonEvent(
            event = event,
            )

        Spacer(modifier = Modifier.height(8.dp))
        i++
    }
}

@Preview
@Composable
fun ItemProgressPreview() {
    ComposeAppTheme {
        ItemProgress(count = 10)
    }
}