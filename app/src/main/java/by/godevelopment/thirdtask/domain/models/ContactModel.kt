package by.godevelopment.thirdtask.domain.models

data class ContactModel(
    val id: Int,
    val key: String,
    val taskPhoneNumber: String,
    val name: String,
    val surname: String,
    val email: String,
    val isSelected: Boolean
)
