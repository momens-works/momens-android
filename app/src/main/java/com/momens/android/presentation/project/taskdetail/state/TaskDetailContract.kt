package com.momens.android.presentation.project.taskdetail.state

import androidx.compose.runtime.Immutable
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.MomensStatusEditType
import com.momens.android.presentation.project.model.ChecklistItem
import com.momens.android.presentation.project.model.TaskRole
import com.momens.android.presentation.project.taskdetail.model.TaskDetailAssigneeModel
import com.momens.android.presentation.project.taskdetail.model.TaskDetailChecklistModel
import com.momens.android.presentation.project.taskdetail.model.TaskDetailFileModel
import com.momens.android.presentation.project.taskdetail.model.TaskDetailModel
import com.momens.android.presentation.project.taskdetail.model.TaskDetailQuestionModel
import com.momens.android.presentation.signal.model.SignalAccordionType
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

@Immutable
data class TaskDetailState(
    val taskDetail: TaskDetailModel? = null,
    val mutatingChecklistItemIds: ImmutableSet<String> = persistentSetOf()
) {
    companion object {
        val Fake = TaskDetailState(
            taskDetail = TaskDetailModel(
                id = "27afd507-9c7f-4f0d-a2be-fcdab2477b19",
                projectId = "30d9e9fe-f43b-4097-a88e-dc19f0a5b025",
                title = "일이삼사오육칠팔일이삼사오",
                status = MomensStatusEditType.TODO,
                role = TaskRole.PM,
                assignee = TaskDetailAssigneeModel(
                    id = "b9b1...e7",
                    name = "김민지",
                    avatarUrl = null,
                ),
                priority = ImportantLevel.MEDIUM,
                purpose = "어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구어쩌구저쩌구어쩌구저쩌구어쩌구어쩌구" +
                    "저쩌구어쩌구저쩌구어쩌구어쩌구저쩌구어쩌구저쩌구어쩌구구",
                checklist = TaskDetailChecklistModel(
                    completedCount = 0,
                    totalCount = 4,
                    items = persistentListOf(
                        ChecklistItem(id = "1", title = "어쩌구어쩌구 반영어쩌구어쩌구 반영어쩌구어쩌구 반", completed = false),
                        ChecklistItem(id = "2", title = "어쩌구어쩌구 반영어쩌구어쩌구 반영어쩌구어쩌구 반", completed = false),
                        ChecklistItem(id = "3", title = "어쩌구어쩌구 반영어쩌구어쩌구 반영어쩌구어쩌구 반", completed = false),
                        ChecklistItem(id = "4", title = "어쩌구어쩌구 반영어쩌구어쩌구 반영어쩌구어쩌구 반", completed = false),
                    ),
                ),
                materials = persistentListOf(
                    TaskDetailFileModel(
                        id = "1",
                        title = "회원가입 에러 메시지 정책 초안",
                        summary = "회원가입의 MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.\n회원가입의 MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                        kind = SignalAccordionType.FIGMA,
                        sourceUrl = "https://example.com",
                        createdAtText = "2026.07.15 22:48",
                    ),
                    TaskDetailFileModel(
                        id = "2",
                        title = "회원가입 에러 메시지 정책 초안",
                        summary = "회원가입의 MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                        kind = SignalAccordionType.FILE,
                        sourceUrl = "https://example.com",
                        createdAtText = "2026.07.15 22:50",
                    ),
                ),
                openQuestions = persistentListOf(
                    TaskDetailQuestionModel(
                        id = "1",
                        body = "약한 비밀번호 기준을 사용자에게 얼마나 구체적으로 알려줘야할 지 결정이 필요해보임",
                    ),
                    TaskDetailQuestionModel(
                        id = "2",
                        body = "약한 비밀번호 기준을 사용자에게 얼마나 구체적으로 알려줘야할 지 결정이 필요해보임",
                    ),
                ),
                nextAction = "민수가 추천해주는 다음행동이에용",
            ),
        )

        val FakeEmptyFields = TaskDetailState(
            taskDetail = TaskDetailModel(
                id = "27afd507-9c7f-4f0d-a2be-fcdab2477b19",
                projectId = "30d9e9fe-f43b-4097-a88e-dc19f0a5b025",
                title = "",
                status = MomensStatusEditType.TODO,
                role = TaskRole.PM,
                assignee = TaskDetailAssigneeModel(
                    id = "b9b1...e7",
                    name = "김민지",
                    avatarUrl = null,
                ),
                priority = ImportantLevel.MEDIUM,
                purpose = null,
                checklist = TaskDetailChecklistModel(
                    completedCount = 0,
                    totalCount = 0,
                    items = persistentListOf(),
                ),
                materials = persistentListOf(),
                openQuestions = persistentListOf(),
                nextAction = null,
            ),
        )
    }
}

sealed interface TaskDetailSideEffect {
    data class ShowSnackbar(val message: String) : TaskDetailSideEffect
}
